package edu.augustana.csc490.androidbrainapp.Fragments;

/**
 * Created by Alerik Vi on 2/3/2017.
 */

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import edu.augustana.csc490.androidbrainapp.Activities.PopupColorChooserActivity;
import edu.augustana.csc490.androidbrainapp.Color2;
import edu.augustana.csc490.androidbrainapp.Activities.MainActivity;
import edu.augustana.csc490.androidbrainapp.R;

/**
 * This class allows for the robot to implement some sort of image processing.
 *
 * We do this utilizing the open socket with the Camera sensor app phone mounted on the robot
 * The robot will provide a constant feed of bitmap images, for which this class
 * divides up each image into region to detect the most occurrences of a target color, set
 * by the user (choices are red, blue, green).
 */

@SuppressWarnings("deprecation")
public class CameraViewFrag extends Fragment {

    //XML data fields
    private Button btnStart;
    private Button btnStop;
    //private Button btnChooseTarget;
    private ImageView ivCamView;
    private TextView tvDetRegion;

    //private Dialog chooser;

    private boolean stop;
    private int region = -1;
    public static final int ROWS = 5;

    //color constants
    private static final Color2 RED = new Color2(120, 50, 50); //color red
    private static final Color2 GREEN = new Color2(120, 50, 50); //color green
    private static final Color2 BLUE = new Color2(120, 50, 50); //color blue
    private Color2 target = RED; //red is defaulted unless chosen otherwise

    /**
     * This is for the view pager to reference this fragment
     *
     * @return
     */
    public static CameraViewFrag newInstance() {
        CameraViewFrag camView_control = new CameraViewFrag();
        return camView_control;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.camview_layout, container, false);

        //SET THE XML widgets
        tvDetRegion = (TextView) rootView.findViewById(R.id.tvDetRegion);
        ivCamView = (ImageView)rootView.findViewById(R.id.imageView);
        ivCamView.setRotation(90);

//        btnChooseTarget = (Button) rootView.findViewById(R.id.btnChooseTarget);
//        btnChooseTarget.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getContext(), PopupColorChooserActivity.class));
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setTitle("Choose a target color")
//                        .setItems()
//            }
//        });

        btnStart = (Button) rootView.findViewById(R.id.btnStart);
        //btnStart.setEnabled(false);
        //allow the start button to request for pictures from the camera sensor app
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        if(stop){
                            try{
                                MainActivity.mSocketConnectionCamera.requestImg();
                            }catch(IOException e){
                                e.printStackTrace();
                            }
                            stop = false;
                        }
                        while(!stop){
                            try {
                                Log.d("Socket","Requesting Image");
                                final Map map = new Map();
                                map.bm = MainActivity.mSocketConnectionCamera.requestImg();
                                Bitmap temp = map.bm;

                                //process the image
                                processImage(temp);

                                Log.d("ImageView","BitMap byte Count ="+map.bm.getByteCount());

                                Log.d("Image View","Set Image View");
                                getActivity().runOnUiThread(new Runnable() { //todo: check here
                                    @Override
                                    public void run() {

                                        //updates the image view
                                        ivCamView.setImageBitmap(map.bm);

                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

            }
        });
        btnStop = (Button)rootView.findViewById(R.id.btnStop);
        //btnStop.setEnabled(false);

        //allows the stop button close the socket
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Socket","Stop Transfer");
                stop = true;
                try{
                    Log.d("Socket","Called SocketConnectionCamera.stopTranfer");
                    MainActivity.mSocketConnectionCamera.stopTransfer();
                } catch(IOException e ){
                    e.printStackTrace();
                }
                //MainActivity.mSocketConnectionCamera.closeSocket();
            }
    });


        return rootView;
    }

    /**
     * this static class allows us to be redefine a parameter
     * being sent from a thread
     *
     * public data field so the object can have its data changed
     */
    public static class Map{
        protected Bitmap bm;
    }

    /**
     * This is the method that detects our target color from the requested bitmap,
     *
     * this method will divide the our picture into 5 regions and call
     * findColor.
     *
     * @param bmp
     */
    public void processImage(Bitmap bmp)  {

        //accounts for pictures being sent in sideways
        int rowHeight = bmp.getHeight()/ ROWS;
        int maxFreq = -1;
        int index = -1;
        int temp;
        for(int i = 0; i < ROWS; i++){
            temp = findColor(bmp, i*rowHeight, (i+1)*rowHeight);
            if(temp > maxFreq){
                maxFreq = temp;
                index = i;
            }
        }
         Log.d("color found","color found with region: " + region);
         /**
         * we divide the picture image into 5 regions, from 0 to 4, with our center region being region 2.
         *
         * If region 2 (our center region) detects the most instances of our target color, then move forward.
         *
         * regions 0 and 1 will turn the robot right.
         *
         * regions 3 and 4 will turn the robot to the left
         *
         * the robot will default with region 0 if nothing is found, prompting the robot to turn right until a target is found
         */
        region = index;

        try {
            if (region == 0 || region == 1) {
                MainActivity.mSocketConnectionRobot.sendMessage("setright");
            } else if( region == 3 || region == 4) {
                MainActivity.mSocketConnectionRobot.sendMessage("setright");
            } else if(region == 2) { //our center case
                MainActivity.mSocketConnectionRobot.sendMessage("setforward");
            } else {
                MainActivity.mSocketConnectionRobot.sendMessage("setright"); //may never reach here, region cannot be null. but i suppose just in case...
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //update the TextView on the fragment of the detected region
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvDetRegion.setText("region: " + region);
            }
        });
    }

    /**
     * find color takes the pixel at its current iteration and compares it to our target color,
     *
     * allowing for a 15% margin of error. (this allows us to find like colors and nothing specific.
     * a range of colors to be found so to speak.
     *
     * @param image
     * @param startY
     * @param stopY
     * @return
     */
    public int findColor(Bitmap image, int startY, int stopY) {

        // image size
        int width = image.getWidth();
        int pixel, frequency = 0;

        Color2 temp = new Color2();

        //iterates through the given region
        for(int y =  startY; y < stopY; y++) {
            for(int x = 0; x < width; x++) {
                // get pixel color
                pixel = image.getPixel(x, y);
                //update temp color to be color of current pixel
                temp.setRed(Color.red(pixel));
                temp.setGreen(Color.green(pixel));
                temp.setBlue(Color.blue(pixel));
                if (temp.compareColor(target)) {
                    frequency++;
                }
            }
        }
        //returns the amount of occurences of the target color
        return frequency;
    }






}