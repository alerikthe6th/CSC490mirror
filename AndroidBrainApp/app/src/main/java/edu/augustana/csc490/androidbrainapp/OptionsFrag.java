package edu.augustana.csc490.androidbrainapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.support.v4.app.Fragment;

/**
 * Created by Alerik Vi on 1/30/2017.
 */

public class OptionsFrag extends Fragment {

    Button btnDisconnect;

    public static OptionsFrag newInstance() {
        OptionsFrag optionsFrag = new OptionsFrag();


        return optionsFrag;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.options_layout, container, false);

        btnDisconnect = (Button) rootView.findViewById(R.id.btnDisconnect);


        //if this button is pressed, closes the socket
        btnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mSocketConnection.closeSocket();
            }
        });

        return rootView;
    }



}
