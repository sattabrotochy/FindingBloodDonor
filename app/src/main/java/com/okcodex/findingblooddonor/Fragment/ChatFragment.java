package com.okcodex.findingblooddonor.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.okcodex.findingblooddonor.Chat.ChatShowActivity;
import com.okcodex.findingblooddonor.Model.Contacts;
import com.okcodex.findingblooddonor.Post.BloodPostActivity;
import com.okcodex.findingblooddonor.R;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import static android.content.ContentValues.TAG;


public class ChatFragment extends Fragment {


    private RecyclerView chatList;
    private DatabaseReference chatsRef,UserRef;
    private FirebaseAuth mauth ;

    private String currentUserId;

    public ChatFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_chat, container, false);


        mauth=FirebaseAuth.getInstance();
        currentUserId=mauth.getCurrentUser().getUid();
        chatsRef= FirebaseDatabase.getInstance().getReference().child("Contacts").child(currentUserId);
        UserRef=FirebaseDatabase.getInstance().getReference().child("MyUser");
        chatList=view.findViewById(R.id.chat_list);
        chatList.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


        savedata();

        FirebaseRecyclerOptions<Contacts> options=
                new FirebaseRecyclerOptions.Builder<Contacts>()
                        .setQuery(chatsRef,Contacts.class )
                        .build();


        FirebaseRecyclerAdapter<Contacts,ChatViewHolder> adapter=
                new FirebaseRecyclerAdapter<Contacts, ChatViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ChatViewHolder holder, int position, @NonNull Contacts contacts)
                    {
                        final  String userId=getRef(position).getKey();

                        UserRef.child(userId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                                if (dataSnapshot.exists())
                                {
                                    if (dataSnapshot.hasChild("image")) {
                                        final String reImage = dataSnapshot.child("image").getValue().toString();
                                        final String rtName = dataSnapshot.child("name").getValue().toString();

                                        holder.name.setText(rtName);
                                        holder.address.setText("last seen" + "\n" + "Date" + "Time");
                                        Picasso.get().load(reImage).into(holder.imageView);


                                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(getContext(), ChatShowActivity.class);
                                                intent.putExtra("user_id", userId);
                                                intent.putExtra("user_name", rtName);
                                                intent.putExtra("user_image",reImage);

                                                startActivity(intent);

                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError)
                            {

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.contactuserlist_layout,parent,false);
                        return  new ChatViewHolder(view);
                    }
                };

        chatList.setAdapter(adapter);
        adapter.startListening();


    }

    private void savedata()
    {
        SharedPreferences preferences= Objects.requireNonNull(getActivity()).getPreferences(Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor=preferences.edit();
        editor.putString("currentUser",currentUserId);
        editor.apply();

        String result=preferences.getString("currentUser","data not founnd");
        Log.d(TAG, "savedata: "+result);


    }


    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView name,address;
        CircularImageView imageView;
        public ChatViewHolder(@NonNull View itemView)
        {
            super(itemView);



            name=itemView.findViewById(R.id.contact_user_profile_name);
            address=itemView.findViewById(R.id.contact_user_address);
            imageView=itemView.findViewById(R.id.contact_users_profile_image);

        }
    }
}