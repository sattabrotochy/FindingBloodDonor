package com.okcodex.findingblooddonor.Chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.okcodex.findingblooddonor.Model.AddmemberList;
import com.okcodex.findingblooddonor.PopUpActivity.MessageSendRequestActivity;
import com.okcodex.findingblooddonor.R;
import com.squareup.picasso.Picasso;

public class FindFriendActivity extends AppCompatActivity {

    private static final String TAG="FindFriendActivity";


    RecyclerView userList;

    private DatabaseReference userRef;
    private FirebaseAuth mAuth;
    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);


        userList=findViewById(R.id.recyclViewID);
        userList.setLayoutManager(new LinearLayoutManager(this));

        userRef= FirebaseDatabase.getInstance().getReference("MyUser");
        mAuth=FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AddmemberList> options=
                new FirebaseRecyclerOptions.Builder<AddmemberList>()
                        .setQuery(userRef,AddmemberList.class )
                        .build();


        FirebaseRecyclerAdapter<AddmemberList, FindFriendActivity.FindFriendViewHolder> adapter=new FirebaseRecyclerAdapter<AddmemberList, FindFriendActivity.FindFriendViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final FindFriendActivity.FindFriendViewHolder findFriendViewHolder, final int i, @NonNull final AddmemberList contacts)
            {



                findFriendViewHolder.userName.setText(contacts.getName());
                Log.d(TAG, "onBindViewHolder: "+contacts.getName());
                findFriendViewHolder.userAddress.setText(contacts.getAddress());
                findFriendViewHolder.userDate.setText(contacts.getDate());
                Picasso.get().load(contacts.getImage()).into(findFriendViewHolder.userProfileImage);




                findFriendViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(FindFriendActivity.this, MessageSendRequestActivity.class);
                        final String id=getRef(i).getKey();
                        intent.putExtra("Visit_user_Id",id);
                        Log.d(TAG, "onClick: "+id);
                        startActivity(intent);
                    }
                });




            }

            @NonNull
            @Override
            public FindFriendActivity.FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_profile,parent,false);

                return new FindFriendViewHolder(view) ;
            }
        };

        userList.setAdapter(adapter);
        adapter.startListening();
    }
    public static class FindFriendViewHolder extends RecyclerView.ViewHolder {

        TextView userName,userAddress,userDate;
        CircularImageView userProfileImage;

        public FindFriendViewHolder(@NonNull View itemView) {


            super(itemView);

            userName=itemView.findViewById(R.id.search_name);
            userAddress=itemView.findViewById(R.id.search_address);
            userDate=itemView.findViewById(R.id.search_blood_donor);
            userProfileImage=itemView.findViewById(R.id.user_search_profile_image);





        }
    }
}