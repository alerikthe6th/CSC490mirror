package edu.augustana.csc490.androidbrainapp;

import android.content.Intent;
import android.content.SharedPreferences;
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
    public static final String portDef = "4567"; //constant initially set for a default portDef number
    public static final String ipDef = "172.20.10.2"; //constant initially set for a default IP address
    private EditText etAddress;
    private EditText etPort;
    private int portNum;
    private String addressString;

    private EditText etAddress2;
    private EditText etPort2;
    private int portNum2;
    private String addressString2;

    public SharedPreferences prefs;
    public SharedPreferences.Editor editor;

    protected static SocketConnection mSocketConnection = null;
    protected static SocketConnection mSocketConnectionCamera = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        editor = prefs.edit();

        /**
         * Strict mode is a developer tool which detects things one might be doing by accident and brings them to
         * the developer's attention so they can be fixed i.e. catching accidental disk errors or network access
         * on the app's main thread
         */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //link the buttons to the activity layout
        etAddress = (EditText) findViewById(R.id.etAddress);
        etPort = (EditText) findViewById(R.id.etPort);

        //link the buttons to the activity layout for the camera
        etAddress2 = (EditText) findViewById(R.id.etAddress2);
        etPort2 = (EditText) findViewById(R.id.etPort2);

        //add text watchers to the edit buttons
        etPort.addTextChangedListener(portTW);
        etAddress.addTextChangedListener(addressTW);

        //add text watchers to the edit buttons for the camera
        etPort2.addTextChangedListener(portTWCamera);
        etAddress2.addTextChangedListener(addressTWCamera);
    }

    /**
     * set default IP and Port number with Abby's phone as a wifi hotspot
     * TODO: SET BACK TO THIS DEFAULT
     * @param view
     */
    public void setDefaultDestination(View view) {
        etAddress.setText(prefs.getString("ip_address", "Not Found"));
        etPort.setText(prefs.getString("port_num", "1234"));
    }

    /**
     * Converts the portDef and ipDef address fields from their appropriate edit text fields and creates a socket connection object
     * to connect the android device to the lejos robot
     *
     * @param view
     * @throws Exception
     */
    public void connectToSocket(View view) throws Exception{

        if(portNum > 0 && addressString != null) {
            mSocketConnection = new SocketConnection(portNum, addressString);
            Toast.makeText(this, "connection successful", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "connection failed, retype the destination fields", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Converts the portDef and ipDef address fields from their appropriate edit text fields and creates a socket connection object
     * to connect the android device to the lejos robot
     * TODO: CURRENTLY NOT BEING USED BY  BUTTON
     * @param view
     * @throws Exception
     */
    public void connectToSocketCamera(View view) throws Exception{

        if(portNum2 > 0 && addressString2 != null) {
            mSocketConnectionCamera = new SocketConnection(portNum2, addressString2);
            Toast.makeText(this, "connection successful", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "connection failed, retype the destination fields", Toast.LENGTH_LONG).show();
        }
    }

    //TODO: REMOVE AFTER TESTING
    public void launchNewShit(View view){
        Intent intent = new Intent(MainActivity.this, SelectControlsActivity.class);
        startActivity(intent);
    }

    // text watcher object for converting the edit text field for the portDef string to an integer
    private TextWatcher portTW = new TextWatcher() {
        //THE INPUT ELEMENT IS ATTACHED TO AN EDITABLE,
        //THEREFORE THESE METHODS ARE CALLED WHEN THE TEXT IS CHANGED

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try{
                portNum = parseInt(s.toString());
                editor.putString("port_num", "" + portNum);
                editor.commit();
            }catch(NumberFormatException e) {

            }
        }
        public void afterTextChanged(Editable s) {
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    };

    // text watcher object for passing the string in the ipDef address edit text field to a data field of the
    // main activity class
    private TextWatcher addressTW = new TextWatcher() {
        //THE INPUT ELEMENT IS ATTACHED TO AN EDITABLE,
        //THEREFORE THESE METHODS ARE CALLED WHEN THE TEXT IS CHANGED

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try{
                addressString = s.toString();
                editor.putString("ip_address", addressString);
                editor.commit();
            }catch(NumberFormatException e) {

            }
        }
        public void afterTextChanged(Editable s) {
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    };

    /*
    TODO: FIX THESE WATCHERS BELOW

     */

    // text watcher object for converting the edit text field for the portDef string to an integer
    private TextWatcher portTWCamera = new TextWatcher() {
        //THE INPUT ELEMENT IS ATTACHED TO AN EDITABLE,
        //THEREFORE THESE METHODS ARE CALLED WHEN THE TEXT IS CHANGED

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try{
                portNum2 = parseInt(s.toString());
                editor.putString("port_num", "" + portNum2);
                editor.commit();
            }catch(NumberFormatException e) {

            }
        }
        public void afterTextChanged(Editable s) {
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    };

    // text watcher object for passing the string in the ipDef address edit text field to a data field of the
    // main activity class
    private TextWatcher addressTWCamera = new TextWatcher() {
        //THE INPUT ELEMENT IS ATTACHED TO AN EDITABLE,
        //THEREFORE THESE METHODS ARE CALLED WHEN THE TEXT IS CHANGED

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try{
                addressString2 = s.toString();
                editor.putString("ip_address", addressString2);
                editor.commit();
            }catch(NumberFormatException e) {

            }
        }
        public void afterTextChanged(Editable s) {
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    };

}
