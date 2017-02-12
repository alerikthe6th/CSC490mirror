package edu.augustana.csc490.androidbrainapp;

/**
 * Created by Alerik Vi on 2/3/2017.
 */

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.StrictMode;
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

@SuppressWarnings("deprecation")
public class CameraViewFrag extends Fragment {

    private Button btnStart;
    private  ImageView ivCamView;
    private boolean stop;
    private Button btnStop;
    MainActivity mainActivity;
    private TextView tvDetRegion;

    private Color2 target = new Color2(150, 50, 50); //color red


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

        tvDetRegion = (TextView) rootView.findViewById(R.id.tvDetRegion);
        ivCamView = (ImageView)rootView.findViewById(R.id.imageView);
        ivCamView.setRotation(90);

        btnStart = (Button) rootView.findViewById(R.id.btnStart);
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
                                Bitmap temp = map.bm;
                                processImage(temp);

                                Log.d("Socket","Image recieve");
                                Log.d("ImageView","BitMap byte Count ="+map.bm.getByteCount());

                                Log.d("Image View","Set Image View");
                                getActivity().runOnUiThread(new Runnable() { //todo: check here
                                    @Override
                                    public void run() {

                                        Log.d("ImageView","Change Image View");


                                        ivCamView.setImageBitmap(map.bm);
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
        btnStop = (Button)rootView.findViewById(R.id.btnStop);
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

        return rootView;
    }
    /**
     * this static class allows us to be redefine a paramter
     * being sent from a thread
     *
     * public data field so the object can have its data changed
     */
    public static class Map{
        protected Bitmap bm;
    }

    public void processImage(Bitmap bmp) {

        SearchRunnable t1 = new SearchRunnable(bmp, target);
        Thread thread = new Thread(t1);
        thread.start();
        while(thread.isAlive()){
            //wait for thread to finish
        }
        //get what region most matches were in
        int region = t1.region;
        tvDetRegion.setText("Detected Region: " + region);
        //TODO: choose what to do based on region

    }



}