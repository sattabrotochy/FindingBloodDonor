package com.okcodex.findingblooddonor.PopUpActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.okcodex.findingblooddonor.R;
import com.squareup.picasso.Picasso;

public class MessageSendRequestActivity extends AppCompatActivity {


 String TAG="MessageSendRequestActivity";

    private String receiverUUserId, current_state, sendUserID;

    private CircularImageView userProfileImage;
    private TextView userProfileName, cancel_button_id;
    private Button sendMessageRequestButton, DeclineRequestButton;

    private DatabaseReference userRef, ChatRequestRef, ContactRef;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_send_request);


        receiverUUserId = getIntent().getExtras().get("Visit_user_Id").toString();
        //   sendUserID=mAuth.getCurrentUser().getUid();


        userProfileImage = findViewById(R.id.visit_profile_image);
        userProfileName = findViewById(R.id.visit_user_name);
        cancel_button_id = findViewById(R.id.cancel_button_id);
        sendMessageRequestButton = findViewById(R.id.send_message_request_button);
        DeclineRequestButton = findViewById(R.id.decline_message_request_button);
        current_state = "new";


        userRef = FirebaseDatabase.getInstance().getReference("MyUser");
        ChatRequestRef = FirebaseDatabase.getInstance().getReference().child("ChatRequest");
        ContactRef = FirebaseDatabase.getInstance().getReference().child("Contacts");
        mAuth = FirebaseAuth.getInstance();
        sendUserID = mAuth.getCurrentUser().getUid();


        retriveUserInfo();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        setFinishOnTouchOutside(false);
        getWindow().setLayout((int) (width * .9), (int) (height * .7));

        
        sendMessageRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MessageSendRequestActivity.this, "lalala", Toast.LENGTH_SHORT).show();
            }
        });

        cancel_button_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void retriveUserInfo() {

        userRef.child(receiverUUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue().toString();
                    Log.d(TAG, "onDataChange: "+name);
                    String image = snapshot.child("image").getValue().toString();

                    userProfileName.setText(name);
                    Picasso.get().load(image).into(userProfileImage);

                    manageChatRequest();


                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

    }




    @SuppressLint("LongLogTag")
    private void manageChatRequest() {


        ChatRequestRef.child(sendUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                if (dataSnapshot.hasChild(receiverUUserId))
                {
                    String request_type=dataSnapshot.child(receiverUUserId)
                            .child("request_type").getValue().toString();

                    if (request_type.equals("sent"))
                    {
                        current_state="request_sent";
                        sendMessageRequestButton.setText("Cancel chat Request");

                    }
                    else if (request_type.equals("received"))
                    {

                        current_state="request_received";
                        sendMessageRequestButton.setText("Accept Chat Request");
                        DeclineRequestButton.setVisibility(View.VISIBLE);
                        DeclineRequestButton.setEnabled(true);

                        DeclineRequestButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {

                                CancelChatRequest();
                            }
                        });

                    }


                }
                else
                {
                    ContactRef.child(sendUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {

                            if (dataSnapshot.hasChild(receiverUUserId))
                            {
                                current_state="friends";
                                sendMessageRequestButton.setText("Remove this Contact");


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if (!sendUserID.equals(receiverUUserId))
        {

            Log.d(TAG, "ManageChatRequest: "+sendUserID);
            Log.d(TAG, "ManageChatRequest: "+receiverUUserId);

            sendMessageRequestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    sendMessageRequestButton.setEnabled(false);
                    if (current_state.equals("new"))
                    {
                        sendChatRequet();
                    }
                    if (current_state.equals("request_sent"))
                    {
                        CancelChatRequest();
                    }
                    if (current_state.equals("request_received"))
                    {
                        AcceptChatRequest();
                    }
                    if (current_state.equals("friends"))
                    {
                        RemoveSpecificContact();
                    }

                }
            });

        }
        else
        {
            sendMessageRequestButton.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "This is your id", Toast.LENGTH_SHORT).show();
        }


    }

    private void RemoveSpecificContact()
    {
        ContactRef.child(sendUserID).child(receiverUUserId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful())
                        {
                            ContactRef.child(receiverUUserId).child(sendUserID)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()){
                                                sendMessageRequestButton.setEnabled(true);
                                                current_state="new";
                                                sendMessageRequestButton.setText("Send message");


                                                DeclineRequestButton.setVisibility(View.INVISIBLE);
                                                DeclineRequestButton.setEnabled(false);
                                            }

                                        }
                                    });
                        }

                    }
                });

    }

    private void AcceptChatRequest()
    {

        ContactRef.child(sendUserID).child(receiverUUserId)
                .child("Contacts").setValue("Saved")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {

                        if (task.isSuccessful())
                        {
                            ContactRef.child(receiverUUserId).child(sendUserID)
                                    .child("Contacts").setValue("Saved")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {

                                            if (task.isSuccessful())
                                            {

                                                ChatRequestRef.child(sendUserID).child(receiverUUserId)
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task)
                                                            {

                                                                if (task.isSuccessful())
                                                                {

                                                                    ChatRequestRef.child(receiverUUserId).child(sendUserID)
                                                                            .removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task)
                                                                                {

                                                                                    if (task.isSuccessful())
                                                                                    {
                                                                                        sendMessageRequestButton.setEnabled(true);
                                                                                        current_state="friends";
                                                                                        sendMessageRequestButton.setText("Remove this Contact");

                                                                                        DeclineRequestButton.setVisibility(View.INVISIBLE);
                                                                                        DeclineRequestButton.setEnabled(false);


                                                                                    }
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                    }
                });

    }

    private void CancelChatRequest()
    {

        ChatRequestRef.child(sendUserID).child(receiverUUserId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful())
                        {
                            ChatRequestRef.child(receiverUUserId).child(sendUserID)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()){
                                                sendMessageRequestButton.setEnabled(true);
                                                current_state="new";
                                                sendMessageRequestButton.setText("Send message");


                                                DeclineRequestButton.setVisibility(View.INVISIBLE);
                                                DeclineRequestButton.setEnabled(false);
                                            }

                                        }
                                    });
                        }

                    }
                });
    }

    private void sendChatRequet()
    {
        ChatRequestRef.child(sendUserID).child(receiverUUserId)
                .child("request_type").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){

                            ChatRequestRef.child(receiverUUserId).child(sendUserID)
                                    .child("request_type").setValue("received")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()){
                                                sendMessageRequestButton.setEnabled(true);
                                                current_state="request_sent";
                                                sendMessageRequestButton.setText("Cancel chat Request");

                                            }
                                        }
                                    });
                        }


                    }
                });

    }

}
