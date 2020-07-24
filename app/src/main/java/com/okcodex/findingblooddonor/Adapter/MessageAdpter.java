package com.okcodex.findingblooddonor.Adapter;


import android.graphics.Color;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.okcodex.findingblooddonor.Model.Messages;
import com.okcodex.findingblooddonor.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdpter extends RecyclerView.Adapter<MessageAdpter.messageViewHolder>
{
    private static final String TAG = "MessageAdpter";
   List<Messages> messageList;
    private FirebaseAuth mAuth;
    DatabaseReference userRef;
    Messages message;

    public MessageAdpter(List<Messages> messageList){
       this.messageList=messageList;
    }

    public class messageViewHolder extends RecyclerView.ViewHolder
    {


        TextView senderMessageText;
        TextView receiverMessageText;
        CircularImageView receiverProfileImage;

        public messageViewHolder(@NonNull View itemView)
        {
            super(itemView);

            senderMessageText=itemView.findViewById(R.id.sender_message_text);
            receiverMessageText=itemView.findViewById(R.id.receiver_message_text);
            receiverProfileImage=itemView.findViewById(R.id.message_profile_image);




        }
    }

    @NonNull
    @Override
    public messageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {


        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_message_layout,parent,false);


        mAuth=FirebaseAuth.getInstance();
        userRef= FirebaseDatabase.getInstance().getReference();
        return new messageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final messageViewHolder holder, int position)
    {

        String messageSenderId=mAuth.getCurrentUser().getUid();
        message=messageList.get(position);


        String fromMessageID=message.getFrom();
        String fromMessageType=message.getType();


        userRef.child("MyUser").child(fromMessageID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.hasChild("image"))
                {
                    String REimage =dataSnapshot.child("image").getValue().toString();
                    Picasso.get().load(REimage).into(holder.receiverProfileImage);



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        holder.receiverMessageText.setVisibility(View.GONE);
        holder.receiverProfileImage.setVisibility(View.GONE);
        holder.senderMessageText.setVisibility(View.GONE);
        //    holder..setVisibility(View.GONE);
        holder.receiverProfileImage.setVisibility(View.GONE);


        if (fromMessageType.equals("text"))
        {
            if (fromMessageID.equals(messageSenderId))
            {
                holder.senderMessageText.setVisibility(View.VISIBLE);
                holder.senderMessageText.setBackgroundResource(R.drawable.sender_message_layout);
                holder.senderMessageText.setTextColor(Color.BLACK);
                holder.senderMessageText.setText(message.getMessage() );
            }
            else
            {
                holder.receiverProfileImage.setVisibility(View.VISIBLE);
                holder.receiverMessageText.setVisibility(View.VISIBLE);

                holder.receiverMessageText.setBackgroundResource(R.drawable.receiver_message_layout);
                holder.receiverMessageText.setTextColor(Color.BLACK);
                holder.receiverMessageText.setText(message.getMessage());
            }
        }


    }

    @Override
    public int getItemCount()

    {
        return messageList.size();
    }


}