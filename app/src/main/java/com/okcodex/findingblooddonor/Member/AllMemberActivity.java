package com.okcodex.findingblooddonor.Member;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.okcodex.findingblooddonor.Chat.FindFriendActivity;
import com.okcodex.findingblooddonor.Model.AddmemberList;
import com.okcodex.findingblooddonor.Model.UserList;
import com.okcodex.findingblooddonor.PopUpActivity.MessageSendRequestActivity;
import com.okcodex.findingblooddonor.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AllMemberActivity extends AppCompatActivity {


    private static final String TAG="AllMemberActivity";

    RecyclerView userList;

    private DatabaseReference userRef;
    private FirebaseAuth mAuth;
    String currentUserID,searchName;
    EditText edTextSearch;
    ImageButton searchButton;

    List<UserList> userLists=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_member);



        edTextSearch=findViewById(R.id.edText_search_all_member);
        searchButton=findViewById(R.id.search_image_button);

        userList=findViewById(R.id.recyclView);
        userList.setLayoutManager(new LinearLayoutManager(this));

        userRef= FirebaseDatabase.getInstance().getReference("MyUser");
        mAuth=FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();




        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) 
            {


                seachmember();
                
            }
        });

    }

    private void seachmember( )
    {
        searchName=edTextSearch.getText().toString();
        if (searchName.isEmpty())
        {
            edTextSearch.setText("");
        }

        Query searchQuery=userRef.orderByChild("name").startAt(searchName).endAt(searchName+"\uf8ff");
        FirebaseRecyclerOptions<AddmemberList> options=
                new FirebaseRecyclerOptions.Builder<AddmemberList>()
                        .setQuery(searchQuery,AddmemberList.class )
                        .build();
        FirebaseRecyclerAdapter<AddmemberList,myView> adapter=new FirebaseRecyclerAdapter<AddmemberList, myView>(options) {
            @Override
            protected void onBindViewHolder(@NonNull myView holder, int i, @NonNull AddmemberList addmemberList)
            {

                holder.name.setText(addmemberList.getName());
                holder.address.setText(addmemberList.getAddress());
                holder.date.setText(addmemberList.getDate());
                Picasso.get().load(addmemberList.getImage()).into(holder.imageView);

            }

            @NonNull
            @Override
            public myView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_profile,parent,false);
                return  new myView(view);
            }
        };
        userList.setAdapter(adapter);
        adapter.startListening();

    }

public class  myView extends RecyclerView.ViewHolder {


        TextView name,address,date;
        CircularImageView imageView;

    public myView(@NonNull View itemView) {
        super(itemView);



        name=itemView.findViewById(R.id.search_name);
        address=itemView.findViewById(R.id.search_address);
        date=itemView.findViewById(R.id.search_blood_donor);
        imageView=itemView.findViewById(R.id.user_search_profile_image);
    }
}

    @Override
    protected void onStart() {
        super.onStart();
        seachmember();
    }
}

