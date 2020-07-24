package com.okcodex.findingblooddonor.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.okcodex.findingblooddonor.Model.Contacts;
import com.okcodex.findingblooddonor.R;
import com.squareup.picasso.Picasso;

public class RequestFragment extends Fragment {

    String TAG="RequestFragment";
    RecyclerView myRequest;
    DatabaseReference chatRequestRef,userRef,contactRef;
    FirebaseAuth mAuth;
    String currentUserId,  requestUserName;


    public RequestFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        contactRef=FirebaseDatabase.getInstance().getReference().child("Contacts");
        chatRequestRef= FirebaseDatabase.getInstance().getReference("ChatRequest");
        userRef= FirebaseDatabase.getInstance().getReference("MyUser");

        View view= inflater.inflate(R.layout.fragment_request, container, false);


        myRequest=view.findViewById(R.id.request_recycleView);
        myRequest.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;


    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Contacts>options=
                new FirebaseRecyclerOptions.Builder<Contacts>()
                .setQuery(chatRequestRef.child(currentUserId),Contacts.class)
                .build();

        FirebaseRecyclerAdapter<Contacts,MyRequestViewHolder>adapter=
                new FirebaseRecyclerAdapter<Contacts, MyRequestViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final MyRequestViewHolder myRequestViewHolder, int i, @NonNull Contacts contacts)
                    {


                        final String list_user_id=getRef(i).getKey();

                        DatabaseReference getTypeRef=getRef(i).child("request_type").getRef();
                        getTypeRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot)
                            {
                                if (snapshot.exists()) {
                                    String type = snapshot.getValue().toString();
                                    if (type.equals("received"))
                                    {
                                        userRef.child(list_user_id).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot)
                                            {
                                                if (snapshot.hasChild("image"))
                                                {
                                                     requestUserName=snapshot.child("name").getValue().toString();
                                                    String requestUserAddress=snapshot.child("address").getValue().toString();
                                                    String requestUserImage=snapshot.child("image").getValue().toString();

                                                    myRequestViewHolder.name.setText(requestUserName);
                                                    myRequestViewHolder.address.setText(requestUserAddress);
                                                    Picasso.get().load(requestUserImage).into(myRequestViewHolder.imageView);

                                                }

                                                myRequestViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v)
                                                    {


                                                        CharSequence option[]=new CharSequence[]
                                                                {
                                                                        "Accept",
                                                                        "Cancel"
                                                                };
                                                        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                                                        builder.setTitle(requestUserName+" Chat Request");
                                                        builder.setItems(option, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which)
                                                            {

                                                                if (which==0)
                                                                {

                                                                    contactRef.child(currentUserId).child(list_user_id).child("Contacts")
                                                                            .setValue("saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task)
                                                                        {
                                                                            if (task.isSuccessful())
                                                                            {
                                                                                contactRef.child(list_user_id).child(currentUserId).child("Contacts")
                                                                                        .setValue("saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task)
                                                                                    {
                                                                                        if (task.isSuccessful())
                                                                                        {
                                                                                            chatRequestRef.child(currentUserId).child(list_user_id)
                                                                                                    .removeValue()
                                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                                                            if (task.isSuccessful())
                                                                                                            {
                                                                                                                chatRequestRef.child(list_user_id).child(currentUserId)
                                                                                                                        .removeValue()
                                                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                            @Override
                                                                                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                                                                                if (task.isSuccessful())
                                                                                                                                {
                                                                                                                                    Toast.makeText(getContext(), "Contact add", Toast.LENGTH_SHORT).show();

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
                                                                if (which==1)
                                                                {
                                                                    chatRequestRef.child(currentUserId).child(list_user_id)
                                                                            .removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                                    if (task.isSuccessful())
                                                                                    {
                                                                                        chatRequestRef.child(list_user_id).child(currentUserId)
                                                                                                .removeValue()
                                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                                                        if (task.isSuccessful())
                                                                                                        {
                                                                                                            Toast.makeText(getContext(), "Contact deleted", Toast.LENGTH_SHORT).show();

                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                    }
                                                                                }
                                                                            });

                                                                }
                                                            }
                                                        });

                                                        builder.show();





                                                    }
                                                });

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public MyRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                       View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_user_iten_list,parent,false);
                       return new MyRequestViewHolder(view);

                    }
                };

        myRequest.setAdapter(adapter);
        adapter.startListening();

    }
    public static class MyRequestViewHolder extends RecyclerView.ViewHolder {

        TextView name,address;
        CircularImageView imageView;
        Button AcceptButton,CancelButton;

        public MyRequestViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.user_profile_name);
            address=itemView.findViewById(R.id.user_address);
            imageView=itemView.findViewById(R.id.users_profile_image);
            AcceptButton=itemView.findViewById(R.id.request_accept_btn);
            CancelButton=itemView.findViewById(R.id.request_cancel_btn);



        }
    }
}