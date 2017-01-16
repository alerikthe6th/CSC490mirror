package edu.augustana.csc490.androidbrainapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/*
 * This program sends strings of movement controls to the robot
 *
 */

public class ControlActivity extends AppCompatActivity {
    private Button ForwardButton;
    private Button BackwardButton;
    private Button LeftButton;
    private Button RightButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.control_activity);

        ForwardButton = (Button) findViewById(R.id.forward_btn);
        BackwardButton = (Button) findViewById(R.id.backward_btn);
        LeftButton = (Button) findViewById(R.id.left_btn);
        RightButton = (Button) findViewById(R.id.right_btn);

        /*
         * sets a listener for the forward button that will move the robot forward for however long the
         * button is pressed.
         *
         * Stops from the button is depressed
         *
         */
        ForwardButton.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        MainActivity.mSocketConnection.sendMessage("Move");
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "connection successful", Toast.LENGTH_LONG);
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    try {
                        MainActivity.mSocketConnection.sendMessage("Stop");
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "an error occured", Toast.LENGTH_LONG);
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
        BackwardButton.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        MainActivity.mSocketConnection.sendMessage("back");
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "connection successful", Toast.LENGTH_LONG);
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    try {
                        MainActivity.mSocketConnection.sendMessage("Stop");
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "an error occured", Toast.LENGTH_LONG);
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
        LeftButton.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        MainActivity.mSocketConnection.sendMessage("left");
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "connection successful", Toast.LENGTH_LONG);
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    try {
                        MainActivity.mSocketConnection.sendMessage("Stop");
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "an error occured", Toast.LENGTH_LONG);
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
        RightButton.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        MainActivity.mSocketConnection.sendMessage("right");
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "connection successful", Toast.LENGTH_LONG);
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    try {
                        MainActivity.mSocketConnection.sendMessage("Stop");
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "an error occured", Toast.LENGTH_LONG);
                    }
                }
                return true;
            }
        });
    }
}
