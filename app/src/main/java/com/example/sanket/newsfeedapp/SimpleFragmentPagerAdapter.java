package com.example.sanket.newsfeedapp;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by sanket on 09/04/17.
 */

public class SimpleFragmentPagerAdapter extends FragmentStatePagerAdapter {

    Context mContext;
    public SimpleFragmentPagerAdapter(Context context,FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0)
        {
            return new TechnologyFragment();
        }
        else
        {
            return new ScienceFragment();
        }

    }

    @Override

    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        if (position == 0)
        {
            return mContext.getString(R.string.technology);
        }
        else
        {
            return mContext.getString(R.string.science);
        }

    }
}


