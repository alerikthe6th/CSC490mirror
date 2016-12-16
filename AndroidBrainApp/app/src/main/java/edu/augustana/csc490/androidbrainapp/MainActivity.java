package edu.augustana.csc490.androidbrainapp;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class MainActivity extends AppCompatActivity {
    private Button sendButton;
    public static final int port = 4567;
    public static final String ip = "192.168.43.154";
    private SocketConnection mSocketConnection = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        sendButton = (Button) findViewById(R.id.button);

    }

    public void sendMessage(View view) throws Exception{
        Log.d("sendMessage", "success");
        mSocketConnection.sendMessage();
        Toast.makeText(this, "Message Sent", Toast.LENGTH_LONG);
        Log.d("sendMessage", "message sent");
    }

    public void connectToSocket(View view) throws Exception{
        Log.d("connect to socket", "success");
        mSocketConnection = new SocketConnection(port, ip);
        Toast.makeText(this, "connection successful", Toast.LENGTH_LONG);
        Log.d("connect to socket", "connection successful");
    }
}
