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

        ForwardButton.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_BUTTON_PRESS){
                    try {
                        MainActivity.mSocketConnection.sendMessage("Move");
                    } catch(Exception e){
                        Toast.makeText(getApplicationContext() , "connection successful", Toast.LENGTH_LONG);
                    }
                } else if(event.getAction() == MotionEvent.ACTION_BUTTON_RELEASE){
                    try {
                        MainActivity.mSocketConnection.sendMessage("Stop");
                    } catch(Exception e){
                        Toast.makeText(getApplicationContext(), "an error occured", Toast.LENGTH_LONG);
                    }
                }
                return true;
            }
        });
    }


    public void forwardMessage(View view) throws Exception{
        MainActivity.mSocketConnection.sendMessage("Move");
    }

    public void backwardMessage(View view) throws Exception{
        MainActivity.mSocketConnection.sendMessage("Back");
    }

    public void leftMessage(View view) throws Exception{
        MainActivity.mSocketConnection.sendMessage("Left");
    }

    public void rightMessage(View view) throws Exception{
        MainActivity.mSocketConnection.sendMessage("Right");
    }

}
