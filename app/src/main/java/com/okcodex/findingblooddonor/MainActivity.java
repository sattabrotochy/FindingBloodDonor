package com.okcodex.findingblooddonor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.okcodex.findingblooddonor.Chat.ChatActivity;
import com.okcodex.findingblooddonor.Member.AddMemberActivity;
import com.okcodex.findingblooddonor.Member.AllMemberActivity;
import com.okcodex.findingblooddonor.Post.BloodPostActivity;
import com.okcodex.findingblooddonor.Profile.UserProfileActivity;

public class MainActivity extends AppCompatActivity {
    private Context context=MainActivity.this;

    public static final String TAG="MainActivity";


    LinearLayout  searchBloodDonor,addMember,userProfile,bloodNeededPost,allMember,personalChat,linearLayout;
    private DatabaseReference userRef;
    TextView userCount;
    FirebaseAuth mAuth;
    String currentUserId;
    String YesNO,checkYesNO,lala;
    TextView shuvo1,shuvo2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        currentUserId=getIntent().getStringExtra("currentUser");
        Log.d(TAG, "onCreate: "+currentUserId);
        findAllData();







        SharedPreferences sharedPreferences=getSharedPreferences("lala",MODE_PRIVATE);
        boolean check=sharedPreferences.getBoolean("data",true);

        if (check)
        {
            alertDilog();

        }



        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAddmemberActivity();
            }
        });



        Log.d(TAG, "onCreate: "+YesNO);
        searchBloodDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                goToSearchActivity();

            }
        });

          userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                goToProfileActivity();
            }
        });



        personalChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                goTOPersonalActivity();
            }
        });

        allMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                goToAllMemberActivity();

            }
        });

        bloodNeededPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                goToBloodPostActivity();

            }
        });



    }







    private void alertDilog() {
       new AlertDialog.Builder(this)

               .setMessage("lalala")
               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which)
                   {
                       goToAddmemberActivity();
                       dialog.dismiss();
                   }
               })
               .setNegativeButton("No", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which)
                   {
                       dialog.dismiss();
                   }
               })

               .create().show();

        SharedPreferences sharedPreferences=getSharedPreferences("lala",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("data",false);
        editor.apply();

    }


    private void findAllData()
    {
        searchBloodDonor=findViewById(R.id.search_blood_donor);
        addMember=findViewById(R.id.add_member);
        userProfile=findViewById(R.id.profile);
        bloodNeededPost=findViewById(R.id.blood_need_post);
        allMember=findViewById(R.id.all_member);
        personalChat=findViewById(R.id.personal_chat);
        userRef= FirebaseDatabase.getInstance().getReference();
        userCount=findViewById(R.id.userCount);

        linearLayout=findViewById(R.id.linerId);
        shuvo1=findViewById(R.id.Yes);
        shuvo2=findViewById(R.id.No);


        mAuth=FirebaseAuth.getInstance();

    }


    private void goToSearchActivity()
    {

        Intent intent=new Intent(context,SearchActivity.class);
        startActivity(intent);
    }




    private void goToAddmemberActivity()
    {

        Intent intent=new Intent(context, AddMemberActivity.class);
        startActivity(intent);
    }



    private void goToProfileActivity()
    {

        Intent intent=new Intent(context, UserProfileActivity.class);
        startActivity(intent);
    }


    private void goToBloodPostActivity()
    {

        Intent intent=new Intent(context, BloodPostActivity.class);
        startActivity(intent);
    }

    private void goToAllMemberActivity()
    {

        currentUserId=mAuth.getCurrentUser().getUid();
        if (currentUserId==null)
        {
            Toast.makeText(context, "lala", Toast.LENGTH_SHORT).show();

        }else
        {
            Intent intent=new Intent(context, AllMemberActivity.class);
            startActivity(intent);
        }

    }


    private void goTOPersonalActivity()
    {
        Intent intent=new Intent(context, ChatActivity.class);
        startActivity(intent);


    }

    @Override
    protected void onStart() {
        super.onStart();



        userRef.child("MyUser").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String count= String.valueOf((int) dataSnapshot.getChildrenCount());
                String data=count;
                userCount.setText("সর্বমোট নিবন্ধিত রক্তদাতা : " + data);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}