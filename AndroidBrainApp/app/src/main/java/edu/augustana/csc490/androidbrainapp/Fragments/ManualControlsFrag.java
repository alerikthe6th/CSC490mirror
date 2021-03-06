package edu.augustana.csc490.androidbrainapp.Fragments;

import android.graphics.Bitmap;
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
 *
 *
 * Created by hamby on 1/29/2017.
 */

public class ManualControlsFrag extends Fragment {
    private Button btnForward;
    private Button btnBackward;
    private Button btnLeft;
    private Button btnRight;
    private Button btnStart;
    private ImageView imageViewCam;
    protected boolean paused;
    private Button btnStop;
    private boolean threadWasStarted = false;




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
        final ImageLoadingThread imgThread = new ImageLoadingThread();

         /*
         * sets a listener for the forward button that will move the robot forwards for however long the
         * button is pressed.
         *
         * Stops from the button is depressed
         *
         */
        btnForward.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        MainActivity.mSocketConnectionRobot.sendMessage("forward");
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
                if (paused) {
                    paused = false;
                } else if (!threadWasStarted) {
                    imgThread.start();
                    threadWasStarted = true;
                }
            }
        });
        btnStop = (Button)rootView.findViewById(R.id.btnStopCam);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("STOP","In the Stop onClick");
//                imgThread.interrupt();
                paused = true;
                imgThread.interrupt();
               // MainActivity.mSocketConnectionCamera.closeSocket();
            }
        });


        //display the view
        return rootView;
    }

    /**
     * This thread is used to request an image from the main acitivity's socket connection object for
     * the robot
     *
     */
    private class ImageLoadingThread extends Thread{
        public ImageLoadingThread(){

        }

        @Override
        public void run(){
            try {
                while(true){


                        final Bitmap bm = MainActivity.mSocketConnectionCamera.requestImg(); //requests an image from the socket connection

                        getActivity().runOnUiThread(new Runnable() { //todo: check here
                            @Override
                            public void run() {
                                //sets the image in the image view
                                imageViewCam.setImageBitmap(bm);
                            }
                        });
                        //allows the app to finish sending an image before we stop the transfer
                        if (paused) {
                            MainActivity.mSocketConnectionCamera.stopTransfer();
                        }
                        while(paused) {
                            try {
                                //waiting
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                            }
                        }
                    }
                }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
