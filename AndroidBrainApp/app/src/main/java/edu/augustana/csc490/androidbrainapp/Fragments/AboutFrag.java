package edu.augustana.csc490.androidbrainapp.Fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import edu.augustana.csc490.androidbrainapp.Activities.MainActivity;
import edu.augustana.csc490.androidbrainapp.R;

/**
 * Created by Alerik Vi on 2/13/2017.
 */

public class AboutFrag extends Fragment{

    private Button btnDisconnectRobot;
    //private Button btnDisconnectCamera;
    private Context context;

    public static AboutFrag gnewInstance() {
        AboutFrag aboutFrag = new AboutFrag();
        return aboutFrag;
    }

    /**
     *
     * Inflate the view for the fragment based on layout XML
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.options_layout, container, false);




        return rootView;
    }
}
