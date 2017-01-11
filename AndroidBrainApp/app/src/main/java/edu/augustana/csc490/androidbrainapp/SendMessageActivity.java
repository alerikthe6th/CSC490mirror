package edu.augustana.csc490.androidbrainapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class SendMessageActivity extends AppCompatActivity {
    private Button ForwardButton;
    private Button BackwardButton;
    private Button LeftButton;
    private Button RightButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_message_activity);

        ForwardButton = (Button) findViewById(R.id.forward_btn);
        BackwardButton = (Button) findViewById(R.id.backward_btn);
        LeftButton = (Button) findViewById(R.id.left_btn);
        RightButton = (Button) findViewById(R.id.right_btn);

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
