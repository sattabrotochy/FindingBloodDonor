package com.okcodex.findingblooddonor.Member;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.okcodex.findingblooddonor.MainActivity;
import com.okcodex.findingblooddonor.Model.AddmemberList;
import com.okcodex.findingblooddonor.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddMemberActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener ,DatePickerDialog.OnDateSetListener {

    private CircularImageView profileImage;
    private static final String TAG = "AddMemberActivity";
    private Context context = AddMemberActivity.this;

    private EditText userName, userEmail, userPassword, userAddress, userPhoneNumber;
    private TextView bloodLastDate, dateOFBarth;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button dataLoadButton;
    private ProgressDialog progressDialog;


    private Spinner bloodSpinner;



    String name, email, password, address, number, lastDate, gender, blood, currentUserID;


    private DatabaseReference userRef,BloodGroupRef;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference;


    String currentDate, blooddate;
    Date today;


    private static final int GalleryPick = 1;
    Uri imageUri;
    String image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);


        allDataInitilize();

        Calendar calendar = Calendar.getInstance();
        today = calendar.getTime();
        currentDate = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GalleryPick);


            }
        });

        bloodLastDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickBloodDate();
            }
        });

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.blood_Name, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodSpinner.setAdapter(arrayAdapter);
        bloodSpinner.setOnItemSelectedListener(this);


        dataLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addMember();
            }
        });


    }


    private void allDataInitilize() {


        profileImage = findViewById(R.id.profile_image);
        userName = findViewById(R.id.edTxtUsername);
        userAddress = findViewById(R.id.edTxtUserAddress);
        userEmail = findViewById(R.id.edTxtUserEmail);
        userPassword = findViewById(R.id.edTxtUserPassword);
        bloodLastDate = findViewById(R.id.txt_user_last_blood_date);
        // dateOFBarth=findViewById(R.id.txt_date_of_barth);
//        womenButton=findViewById(R.id.woman_Radio_button);
//        manButton=findViewById(R.id.man_Radio_button);
        userPhoneNumber = findViewById(R.id.edTxtUserPhoneNumber);
        bloodSpinner = findViewById(R.id.spinner_blood_group_name);
        radioGroup = findViewById(R.id.radioGroup);
        dataLoadButton = findViewById(R.id.update_button);
        progressDialog=new ProgressDialog(this);


        userRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        BloodGroupRef=FirebaseDatabase.getInstance().getReference();
        storage=FirebaseStorage.getInstance();
        storageReference= storage.getReference("Image");



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(profileImage);

        }

    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    private void pickBloodDate() {
        DatePickerDialog pickerDialog = new DatePickerDialog(
                this,
                (DatePickerDialog.OnDateSetListener) this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        pickerDialog.show();


    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month + 3);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.YEAR, year);
        Date feture = calendar.getTime();
        blooddate = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());


        if (blooddate.equals(currentDate) || today.after(feture)) {

            lastDate = "রক্ত দানের জন্য উপলব্ধ আছেন";
            bloodLastDate.setText("রক্ত দানের জন্য উপলব্ধ আছেন");

        } else {
            lastDate = "রক্ত দানের জন্য উপলব্ধ নয়";
            bloodLastDate.setText("রক্ত দানের জন্য উপলব্ধ নয়");

        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        blood = parent.getItemAtPosition(position).toString();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void checkButton(View view) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        gender = radioButton.getText().toString();
        Log.d(TAG, "checkButton: " + gender);
    }


    private void addMember() {
        progressDialog.setTitle("Upload Your Data");
        progressDialog.setMessage("Please wait.....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        name = userName.getText().toString();
        address = userAddress.getText().toString();
        email = userEmail.getText().toString();
        password = userPassword.getText().toString();
        number = userPhoneNumber.getText().toString();


        if (name.isEmpty()) {
            userName.setText("");
            progressDialog.dismiss();
        }
        if (address.isEmpty()) {
            userAddress.setText("");
            progressDialog.dismiss();
        }
        if (email.isEmpty()) {
            userEmail.setText("");
            progressDialog.dismiss();
        }
        if (password.isEmpty()) {
            userPassword.setText("");
            progressDialog.dismiss();
        }
        if (number.isEmpty()) {
            userPhoneNumber.setText("");
            progressDialog.dismiss();
        } else {
            if (imageUri != null) {


                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful())
                        {

                            currentUserID=mAuth.getCurrentUser().getUid();

                            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

                            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("currentUserID",currentUserID);
                            editor.apply();

                            final StorageReference fileRef = storageReference.child(System.currentTimeMillis() +
                        "." + getFileExtension(imageUri));

                fileRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                image=uri.toString();


                                final AddmemberList data=new AddmemberList(currentUserID,name,address,image,lastDate,number,blood,gender);

                                userRef.child("MyUser").child(currentUserID).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)

                                    {
                                        if (task.isSuccessful())
                                        {
                                            switch (blood) {
                                                case "A+":
                                                    BloodGroupRef.child("A_positive").child(currentUserID).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {

                                                                Toast.makeText(context, "Upload data", Toast.LENGTH_SHORT).show();
                                                                progressDialog.dismiss();
                                                                String id=mAuth.getCurrentUser().getUid();
                                                                Intent intent=new Intent(context,MainActivity.class);
                                                                intent.putExtra("currentUser",id);
                                                                startActivity(intent);
                                                                gotoMainActivity();
                                                            } else {
                                                                Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                progressDialog.dismiss();
                                                            }

                                                        }
                                                    });
                                                    break;
                                                case "A-":
                                                    BloodGroupRef.child("A_negative").child(currentUserID).push().setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {

                                                                Toast.makeText(context, "Upload data", Toast.LENGTH_SHORT).show();
                                                                progressDialog.dismiss();
                                                                String id=mAuth.getCurrentUser().getUid();
                                                                Intent intent=new Intent(context,MainActivity.class);
                                                                intent.putExtra("currentUser",id);
                                                                startActivity(intent);
                                                                gotoMainActivity();
                                                            } else {
                                                                Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                progressDialog.dismiss();
                                                            }

                                                        }
                                                    });

                                                    break;
                                                case "B+":
                                                    BloodGroupRef.child("B_positive").child(currentUserID).push().setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {

                                                                Toast.makeText(context, "Upload data", Toast.LENGTH_SHORT).show();
                                                                progressDialog.dismiss();
                                                                String id=mAuth.getCurrentUser().getUid();
                                                                Intent intent=new Intent(context,MainActivity.class);
                                                                intent.putExtra("currentUser",id);
                                                                startActivity(intent);
                                                                gotoMainActivity();
                                                            } else {
                                                                Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                progressDialog.dismiss();
                                                            }

                                                        }
                                                    });

                                                    break;
                                                case "B-":
                                                    BloodGroupRef.child("B_negative").child(currentUserID).push().setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {

                                                                Toast.makeText(context, "Upload data", Toast.LENGTH_SHORT).show();
                                                                progressDialog.dismiss();
                                                                String id=mAuth.getCurrentUser().getUid();
                                                                Intent intent=new Intent(context,MainActivity.class);
                                                                intent.putExtra("currentUser",id);
                                                                startActivity(intent);
                                                                gotoMainActivity();
                                                            } else {
                                                                Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                progressDialog.dismiss();
                                                            }

                                                        }
                                                    });

                                                    break;
                                                case "O+":
                                                    BloodGroupRef.child("O_positive").child(currentUserID).push().setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {

                                                                Toast.makeText(context, "Upload data", Toast.LENGTH_SHORT).show();
                                                                progressDialog.dismiss();
                                                                String id=mAuth.getCurrentUser().getUid();
                                                                Intent intent=new Intent(context,MainActivity.class);
                                                                intent.putExtra("currentUser",id);
                                                                startActivity(intent);
                                                                 gotoMainActivity();
                                                            } else {
                                                                Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                progressDialog.dismiss();
                                                            }

                                                        }
                                                    });

                                                    break;
                                                case "O-":
                                                    BloodGroupRef.child("O_negative").child(currentUserID).push().setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {

                                                                Toast.makeText(context, "Upload data", Toast.LENGTH_SHORT).show();
                                                                progressDialog.dismiss();
                                                                String id=mAuth.getCurrentUser().getUid();
                                                                Intent intent=new Intent(context,MainActivity.class);
                                                                intent.putExtra("currentUser",id);
                                                                startActivity(intent);
                                                                gotoMainActivity();
                                                            } else {
                                                                Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                progressDialog.dismiss();
                                                            }

                                                        }
                                                    });

                                                    break;
                                                case "AB+":
                                                    BloodGroupRef.child("AB_positive").child(currentUserID).push().setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {

                                                                Toast.makeText(context, "Upload data", Toast.LENGTH_SHORT).show();
                                                                progressDialog.dismiss();

                                                                String id=mAuth.getCurrentUser().getUid();
                                                                Intent intent=new Intent(context,MainActivity.class);
                                                                intent.putExtra("currentUser",id);
                                                                startActivity(intent);
                                                            } else {
                                                                Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                progressDialog.dismiss();

                                                            }

                                                        }
                                                    });

                                                    break;
                                                case "AB-":
                                                    BloodGroupRef.child("AB_negative").child(currentUserID).push().setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {

                                                                Toast.makeText(context, "Upload data", Toast.LENGTH_SHORT).show();
                                                                progressDialog.dismiss();
                                                                String id=mAuth.getCurrentUser().getUid();
                                                                Intent intent=new Intent(context,MainActivity.class);
                                                                intent.putExtra("currentUser",id);
                                                                startActivity(intent);
                                                                gotoMainActivity();
                                                            } else {
                                                                Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                progressDialog.dismiss();
                                                            }

                                                        }
                                                    });

                                                    break;
                                                default:
                                                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                    break;
                                            }
                                        }



                                    }
                                });
                            }
                        });
                    }
                });

                        }
                        else {


                        }

                    }
                });

//
            }
        }

    }

    private void gotoMainActivity() {

    }

}