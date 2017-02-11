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

import java.io.IOException;

@SuppressWarnings("deprecation")
public class CameraViewFrag extends Fragment {

    private Button btnStart;
    private  ImageView ivCamView;
    private boolean stop;
    private Button btnStop;

    private final String tag = "SurfaceViewFragment";


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

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

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
    private static class Map{
        public Bitmap bm;
    }


}