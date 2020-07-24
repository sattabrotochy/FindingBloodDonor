package com.okcodex.findingblooddonor.Post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.okcodex.findingblooddonor.MainActivity;
import com.okcodex.findingblooddonor.Member.AddMemberActivity;
import com.okcodex.findingblooddonor.Model.PostList;
import com.okcodex.findingblooddonor.R;
import com.squareup.picasso.Picasso;

public class BloodPostActivity extends AppCompatActivity {

    Context context = BloodPostActivity.this;
    public static final String TAG = "BloodPostActivity";

    ImageButton backButton;
    TextView postData;
    String currentUserId;
    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    DatabaseReference userRef, postRef, likesRef;
    Boolean likeChecker = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_post);


        backButton = findViewById(R.id.back_button);
        postData = findViewById(R.id.post_Id);
        recyclerView = findViewById(R.id.blood_post_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        postRef = FirebaseDatabase.getInstance().getReference().child("BloodPost");
        userRef = FirebaseDatabase.getInstance().getReference().child("MyUser");
        likesRef = FirebaseDatabase.getInstance().getReference().child("Likes");



        postData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, UserBloodPostActivity.class));
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, MainActivity.class));
            }
        });

        DisplayAllPost();
    }

    private void DisplayAllPost()


    {

        FirebaseRecyclerOptions<PostList> options=
                new FirebaseRecyclerOptions.Builder<PostList>()
                .setQuery(postRef,PostList.class)
                .build();

     FirebaseRecyclerAdapter<PostList,postViewHolder> firebaseRecyclerAdapter=
             new FirebaseRecyclerAdapter<PostList, postViewHolder>(options) {
                 @Override
                 protected void onBindViewHolder(@NonNull postViewHolder holder, int i, @NonNull PostList model)
                 {

                     final String postkey=getRef(i).getKey();

                     holder.name.setText(model.postUsername);
                     holder.date.setText(model.date);
                     holder.time.setText(model.time);
                     holder.blood.setText(model.postBloodGroup);
                     holder.place.setText(model.postUserAddress);
                     holder.number.setText(model.postUserNUmber);
                     Picasso.get().load(model.postUserImage).into(holder.imageView);


                     holder.like.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v)
                         {
                             likeChecker=true;
                             likesRef.addValueEventListener(new ValueEventListener() {
                                 @Override
                                 public void onDataChange(@NonNull DataSnapshot snapshot)
                                 {
                                     if (snapshot.child(postkey).hasChild(currentUserId))
                                     {

                                     }

                                 }

                                 @Override
                                 public void onCancelled(@NonNull DatabaseError error) {

                                 }
                             });

                         }
                     });
                 }

                 @NonNull
                 @Override
                 public postViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                     View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.blood_post_layout,parent,false);
                     return new postViewHolder(view);

                 }
             };
     recyclerView.setAdapter(firebaseRecyclerAdapter);
     firebaseRecyclerAdapter.startListening();



    }

    public static  class  postViewHolder extends RecyclerView.ViewHolder {

        TextView name, time, blood, place, number, likesView,date;
        ImageButton comment, like;
        CircularImageView imageView;



        public postViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.home_profile_image);
            name = itemView.findViewById(R.id.home_post_user_name);
            time = itemView.findViewById(R.id.home_post_user_time);
            date = itemView.findViewById(R.id.home_post_user_Date);
            blood = itemView.findViewById(R.id.home_post_user_blood);
            place = itemView.findViewById(R.id.home_post_user_address);
            number = itemView.findViewById(R.id.post_user_number);

            //imageButton
            comment = itemView.findViewById(R.id.post_comment);
            like = itemView.findViewById(R.id.post_like);

            //likeShow
            likesView = itemView.findViewById(R.id.liks_show_view);
        }
    }

}




