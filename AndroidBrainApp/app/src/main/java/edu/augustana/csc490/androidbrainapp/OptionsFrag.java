package edu.augustana.csc490.androidbrainapp;

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
import android.support.v4.app.Fragment;

/**
 * Optoins fragment allowing the user to disconnect a robot/camera
 * and to connect a new robot/camera
 *
 * Created by Alerik Vi on 1/30/2017.
 */

public class OptionsFrag extends Fragment {

    private Button btnDisconnectRobot;
    private Button btnDisconnectCamera;
    private Context context;

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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.options_layout, container, false);

        context = getContext();

        btnDisconnectRobot = (Button) rootView.findViewById(R.id.btnDisconnectRobot);
        btnDisconnectCamera = (Button) rootView.findViewById(R.id.btnDisconnectCamera);


        //if this button is pressed, closes the socket
        //TODO: launch to first activity after disconnecting
        btnDisconnectRobot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Disconnecting Robot")
                        .setMessage("Are you sure you want to disconnect the robot?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context, "Disconnecting Robot", Toast.LENGTH_SHORT);
                                MainActivity.mSocketConnectionRobot.closeSocket();
                                Intent intent = new Intent(context, MainActivity.class); //hopefully sends back to connection screen?
                                startActivity(intent);
                                //finish();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        //if this button is pressed, closes the socket
        btnDisconnectCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Disconnecting Robot")
                        .setMessage("Are you sure you want to disconnect the robot?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context, "Disconnecting Camera", Toast.LENGTH_SHORT);
                                MainActivity.mSocketConnectionCamera.closeSocket();
                                Intent intent = new Intent(context, MainActivity.class); //hopefully sends back to connection screen?
                                startActivity(intent);
                                //finish();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        return rootView;
    }



}
