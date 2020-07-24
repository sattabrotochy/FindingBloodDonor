package com.okcodex.findingblooddonor.Chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.okcodex.findingblooddonor.R;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG="ChatActivity";
    Context context=ChatActivity.this;

    private EditText edEmail,edPassword;
    private Button btnSingIn;
    private TextView accountCreate;
    private String email,password;
    private ProgressDialog progressDialogBar;
    private FirebaseAuth mauth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        InitilizeallText();

        btnSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                // gotoMainActivity();
                createAccountUser();
            }
        });


    }


    private void createAccountUser()

    {
        email=edEmail.getText().toString().trim();
        password=edPassword.getText().toString().trim();

        progressDialogBar.setTitle("Sign in");
        progressDialogBar.setMessage("Please wait.....");
        progressDialogBar.setCanceledOnTouchOutside(true);
        progressDialogBar.show();

        if (email.isEmpty()){
            edEmail.setError("Enter Email");
            progressDialogBar.dismiss();
        }

        if (password.isEmpty()){
            edPassword.setError("Enter Password");
            progressDialogBar.dismiss();
        }
        else
        {

            mauth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()){
                        gotoPersonalActivity();
                        Toast.makeText(context, "Sing In Successful", Toast.LENGTH_SHORT).show();
                        progressDialogBar.dismiss();
                    }
                    else {
                        Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialogBar.dismiss();
                    }

                }
            });
        }






    }

    private void gotoPersonalActivity()
    {
        startActivity(new Intent(context, PersonalChatActivity.class));
    }


    private void InitilizeallText() {

        edEmail=findViewById(R.id.chat_edTxtUserEmail);
        edPassword=findViewById(R.id.chat_edTxt_User_Password);
        btnSingIn=findViewById(R.id.chat_singIn_button);

        mauth=FirebaseAuth.getInstance();
        progressDialogBar=new ProgressDialog(this);

    }
}