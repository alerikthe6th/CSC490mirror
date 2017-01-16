package edu.augustana.csc490.androidbrainapp;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity {

    // data field and buttons
    public static final String port = "4567"; //initially set for a default port number
    public static final String ip = "172.20.10.2"; //initially set for a default IP address
    private EditText editTextAddress;
    private EditText editTextPort;
    private int portString;
    private String addressString;

    protected static SocketConnection mSocketConnection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Strict mode is a developer tool which detects things one might be doing by accident and brings them to
         * the developer's attention so they can be fixed i.e. catching accidental disk errors or network access
         * on the app's main thread
         */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //link the buttons to the activity layout
        editTextAddress = (EditText) findViewById(R.id.address);
        editTextPort = (EditText) findViewById(R.id.port);

        //add text watchers to the edit buttons
        editTextPort.addTextChangedListener(portTW);
        editTextAddress.addTextChangedListener(addressTW);
    }

    /**
     * set default IP and Port number with Abby's phone as a wifi hotspot
     * 
     * @param view
     */
    public void setDefaultDestination(View view) {

        editTextAddress.setText(ip);
        editTextPort.setText(port);

    }

    /**
     * This method launched the control activity xml when the Launch Manual Control button has
     * been pressed
     *
     * @param view
     * @throws Exception
     */
    public void launchControlsActivity(View view) throws Exception{

        //STARTS THE MOVEMENT ACTIVITY LAYOUT
        Intent sendMessageIntent = new Intent(MainActivity.this, ControlActivity.class);
        startActivity(sendMessageIntent);
    }

    /**
     * This method launched the voice activity xml when the Voice control button has
     * been pressed
     *
     * @param view
     * @throws Exception
     */
    public void launchVoiceActivity(View view) throws Exception{

        //STARTS THE VOICE MOVEMENT LAYOUT
        Intent sendMessageIntent = new Intent(MainActivity.this, VoiceActivity.class);
        startActivity(sendMessageIntent);
    }

    /**
     * Converts the port and ip address fields from their appropriate edit text fields and creates a socket connection object
     * to connect the android device to the lejos robot
     *
     * @param view
     * @throws Exception
     */
    public void connectToSocket(View view) throws Exception{

        if(portString > 0 && addressString != null) {
            mSocketConnection = new SocketConnection(portString, addressString);
            Toast.makeText(this, "connection successful", Toast.LENGTH_LONG);
        } else {
            Toast.makeText(this, "connection failed, retype the destination fields", Toast.LENGTH_LONG);
        }
    }

    // text watcher object for converting the edit text field for the port string to an integer
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

    // text watcher object for passing the string in the ip address edit text field to a data field of the
    // main activity class
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
