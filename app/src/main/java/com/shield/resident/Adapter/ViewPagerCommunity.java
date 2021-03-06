package com.shield.resident.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.shield.resident.Fragment.FragmentLocalHelp;
import com.shield.resident.Fragment.FragmentNoticeBoard;
import com.shield.resident.Fragment.FragmentResidence;

public class ViewPagerCommunity  extends FragmentPagerAdapter {

    public ViewPagerCommunity(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new FragmentResidence();
        }
        else if (position == 1)
        {
            fragment = new FragmentNoticeBoard();
        }
        else if (position == 2)
        {
            fragment = new FragmentLocalHelp();
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
            title = "Residents";
        }
        else if (position == 1)
        {
            title = "Notice";
        }
        else if (position == 2)
        {
            title = "Local Help";
        }
        return title;
    }
}
