package edu.augustana.csc490.androidbrainapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hamby on 1/29/2017.
 */

public class FixedTabsPagerAdapter extends FragmentPagerAdapter{

    public FixedTabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public int getCount(){
        return 2;
    }

    public Fragment getItem(int itemPos){
        switch(itemPos){
            case 0: return new ManualControlsFrag();
            case 1: return new VoiceControlFrag();
        }
        return null;
    }

    public CharSequence getPageTitle(int position){
        switch(position){
            case 0: return "Manual Control";
            case 1: return "Voice Control";
        }
        return null;
    }



}
