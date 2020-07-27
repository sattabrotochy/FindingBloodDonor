package com.okcodex.findingblooddonor.Post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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
import com.okcodex.findingblooddonor.Model.Commentlist;
import com.okcodex.findingblooddonor.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CommentActivity extends AppCompatActivity {


    private static final String TAG = "CommentActivity";
    Context context=CommentActivity.this;
    RecyclerView recyclerView;
    EditText commentEdit;
    ImageButton commentSendButton;
    DatabaseReference userRef,postRef,commentRef;
    FirebaseAuth mAuth;
    String name,image,currentUserId,postKey,writeComment,currentDate,currentTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);


        postKey=getIntent().getStringExtra("postID");
        Log.d(TAG, "onCreate: "+postKey);

        allViewFind();
        commentUserdata();




        commentSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComment();
            }
        });


    }

    private void allViewFind()
    {
        recyclerView=findViewById(R.id.commentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        commentEdit=findViewById(R.id.commentEditText);
        commentSendButton=findViewById(R.id.commentSend);
        userRef= FirebaseDatabase.getInstance().getReference().child("MyUser");
        postRef=FirebaseDatabase.getInstance().getReference();
        commentRef=FirebaseDatabase.getInstance().getReference().child("Comment").child(postKey);
        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();

    }

    private void commentUserdata() {

        userRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {
                    name=snapshot.child("name").getValue().toString();
                    image=snapshot.child("image").getValue().toString();


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void postComment()
    {

        Calendar calForDate=Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat date=new SimpleDateFormat("dd-MMMM-yyyy");
        currentDate=date.format(calForDate.getTime());

        Calendar calendar=Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat time=new SimpleDateFormat("HH:mm");
        currentTime=time.format(calendar.getTime());


        writeComment=commentEdit.getText().toString();
        if (writeComment.isEmpty())
        {
            commentEdit.setText("leko va kicu");
        }
        else
        {

            HashMap data=new HashMap();
            data.put("commentUserName",name);
            data.put("commentUserImage",image);
            data.put("date",currentDate);
            data.put("time",currentTime);
            data.put("commentText",writeComment);

            commentRef.push().setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isSuccessful())
                    {
                       commentEdit.setText("");
                        finish();
                    }
                }
            });



        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Commentlist> options=new
                FirebaseRecyclerOptions.Builder<Commentlist>()
                .setQuery(commentRef,Commentlist.class)
                .build();

        FirebaseRecyclerAdapter<Commentlist,commentViewHolder> adapter=
                new FirebaseRecyclerAdapter<Commentlist, commentViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull commentViewHolder holder, int i, @NonNull Commentlist commentlist)
                    {
                        holder.name.setText(commentlist.getCommentUserName());
                        holder.comment.setText(commentlist.getCommentText());
                        holder.time.setText(commentlist.getTime());
                        Picasso.get().load(commentlist.getCommentUserImage()).into(holder.imageView);

                    }

                    @NonNull
                    @Override
                    public commentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_layout,parent,false);

                        return new commentViewHolder(view);
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
    public class commentViewHolder extends RecyclerView.ViewHolder {

        TextView name,comment,time;
        CircularImageView imageView;

        public commentViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.commentUserName);
            comment=itemView.findViewById(R.id.userComent);
            time=itemView.findViewById(R.id.commentTime);
            imageView=itemView.findViewById(R.id.commentUserImage);
        }
    }
}