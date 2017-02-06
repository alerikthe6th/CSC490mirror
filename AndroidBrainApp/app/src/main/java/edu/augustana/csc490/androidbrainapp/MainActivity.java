package edu.augustana.csc490.androidbrainapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity {

    // data field and buttons
    public static final String portDefRobot = "4567"; //constant initially set for a default portDefRobot number
    public static final String portDefCam = "5678"; // port constant for camera
    public static final String ipDefRobot = "172.20.10.2"; //constant initially set for a default IP address
    public static final String ipDefCam = "255.255.255.255"; //constant for IP address of the camera

    private EditText etAddressRobot;
    private EditText etPortRobot;
    private int portNumRobot;
    private String addressStringRobot;

    private EditText etAddressCamera;
    private EditText etPortCamera;
    private int portNumCamera;
    private String addressStringCamera;

    public SharedPreferences prefs;
    public SharedPreferences.Editor editor;

    protected static SocketConnection mSocketConnectionRobot = null;
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
        etAddressRobot = (EditText) findViewById(R.id.etAddressRobot);
        etPortRobot = (EditText) findViewById(R.id.etPortRobot);

        //link the buttons to the activity layout for the camera
        etAddressCamera = (EditText) findViewById(R.id.etAddressCamera);
        etPortCamera = (EditText) findViewById(R.id.etPortCamera);

        //add text watchers to the edit buttons
        etPortRobot.addTextChangedListener(portTWRobot);
        etAddressRobot.addTextChangedListener(addressTWRobot);

        //add text watchers to the edit buttons for the camera
        etPortCamera.addTextChangedListener(portTWCamera);
        etAddressCamera.addTextChangedListener(addressTWCamera);



        //sets the edit text fields if there is a "default" usage in the past
//        if(prefs.getString("ip_address_robot", null) != null) {
//            etAddressRobot.setText(prefs.getString("ip_address_robot", null));
//        }
//        if(prefs.getString("ip_address_camera", null) != null) {
//            etAddressCamera.setText(prefs.getString("ip_address_camera", null));
//        }
//        if(prefs.getInt("port_num_robot", -1) != -1) {
//            etPortRobot.setText("" + prefs.getInt("port_num_robot", -1));
//        }
//        if(prefs.getInt("port_num_camera", -1) != -1) {
//            etPortCamera.setText("" + prefs.getInt("port_num_camera", -1));
//        }
    }

    /**
     * set default IP and Port number with Abby's phone as a wifi hotspot
     * TODO: SET BACK TO THIS DEFAULT
     * @param view
     */
    public void setDefaultDestination(View view) {

        //robot connection defaults
        //etAddressRobot.setText(prefs.getString("ip_address_robot", "Not Found"));
        //etPortRobot.setText(prefs.getString("port_num_robot", "1234"));

        etAddressRobot.setText(ipDefRobot);
        etPortRobot.setText(portDefRobot);

        //camera connection defaults
       // etAddressCamera.setText(prefs.getString("ip_address_camera", "Not Found"));
       // etPortCamera.setText(prefs.getString("port_num_camera", "1234"));
    }

    /**
     * Converts the portDefRobot and ipDefRobot address fields from their appropriate edit text fields and creates a socket connection object
     * to connect the android device to the lejos robot
     *
     * @param view
     * @throws Exception
     */
    public void connectToSocket(View view) throws Exception{

        if(portNumRobot > 0 && addressStringRobot != null) {
            Log.d("values", "" + portNumRobot + ", " + addressStringRobot);
            mSocketConnectionRobot = new SocketConnection(portNumRobot, addressStringRobot);

            //remove later to a different button
            Intent intent = new Intent(MainActivity.this, SelectControlsActivity.class);
            startActivity(intent);
            Toast.makeText(this, "connection successful", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "connection failed, retype the destination fields", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Converts the portDefRobot and ipDefRobot address fields from their appropriate edit text fields and creates a socket connection object
     * to connect the android device to the lejos robot
     * TODO: CURRENTLY NOT BEING USED BY  BUTTON
     * @param view
     * @throws Exception
     */
    public void connectToSocketCamera(View view) throws Exception{

        if(portNumCamera > 0 && addressStringCamera != null) {
            mSocketConnectionCamera = new SocketConnection(portNumCamera, addressStringCamera);
            Toast.makeText(this, "connection successful", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "connection failed, retype the destination fields", Toast.LENGTH_LONG).show();
        }
    }

    //TODO: REMOVE AFTER TESTING
    /*
    public void launchNewShit(View view){
        Intent intent = new Intent(MainActivity.this, SelectControlsActivity.class);
        startActivity(intent);
    }
    */

    // text watcher object for converting the edit text field for the portDefRobot string to an integer
    private TextWatcher portTWRobot = new TextWatcher() {
        //THE INPUT ELEMENT IS ATTACHED TO AN EDITABLE,
        //THEREFORE THESE METHODS ARE CALLED WHEN THE TEXT IS CHANGED

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try{
                portNumRobot = parseInt(s.toString());
                editor.putString("port_num_robot", "" + portNumRobot);
                editor.commit();
            }catch(NumberFormatException e) {

            }
        }
        public void afterTextChanged(Editable s) {
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    };

    // text watcher object for passing the string in the ipDefRobot address edit text field to a data field of the
    // main activity class
    private TextWatcher addressTWRobot = new TextWatcher() {
        //THE INPUT ELEMENT IS ATTACHED TO AN EDITABLE,
        //THEREFORE THESE METHODS ARE CALLED WHEN THE TEXT IS CHANGED

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try{
                addressStringRobot = s.toString();
                editor.putString("ip_address_robot", addressStringRobot);
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

    // text watcher object for converting the edit text field for the portDefRobot string to an integer
    private TextWatcher portTWCamera = new TextWatcher() {
        //THE INPUT ELEMENT IS ATTACHED TO AN EDITABLE,
        //THEREFORE THESE METHODS ARE CALLED WHEN THE TEXT IS CHANGED

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try{
                portNumCamera = parseInt(s.toString());
                editor.putString("port_num_camera", "" + portNumCamera);
                editor.commit();
            }catch(NumberFormatException e) {
                e.printStackTrace();
            }
        }
        public void afterTextChanged(Editable s) {
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    };

    // text watcher object for passing the string in the ipDefRobot address edit text field to a data field of the
    // main activity class
    private TextWatcher addressTWCamera = new TextWatcher() {
        //THE INPUT ELEMENT IS ATTACHED TO AN EDITABLE,
        //THEREFORE THESE METHODS ARE CALLED WHEN THE TEXT IS CHANGED

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try{
                addressStringCamera = s.toString();
                editor.putString("ip_address_camera", addressStringCamera);
                editor.commit();
            }catch(NumberFormatException e) {

            }
        }
        public void afterTextChanged(Editable s) {
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    };

}
