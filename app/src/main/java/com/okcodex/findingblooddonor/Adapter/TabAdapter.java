package com.okcodex.findingblooddonor.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.okcodex.findingblooddonor.Fragment.ChatFragment;
import com.okcodex.findingblooddonor.Fragment.ContactFragment;
import com.okcodex.findingblooddonor.Fragment.RequestFragment;

public class TabAdapter extends FragmentPagerAdapter {
    public TabAdapter(@NonNull FragmentManager fm)
    {
        super(fm);


    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ChatFragment chatFragment = new ChatFragment();
                return chatFragment;


            case 1:
                ContactFragment contactsFragment = new ContactFragment();
                return contactsFragment;

            case 2:

                RequestFragment requestFragment = new RequestFragment();
                return requestFragment;

            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 3;
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:

                return "Chats";

            case 1:

                return "Contacts";
            case 2:
                return "Request";

            default:
                return null;

        }

    }
}
