package edu.augustana.csc490.androidbrainapp.Fragments;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.support.v4.app.Fragment;

import java.io.IOException;

import edu.augustana.csc490.androidbrainapp.Activities.MainActivity;
import edu.augustana.csc490.androidbrainapp.R;

/**
 * Created by hamby on 1/29/2017.
 */

public class ManualControlsFrag extends Fragment {
    Button btnForward;
    Button btnBackward;
    Button btnLeft;
    Button btnRight;

    private Button btnStart;
    private ImageView imageViewCam;
    private boolean stop;
    private Button btnStop;

    // newInstance constructor for creating fragment with arguments
    public static ManualControlsFrag newInstance() {
        ManualControlsFrag manual_control = new ManualControlsFrag();
        return manual_control;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     *
     * Inflate the view for the fragment based on layout XML
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.control_layout, container, false);

        btnRight = (Button)rootView.findViewById(R.id.btnRight);// get your root  layout
        btnLeft = (Button)rootView.findViewById(R.id.btnLeft);
        btnForward = (Button)rootView.findViewById(R.id.btnForward);
        btnBackward = (Button)rootView.findViewById(R.id.btnBackward);
        imageViewCam = (ImageView) rootView.findViewById(R.id.imageViewCam);
        imageViewCam.setRotation(90);

        btnForward.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        MainActivity.mSocketConnectionRobot.sendMessage("Move");
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "connection unsuccessful", Toast.LENGTH_LONG).show();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    try {
                        MainActivity.mSocketConnectionRobot.sendMessage("Stop");
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "an error occured", Toast.LENGTH_LONG).show();
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
        btnBackward.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        MainActivity.mSocketConnectionRobot.sendMessage("back");
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "connection unsuccessful", Toast.LENGTH_LONG).show();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    try {
                        MainActivity.mSocketConnectionRobot.sendMessage("Stop");
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "an error occured", Toast.LENGTH_LONG).show();
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
        btnLeft.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        MainActivity.mSocketConnectionRobot.sendMessage("left");
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "connection unsuccessful", Toast.LENGTH_LONG).show();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    try {
                        MainActivity.mSocketConnectionRobot.sendMessage("Stop");
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "an error occured", Toast.LENGTH_LONG).show();
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
        btnRight.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        MainActivity.mSocketConnectionRobot.sendMessage("right");
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "connection unsuccessful", Toast.LENGTH_LONG).show();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    try {
                        MainActivity.mSocketConnectionRobot.sendMessage("Stop");
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "an error occured", Toast.LENGTH_LONG).show();
                    }
                }
                return true;
            }
        });

        btnStart = (Button) rootView.findViewById(R.id.btnStartCam);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        while(!stop){
                            Log.d("START","Running");
                            try {
                                Log.d("Socket","Requesting Image");
                                final Map map = new Map();
                                map.bm = MainActivity.mSocketConnectionCamera.requestImg();

                                Log.d("Socket","Image recieve");
                                Log.d("ImageView","BitMap byte Count ="+map.bm.getByteCount());

                                Log.d("Image View","Set Image View");
                                getActivity().runOnUiThread(new Runnable() { //todo: check here
                                    @Override
                                    public void run() {

                                        Log.d("ImageView","Change Image View");
                                        imageViewCam.setImageBitmap(map.bm);
                                        //ivCamView.invalidate();
                                        Log.d("ImageView", "Current File: "+map.bm.toString());

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
        btnStop = (Button)rootView.findViewById(R.id.btnStopCam);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Socket","Stop Transfer");
                stop = true;
                try{
                    Log.d("Socket","Called SocketConnectionRobot.stopTranfer");
                    MainActivity.mSocketConnectionCamera.stopTransfer();
                } catch(IOException e ){
                    e.printStackTrace();
                }
                MainActivity.mSocketConnectionCamera.closeSocket();
            }
        });


        //display the view
        return rootView;
    }

    private static class Map{
        protected Bitmap bm;
    }

}
