package edu.augustana.csc490.androidbrainapp;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity {
    private Button sendButton;
//  public static final int port = 4567;
//  public static final String ip = "192.168.43.154";
    private EditText editTextAddress;
    private EditText editTextPort;

    private int portString;
    private String addressString;

    protected static SocketConnection mSocketConnection = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        sendButton = (Button) findViewById(R.id.button);

        editTextAddress = (EditText) findViewById(R.id.address);
        editTextPort = (EditText) findViewById(R.id.port);

        editTextPort.addTextChangedListener(portTW);
        editTextAddress.addTextChangedListener(addressTW);
    }

    public void sendMessage(View view) throws Exception{
        Log.d("sendMessage", "success");
        mSocketConnection.sendMessage("move");
        Toast.makeText(this, "Message Sent", Toast.LENGTH_LONG);
        Log.d("sendMessage", "message sent");
        Intent sendMessageIntent = new Intent(MainActivity.this, SendMessageActivity.class);
        startActivity(sendMessageIntent);
    }

    public void connectToSocket(View view) throws Exception{
        Log.d("connect to socket", "success");
        Log.d("test","test");
        Log.d("port",""+portString);
        Log.d("IP",""+addressString);
        mSocketConnection = new SocketConnection(portString, addressString);
        Toast.makeText(this, "connection successful", Toast.LENGTH_LONG);
        Log.d("connect to socket", "connection successful");

    }

    private TextWatcher portTW = new TextWatcher() {
        //THE INPUT ELEMENT IS ATTACHED TO AN EDITABLE,
        //THEREFORE THESE METHODS ARE CALLED WHEN THE TEXT IS CHANGED

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try{
                portString = parseInt(s.toString());
            }catch(NumberFormatException e) {

            }
        }
        public void afterTextChanged(Editable s) {
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    };

    private TextWatcher addressTW = new TextWatcher() {
        //THE INPUT ELEMENT IS ATTACHED TO AN EDITABLE,
        //THEREFORE THESE METHODS ARE CALLED WHEN THE TEXT IS CHANGED

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try{
                addressString = s.toString();
            }catch(NumberFormatException e) {

            }
        }
        public void afterTextChanged(Editable s) {
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    };
}
