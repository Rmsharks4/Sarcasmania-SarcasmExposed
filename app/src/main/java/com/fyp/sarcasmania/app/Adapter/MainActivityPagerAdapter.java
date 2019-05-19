package com.fyp.sarcasmania.app.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fyp.sarcasmania.app.Fragment.NewfeedFragment;
import com.fyp.sarcasmania.app.Fragment.PostFragment;
import com.fyp.sarcasmania.app.Fragment.ProfileFragment;
import com.fyp.sarcasmania.app.Fragment.reportedtweets;


/**
 * Created by rmsha on 9/29/2017.
 */

public class MainActivityPagerAdapter extends FragmentPagerAdapter {

    private int nooftabs;
    private Fragment home = new NewfeedFragment();
    private Fragment upload = new PostFragment();
    private Fragment profile = new ProfileFragment();
    private Fragment reportedtweets = new reportedtweets();

    public MainActivityPagerAdapter(FragmentManager fm, int NumberOfTabs) {
        super(fm);
        nooftabs = NumberOfTabs;
    }

    @Override
    public  Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return home;
            case 1:
                return upload;
            case 2:
                return profile;
            case 3:
                return reportedtweets;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return nooftabs;
    }
}

