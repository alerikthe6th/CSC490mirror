package edu.augustana.csc490.androidbrainapp.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import edu.augustana.csc490.androidbrainapp.FixedTabsPagerAdapter;
import edu.augustana.csc490.androidbrainapp.Fragments.ManualControlsFrag;
import edu.augustana.csc490.androidbrainapp.MyViewPager;
import edu.augustana.csc490.androidbrainapp.R;

/**
 * This activity is used contains a view pager container that allows
 * fragments to be placed within this activity.
 *
 * Created by hamby on 1/12/2017.
 */


public class SelectControlsActivity extends AppCompatActivity {

    //private Button rcBtn;
    MyViewPager view_pager;
    FragmentPagerAdapter adapterViewPager;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_controls_layout);

        //SET THE VIEW PAGER
        view_pager = (MyViewPager) findViewById(R.id.viewPager);
        adapterViewPager = new FixedTabsPagerAdapter(getSupportFragmentManager());
        view_pager.setAdapter(adapterViewPager);



    }

    //this method disables the back button on the bottom bar of the android device
    //nothing happens with the overriden button is pressed.
    @Override
    public void onBackPressed() {
    }

}


