package com.test.tworldapplication.adapter;

import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.test.tworldapplication.activity.admin.CommissionFragment;


public class CommissionListAdapter
        extends FragmentPagerAdapter {

    private CommissionFragment currentFragment;;
    private String[] titles;

    public CommissionListAdapter(FragmentManager fm, String...titles) {
        super(fm);
        this.titles = titles;
    }


    @Override
    public Fragment getItem(int position) {
        return CommissionFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return titles.length;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        currentFragment = (CommissionFragment) object;

    }

    public CommissionFragment getCurrentFragment() {
        return currentFragment;
    }
}
