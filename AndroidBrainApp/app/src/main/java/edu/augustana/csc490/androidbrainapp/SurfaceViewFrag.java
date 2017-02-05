package edu.augustana.csc490.androidbrainapp;

/**
 * Created by Alerik Vi on 2/3/2017.
 */

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.Locale;

@SuppressWarnings("deprecation")
public class SurfaceViewFrag extends Fragment implements SurfaceHolder.Callback {

    private Camera camera;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Button start;
    private Button stop;
    private Button take;

    //currently not being used
    private Camera.PictureCallback rawCallback;
    private Camera.ShutterCallback shutterCallback;
    private Camera.PictureCallback pngCallback;

    private final String tag = "SurfaceViewFragment";


    public static SurfaceViewFrag newInstance() {
        SurfaceViewFrag surfView_control = new SurfaceViewFrag();
        return surfView_control;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.surfview_layout, container, false);

        start = (Button) rootView.findViewById(R.id.btnStart);
        stop = (Button) rootView.findViewById(R.id.btnStop);
        take = (Button) rootView.findViewById(R.id.btnTake);
        take.setVisibility(View.INVISIBLE);

        start.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View arg){
        startCamera();
        }
});
        stop.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                stopCamera();
            }
        });
        take.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                // captureImage();
            }
        });

        surfaceView = (SurfaceView) rootView.findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        rawCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Log.d("Log","onPictureTaken - raw");
            }
        };
        shutterCallback = new Camera.ShutterCallback() {
            @Override
            public void onShutter() {
                Log.i("Log","onshutter'd");
            }
        };


        /* pngCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream outputStream = null;
                try{
                    outputStream = new FileOutputStream(String.format("currentPhoto.png",System.currentTimeMillis()));
                    outputStream.write(data);
                    outputStream.close();
                    Log.d("Log", "onPictureTaken - wrote bytes: "+data.length);
                }
            }
        }*/

        return rootView;
    }

    private void startCamera(){
        if(Camera.getNumberOfCameras()>0){
            if(getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)){
                camera = Camera.open(0);
            }else{
                camera = Camera.open();
            }

        }else{
            return;
        }

        Camera.Parameters param;
        param = camera.getParameters();

        param.setPreviewFrameRate(20);
        param.setPreviewSize(surfaceView.getWidth(),surfaceView.getHeight());
        camera.setParameters(param);

        try{
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e){
            Log.e(tag, "init_camera: "+e);
            return;
        }
    }

    private void stopCamera(){
        camera.stopPreview();
        camera.release();
        surfaceView.clearFocus();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    public void surfaceChanged(SurfaceHolder holder, int arg1, int arg2, int arg3){

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

}