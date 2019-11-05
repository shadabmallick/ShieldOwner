package com.sketch.securityowner.Adapter;




import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.sketch.securityowner.Fragment.FragmentLocalHelp;
import com.sketch.securityowner.Fragment.FragmentNoticeBoard;
import com.sketch.securityowner.Fragment.FragmentResidence;

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
            title = "Residence";
        }
        else if (position == 1)
        {
            title = "Notice Board";
        }
        else if (position == 2)
        {
            title = "Local Help";
        }
        return title;
    }
}
