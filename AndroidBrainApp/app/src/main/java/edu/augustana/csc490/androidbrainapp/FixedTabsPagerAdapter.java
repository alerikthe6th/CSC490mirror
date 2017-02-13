package edu.augustana.csc490.androidbrainapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import edu.augustana.csc490.androidbrainapp.Fragments.AboutFrag;
import edu.augustana.csc490.androidbrainapp.Fragments.CameraViewFrag;
import edu.augustana.csc490.androidbrainapp.Fragments.ManualControlsFrag;
import edu.augustana.csc490.androidbrainapp.Fragments.OptionsFrag;
import edu.augustana.csc490.androidbrainapp.Fragments.VoiceControlFrag;

/**
 * This adapter class organizes all the fragments in into a view pager object that will be displayed
 * in the select controls activity
 *
 * Created by hamby on 1/29/2017.
 */

public class FixedTabsPagerAdapter extends FragmentPagerAdapter{

    public FixedTabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public int getCount(){
            return 5;
    } //increment every time a fragment is added and decrement every time project

    public Fragment getItem(int itemPos){
        switch(itemPos){
            case 0: return new ManualControlsFrag();
            case 1: return new VoiceControlFrag();
            case 2: return new CameraViewFrag();
            case 3: return new OptionsFrag();
            case 4: return new AboutFrag();
        }
        return null;
    }

    /**
     * sets the fragment title in the view pager
     *
     * @param position
     * @return
     */
    public CharSequence getPageTitle(int position){
        switch(position){
            case 0: return "Manual Control";
            case 1: return "Voice Control";
            case 2: return "Camera View Control";
            case 3: return "Options";
            case 4: return "About";
        }
        return null;
    }



}
