package com.okcodex.findingblooddonor.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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


public class ContactFragment extends Fragment {



    private DatabaseReference contactRef,userRef;
    private FirebaseAuth mAuth;
    private RecyclerView mycontactList;
    private String currentUser;

    public ContactFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser().getUid();
        contactRef= FirebaseDatabase.getInstance().getReference("Contacts").child(currentUser);
        userRef=FirebaseDatabase.getInstance().getReference("MyUser");


   View view= inflater.inflate(R.layout.fragment_contact, container, false);


   mycontactList=view.findViewById(R.id.recyclViewContactList);
   mycontactList.setLayoutManager(new LinearLayoutManager(getContext()));
   return view;
    }

    @Override
    public void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<Contacts> options=
                new FirebaseRecyclerOptions.Builder<Contacts>()
                .setQuery(contactRef, Contacts.class)
                .build();

        FirebaseRecyclerAdapter<Contacts ,contactViewHolder>adapter
                =new FirebaseRecyclerAdapter<Contacts, contactViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final contactViewHolder contactViewHolder, int i, @NonNull Contacts contacts)
            {
                String userIds=getRef(i).getKey();

                userRef.child(userIds).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        if (snapshot.exists())
                        {
                            String name=snapshot.child("name").getValue().toString();
                            String address=snapshot.child("address").getValue().toString();
                            String image=snapshot.child("image").getValue().toString();
                            contactViewHolder.name.setText(name);
                            contactViewHolder.address.setText(address);
                            Picasso.get().load(image).into(contactViewHolder.imageView);
                        }
                        else
                        {
                            Toast.makeText(getContext(), "No Contact", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {
                        Toast.makeText(getContext(), "No Contact", Toast.LENGTH_SHORT).show();

                    }
                });

            }

            @NonNull
            @Override
            public contactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {


                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.contactuserlist_layout,parent,false);
                return new contactViewHolder(view);


            }
        };

        mycontactList.setAdapter(adapter);
        adapter.startListening();

    }
    public static class contactViewHolder extends RecyclerView.ViewHolder {


        TextView name,address;
        CircularImageView imageView;
        @SuppressLint("CutPasteId")
        public contactViewHolder(@NonNull View itemView) {

            super(itemView);


            name=itemView.findViewById(R.id.contact_user_profile_name);
            address=itemView.findViewById(R.id.contact_user_address);
            imageView=itemView.findViewById(R.id.contact_users_profile_image);

        }
    }
}