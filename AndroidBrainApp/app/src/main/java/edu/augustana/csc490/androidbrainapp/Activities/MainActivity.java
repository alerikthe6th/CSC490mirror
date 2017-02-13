package edu.augustana.csc490.androidbrainapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import edu.augustana.csc490.androidbrainapp.R;
import edu.augustana.csc490.androidbrainapp.Sockets.SocketConnectionCamera;
import edu.augustana.csc490.androidbrainapp.Sockets.SocketConnectionRobot;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.valueOf;

public class MainActivity extends AppCompatActivity {

    //IP and port defaults
    public static final String portDefRobot = "4567"; //constant initially set for a default portDefRobot number
    public static final String portDefCam = "5678"; // port constant for camera
    public static final String ipDefRobot = "172.20.10.4"; //constant initially set for a default IP address
    public static final String ipDefCam = "10.100.27.108"; //constant for IP address of the camera
    public static final String ipDefCam2 = "172.20.10.3"; //constant for IP address of the camera

    //Shared preferences tags
    public static final String ROBOT_IP_PREFS = "ip_address_robot";
    public static final String ROBOT_PORT_PREFS = "port_num_robot";
    public static final String CAMERA_IP_PREFS = "ip_address_cam";
    public static final String CAMERA_PORT_PREFS = "port_num_cam";

    //xml widgets
    private EditText etAddressRobot;
    private EditText etPortRobot;
    private String portNumRobot;
    private String addressStringRobot;

    //file path to create/use to retrieve and store pictures to
    private String filePath; //destination of our created directory to overwrite a picture file

    //data fields for the shared prefs
    private EditText etAddressCamera;
    private EditText etPortCamera;
    private String portNumCamera;
    private String addressStringCamera;
    public SharedPreferences prefs;
    public SharedPreferences.Editor editor;

    //our socket connection objects
    public static SocketConnectionRobot mSocketConnectionRobot = null;
    public static SocketConnectionCamera mSocketConnectionCamera = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        editor = prefs.edit();


        //editor.clear(); //this is used to clear the shared preferences

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

        //creates the file directory for our camera sensor
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"AugustanaRobotImgs");
        if(!storageDir.exists()){
            storageDir.mkdir();
        }
        filePath = storageDir.getPath()+"/image.png";

        checkPrefs();
    }

    @Override
    protected void onStop(){

    }

    @Override
    public void onDestroy(){
        try {
            mSocketConnectionCamera.sendClosingMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * set default IP and Port number with Abby's phone as a wifi hotspot
     * @param view
     */
    public void setDefaultDestination(View view) {
        etAddressRobot.setText(ipDefRobot);
        etPortRobot.setText(portDefRobot);

        etAddressCamera.setText(ipDefCam);
        etPortCamera.setText(portDefCam);

    }

    /**
     * Converts the portDefRobot and ipDefRobot address fields from their appropriate edit text fields and creates a socket connection object
     * to connect the android device to the lejos robot
     *
     * @param view
     * @throws Exception
     */
    public void connectToSocket(View view) {
        if (valueOf(portNumRobot) > 0 && addressStringRobot != null) {
            Log.d("values", portNumRobot + ", " + addressStringRobot);
            try {
                mSocketConnectionRobot = new SocketConnectionRobot(valueOf(portNumRobot), addressStringRobot);
                //remove later to a different button
//            Intent intent = new Intent(MainActivity.this, SelectControlsActivity.class);
//            startActivity(intent);

                Toast.makeText(this, " robot connection successful", Toast.LENGTH_LONG).show();
            } catch (Exception e) {

                //if no connect is made, we restart the acitivity
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);

            }
        }
    }
    /**
     * Converts the portDefRobot and ipDefRobot address fields from their appropriate edit text fields and creates a socket connection object
     * to connect the android device to the lejos robot
     * @param view
     * @throws Exception
     */
    public void connectToSocketCamera(View view) {
        if (parseInt(portNumCamera)> 0 && addressStringCamera != null) {

            try {
                mSocketConnectionCamera = new SocketConnectionCamera(parseInt(portNumCamera), addressStringCamera, filePath);
                Toast.makeText(this, "camera connection successful", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MainActivity.this, SelectControlsActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "connection failed, retype the destination fields", Toast.LENGTH_LONG).show();
                //if connection fails, restart the main activity
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);

            }
        }

    }
    // text watcher object for converting the edit text field for the portDefRobot string to an integer
    private TextWatcher portTWRobot = new TextWatcher() {
        //THE INPUT ELEMENT IS ATTACHED TO AN EDITABLE,
        //THEREFORE THESE METHODS ARE CALLED WHEN THE TEXT IS CHANGED
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try{
                portNumRobot = s.toString();
            }catch(NumberFormatException e) {
            }
        }
        public void afterTextChanged(Editable s) {
            //save new robot port num to sharedPrefs
            editor.putString(ROBOT_PORT_PREFS, portNumRobot);
            editor.commit();
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
            }catch(NumberFormatException e) {
            }
        }
        public void afterTextChanged(Editable s) {
            //save new robot ip to sharedPrefs
            editor.putString(ROBOT_IP_PREFS, addressStringRobot);
            editor.commit();
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    };

    // text watcher object for converting the edit text field for the portDefRobot string to an integer
    private TextWatcher portTWCamera = new TextWatcher() {
        //THE INPUT ELEMENT IS ATTACHED TO AN EDITABLE,
        //THEREFORE THESE METHODS ARE CALLED WHEN THE TEXT IS CHANGED
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try{
                portNumCamera = s.toString();
            }catch(NumberFormatException e) {
                e.printStackTrace();
            }
        }
        public void afterTextChanged(Editable s) {
            //save new port num to sharedPrefs
            editor.putString(CAMERA_PORT_PREFS, portNumCamera);
            editor.commit();
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
    };

    /**
     * text watcher object for passing the string in the ipDefRobot address edit text field to a data field of the
     * main activity class
     *
     */
    private TextWatcher addressTWCamera = new TextWatcher() {
        //THE INPUT ELEMENT IS ATTACHED TO AN EDITABLE,
        //THEREFORE THESE METHODS ARE CALLED WHEN THE TEXT IS CHANGED
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try{
                addressStringCamera = s.toString();
            }catch(NumberFormatException e) {
            }
        }
        public void afterTextChanged(Editable s) {
            //save new address in sharedPrefs
            editor.putString(CAMERA_IP_PREFS, addressStringCamera);
            editor.commit();
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
    };

    /**
     * This method checks if a previously entered field has been set
     *  if so, the method will fill the input fields with the previously typed in address
     */
    public void checkPrefs(){
        String temp = null;
        //check robot ip address
        temp = prefs.getString(ROBOT_IP_PREFS, null);
        if(temp != null) {
            etAddressRobot.setText(temp);
        } else
        //check camera device ip address
        temp = prefs.getString(CAMERA_IP_PREFS, null);
        if(temp != null) {
            etAddressCamera.setText(temp);
        }
        //check robot port number
        temp = prefs.getString(ROBOT_PORT_PREFS, null);
        if(temp != null) {
            etPortRobot.setText(temp);
        }
        //check camera device port number
        temp = prefs.getString(CAMERA_PORT_PREFS, null);
        if(temp != null) {
            etPortCamera.setText(temp);
        }
    }
}
