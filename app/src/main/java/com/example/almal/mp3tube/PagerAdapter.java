package com.example.almal.mp3tube;

/**
 * Created by almal on 2017-05-17.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by abdulaziz on 10/27/16.
 */
public class PagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments;

    int pos = 0;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<Fragment>();
    }

    public Fragment getCurrentFragment(){
        return fragments.get(pos);
    }

    public Fragment getLastFragment(){
        return fragments.get(fragments.size());
    }
    public void removeFragment(int fr){
        fragments.remove(fr);
    }
    public void addFragment(Fragment fragment){
        fragments.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        pos = position;
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}