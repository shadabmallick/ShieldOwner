package com.shield.resident.Adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.shield.resident.Fragment.FragEmergency;
import com.shield.resident.Fragment.FragmentHelp;
import com.shield.resident.Fragment.FragSecurity;


public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new FragmentHelp();
        }
        else if (position == 1)
        {
            fragment = new FragSecurity();
        }
        else if (position == 2)
        {
            fragment = new FragEmergency();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "Help";
        }
        else if (position == 1)
        {
            title = "Guard";
        }
        else if (position == 2)
        {
           // title = "Alert";
            title = "EMERG";
        }
        return title;
    }
}
