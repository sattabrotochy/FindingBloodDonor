package com.okcodex.findingblooddonor.Post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.okcodex.findingblooddonor.MainActivity;
import com.okcodex.findingblooddonor.Member.AddMemberActivity;
import com.okcodex.findingblooddonor.Model.PostList;
import com.okcodex.findingblooddonor.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class UserBloodPostActivity extends AppCompatActivity
{



    public static final String TAG="UserBloodPostActivity";
    private DatabaseReference userRef,postRef;
    private FirebaseAuth mAuth;
    private String currentUser;
    String name,address,image,bldGroup,number,currentDate,currentTime,redomDateTime;
   private long postCount=0;

    long timeMillis;


    private EditText bloodGroup,bloodAddress,bloodNumber;
    private Button postButton;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_blood_post);


        initilizeAllData();
         timeMillis = System.currentTimeMillis();


        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                postData();
            }
        });





    }



    private void initilizeAllData()
    {
        bloodGroup=findViewById(R.id.post_blood_group);
        bloodAddress=findViewById(R.id.post_address);
        bloodNumber=findViewById(R.id.post_number);
        postButton=findViewById(R.id.blood_post);
        progressDialog=new ProgressDialog(this);



        userRef= FirebaseDatabase.getInstance().getReference("MyUser");
        postRef= FirebaseDatabase.getInstance().getReference().child("BloodPost");
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser().getUid();

        Log.d(TAG, "initilizeAllData: "+currentUser);

    }

    @Override
    protected void onStart() {
        super.onStart();






        userRef.child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                name=snapshot.child("name").getValue().toString();
                image=snapshot.child("image").getValue().toString();
                String num=snapshot.child("number").getValue().toString();
                Log.d(TAG, "onDataChange: "+num);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {
                    postCount=snapshot.getChildrenCount();
                    Log.d(TAG, "onDataChange: "+postCount);
                }
                else
                {
                    postCount=0;
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






    }


    private void postData()
    {





        progressDialog.setTitle("Upload Your Post");
        progressDialog.setMessage("Please wait.....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();



        Calendar calForDate=Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat date=new SimpleDateFormat("dd-MMMM-yyyy");
        currentDate=date.format(calForDate.getTime());

        Calendar calendar=Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat time=new SimpleDateFormat("HH:mm");
        currentTime=time.format(calendar.getTime());

        redomDateTime=currentDate+currentTime;


        bldGroup=bloodGroup.getText().toString();
        address=bloodAddress.getText().toString();
        number=bloodNumber.getText().toString();
        if (bldGroup.isEmpty())
        {
            bloodGroup.setError("");
            progressDialog.dismiss();
        }
        if (address.isEmpty())
        {
            bloodAddress.setError("");
            progressDialog.dismiss();
        }
        if (number.isEmpty())
        {
            bloodNumber.setError("");
            progressDialog.dismiss();
        }

        else
        {




          HashMap postmap=new HashMap();
          postmap.put("postId",currentUser);
          postmap.put("postUsername",name);
          postmap.put("postUserAddress",address);
          postmap.put("postBloodGroup",bldGroup);
          postmap.put("postUserImage",image);
          postmap.put("postUserNUmber",number);
          postmap.put("time",currentTime);
          postmap.put("date",currentDate);
          postmap.put("count",postCount);



            postRef.push().setValue(postmap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task)
                {

                    if (task.isSuccessful())
                    {
                        Toast.makeText(UserBloodPostActivity.this, "Post is Done", Toast.LENGTH_SHORT).show();
                        bloodGroup.setText("");
                        bloodAddress.setText("");
                        bloodNumber.setText("");
                        startActivity(new Intent(UserBloodPostActivity.this, MainActivity.class));
                        progressDialog.dismiss();
                      finish();
                    }
                    else
                    {
                        Toast.makeText(UserBloodPostActivity.this, "oiche na", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



}