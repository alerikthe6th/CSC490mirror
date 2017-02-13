package edu.augustana.csc490.androidbrainapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;

import edu.augustana.csc490.androidbrainapp.FixedTabsPagerAdapter;
import edu.augustana.csc490.androidbrainapp.R;

/**
 * Created by hamby on 1/12/2017.
 */

public class SelectControlsActivity extends AppCompatActivity {

    //private Button rcBtn;
    ViewPager view_pager;
    FragmentPagerAdapter adapterViewPager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_controls_layout);
        view_pager = (ViewPager)findViewById(R.id.viewPager);
        adapterViewPager = new FixedTabsPagerAdapter(getSupportFragmentManager());
        view_pager.setAdapter(adapterViewPager);



        /*rcBtn = (Button) findViewById(R.id.rc_button);
        rcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rcIntent = new Intent(SelectControlsActivity.this, ControlActivity.class);
                startActivity(rcIntent);
            }
        });*/
    }

    //this method disables the back button on the bottom bar of the android device
    //nothing happens with the overriden button is pressed.
    @Override
    public void onBackPressed() {
    }
}
