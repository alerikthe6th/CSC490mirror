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
 * Created by hamby on 1/29/2017.
 */

public class ManualControlsFrag extends Fragment {
    Button btnForward;
    Button btnBackward;
    Button btnLeft;
    Button btnRight;

    // newInstance constructor for creating fragment with arguments
    public static ManualControlsFrag newInstance() {
        ManualControlsFrag manual_control = new ManualControlsFrag();
        return manual_control;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                .inflate(R.layout.control_layout, container, false);

        btnRight = (Button)rootView.findViewById(R.id.btnRight);// get your root  layout
        btnLeft = (Button)rootView.findViewById(R.id.btnLeft);
        btnForward = (Button)rootView.findViewById(R.id.btnForward);
        btnBackward = (Button)rootView.findViewById(R.id.btnBackward);

        btnForward.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        MainActivity.mSocketConnection.sendMessage("Move");
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "connection unsuccessful", Toast.LENGTH_LONG).show();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    try {
                        MainActivity.mSocketConnection.sendMessage("Stop");
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "an error occured", Toast.LENGTH_LONG).show();
                    }
                }
                return true;
            }
        });

        /*
         * sets a listener for the backwwards button that will move the robot backwards for however long the
         * button is pressed.
         *
         * Stops from the button is depressed
         *
         */
        btnBackward.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        MainActivity.mSocketConnection.sendMessage("back");
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "connection unsuccessful", Toast.LENGTH_LONG).show();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    try {
                        MainActivity.mSocketConnection.sendMessage("Stop");
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "an error occured", Toast.LENGTH_LONG).show();
                    }
                }
                return true;
            }
        });

        /*
         * sets a listener for the left button that will turn the robot left for however long the
         * button is pressed.
         *
         * Stops from the button is depressed
         *
         */
        btnLeft.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        MainActivity.mSocketConnection.sendMessage("left");
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "connection unsuccessful", Toast.LENGTH_LONG).show();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    try {
                        MainActivity.mSocketConnection.sendMessage("Stop");
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "an error occured", Toast.LENGTH_LONG).show();
                    }
                }
                return true;
            }
        });

        /*
         * sets a listener for the right button that will turn the robot right for however long the
         * button is pressed.
         *
         * Stops from the button is depressed
         *
         */
        btnRight.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        MainActivity.mSocketConnection.sendMessage("right");
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "connection unsuccessful", Toast.LENGTH_LONG).show();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    try {
                        MainActivity.mSocketConnection.sendMessage("Stop");
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "an error occured", Toast.LENGTH_LONG).show();
                    }
                }
                return true;
            }
        });

        //display the view
        return rootView;
    }
}
