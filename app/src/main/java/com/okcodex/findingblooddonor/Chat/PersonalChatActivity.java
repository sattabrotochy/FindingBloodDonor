package com.okcodex.findingblooddonor.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.okcodex.findingblooddonor.Adapter.TabAdapter;
import com.okcodex.findingblooddonor.R;

public class PersonalChatActivity extends AppCompatActivity {


    private Toolbar mtoolbar;
    private ViewPager myviewPager;
    private TabLayout mytabLayout;
    private TabAdapter mytabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_chat);


        mtoolbar=findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("My Chat");


        myviewPager=findViewById(R.id.main_tabs_pager);
        mytabAdapter=new TabAdapter(getSupportFragmentManager());
        myviewPager.setAdapter(mytabAdapter);

        mytabLayout=findViewById(R.id.main_tabs);
        mytabLayout.setupWithViewPager(myviewPager);




    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return  true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.main_find_friends_option:
                SendUseFindFriendsSettingActivity();
                return true;

            case R.id.main_logout_option:

                //mauth.signOut();
              //  SendUserLoginActivity();
                finish();
                return true;



            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void SendUseFindFriendsSettingActivity() {
        Intent intent=new Intent(PersonalChatActivity.this,FindFriendActivity.class);
        startActivity(intent);
        finish();
    }


}