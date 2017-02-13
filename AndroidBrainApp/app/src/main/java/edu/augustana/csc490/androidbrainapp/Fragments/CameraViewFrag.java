package edu.augustana.csc490.androidbrainapp.Fragments;

/**
 * Created by Alerik Vi on 2/3/2017.
 */

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

import edu.augustana.csc490.androidbrainapp.Color2;
import edu.augustana.csc490.androidbrainapp.Activities.MainActivity;
import edu.augustana.csc490.androidbrainapp.R;

@SuppressWarnings("deprecation")
public class CameraViewFrag extends Fragment {

    private Button btnStart;
    private  ImageView ivCamView;
    private boolean stop;
    private Button btnStop;
    MainActivity mainActivity;
    private TextView tvDetRegion;
    private int region = -1;
    public static final int ROWS = 5;

    private Color2 target = new Color2(120, 50, 50); //color red


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
                            try {
                                Log.d("Socket","Requesting Image");
                                final Map map = new Map();
                                map.bm = MainActivity.mSocketConnectionCamera.requestImg();
                                Bitmap temp = map.bm;
                                processImage(temp);

                                Log.d("ImageView","BitMap byte Count ="+map.bm.getByteCount());

                                Log.d("Image View","Set Image View");
                                getActivity().runOnUiThread(new Runnable() { //todo: check here
                                    @Override
                                    public void run() {


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
                    Log.d("Socket","Called SocketConnectionCamera.stopTranfer");
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

    public void processImage(Bitmap bmp)  {
/*

        Log.d("within Proc img","starting search runnable..");

        SearchThread t1 = new SearchThread(bmp, target);
        t1.start();
        Log.d("search thread started","search thread started");
        if(t1.isAlive()){
            try {
                t1.join(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.d("search thread is dead","search thread is ded");
        //get what region most matches were in
        int region = t1.region;
//        tvDetRegion.setText("Detected Region: " + region);
//        //TODO: choose what to do based on region
        System.out.println("region: " + region);


*/
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
        region = index;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvDetRegion.setText("region: " + region);
            }
        });
    }

    public int findColor(Bitmap image, int startY, int stopY) {

        // image size
        int width = image.getWidth();
        int height = image.getHeight();
        int pixel, frequency = 0;

        Color2 temp = new Color2();

//        // start the scan through the first region
//        for (int y = 0; y < height; y++) {
//            for (int x = startX; x < stopX; x++) {
//                // get pixel color
//                pixel = image.getPixel(x, y);
//                //update temp color to be color of current pixel
//                temp.setRed(Color.red(pixel));
//                temp.setGreen(Color.green(pixel));
//                temp.setBlue(Color.blue(pixel));
//                if (temp.compareColor(target)) {
//                    frequency++;
//                }
//            }
//        }

        for (int x = 0; x < width; x++) {
            for (int y = startY; y < stopY; y++) {
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



        return frequency;
    }






}