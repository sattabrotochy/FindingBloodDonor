package com.okcodex.findingblooddonor.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.okcodex.findingblooddonor.Adapter.MessageAdpter;
import com.okcodex.findingblooddonor.Model.Messages;
import com.okcodex.findingblooddonor.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatShowActivity extends AppCompatActivity {

    Toolbar chatToolbar;
    private TextView chatUserName,chatUserLastSeen;;
    CircularImageView imageView;
    String messageReceiveName,messageReceiveId,messageReceiveImage,usermessage,messageSenderID;

    private RecyclerView chatList;
    ImageButton sendMessageButton;
    EditText messageInputText;

    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;



    private  final List<Messages> messageList=new ArrayList<>();

    private LinearLayoutManager linearLayoutManager;
    private MessageAdpter messageAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_show);


        messageReceiveId=getIntent().getStringExtra("user_id").toString();
        messageReceiveName=getIntent().getStringExtra("user_name").toString();
        messageReceiveImage=getIntent().getStringExtra("user_image").toString();


        IntializeControlers();

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                sendMessage();
            }
        });

    }

    private void IntializeControlers() {

        chatToolbar=findViewById(R.id.chat_toolbar);
        setSupportActionBar(chatToolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);


        LayoutInflater inflater=(LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView=inflater.inflate(R.layout.custom_chat_bar,null);
        actionBar.setCustomView(actionBarView);


        sendMessageButton=findViewById(R.id.send_message_but);
        messageInputText=findViewById(R.id.input_message);


        chatUserName=findViewById(R.id.custom_profile_name);
        chatUserLastSeen=findViewById(R.id.custom_user_last_seen);
        imageView=findViewById(R.id.custom_profile_image);




        chatList=findViewById(R.id.private_recycler_list);



        messageAdapter=new MessageAdpter(messageList);
        linearLayoutManager=new LinearLayoutManager(this);
        chatList.setLayoutManager(linearLayoutManager);
        chatList.setAdapter(messageAdapter);

        mAuth=FirebaseAuth.getInstance();
        messageSenderID=mAuth.getCurrentUser().getUid();
        RootRef= FirebaseDatabase.getInstance().getReference();


    }


    @Override
    protected void onStart() {
        super.onStart();


        RootRef.child("Message").child(messageSenderID).child(messageReceiveId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                    {
                        Messages message=dataSnapshot.getValue(Messages.class);
                        String TAG="ChatActivity";
                        Log.d(TAG, "onChildAdded: "+message);
                        messageList.add(message);
                        messageAdapter.notifyDataSetChanged();
                       // DisplayLastSeen();
                        chatList.smoothScrollToPosition(chatList.getAdapter().getItemCount());

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void sendMessage()
    {
        usermessage=messageInputText.getText().toString();
        if (usermessage.isEmpty())
        {
            Toast.makeText(ChatShowActivity.this, "kicu leko mia", Toast.LENGTH_SHORT).show();

        }
        else
        {

            String messageReceiverRef="Message/"+messageSenderID+"/"+messageReceiveId;
            String messageSenderRef="Message/"+messageReceiveId+"/"+messageSenderID;

            DatabaseReference userMessageKeyRef=RootRef.child("Messages").
                    child(messageSenderID).child(messageReceiveId).push();


            String messagePushId=userMessageKeyRef.getKey();
            Map messageTextBody= new HashMap();
            messageTextBody.put("message",usermessage);
            messageTextBody.put("type","text");
            messageTextBody.put("from",messageSenderID);


            Map messageBodyDetails=new HashMap();
            messageBodyDetails.put(messageSenderRef + "/"+ messagePushId,messageTextBody);

            messageBodyDetails.put(messageReceiverRef + "/"+ messagePushId,messageTextBody);

            RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task)
                {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(ChatShowActivity.this, "message send", Toast.LENGTH_SHORT).show();


                    }else
                    {
                        Toast.makeText(ChatShowActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                    messageInputText.setText(" ");
                }
            });
        }

    }
}