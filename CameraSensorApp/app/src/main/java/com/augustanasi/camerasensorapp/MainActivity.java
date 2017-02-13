package com.augustanasi.camerasensorapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@SuppressWarnings("deprecation")

/**
 *This class starts the devices camera and when prompted by the Control App, takes a picture and sends it back to the Control App.
 * Requires the device has a back-facing camera.
 */
public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    Camera.PictureCallback rawCallBack;
    Camera.ShutterCallback shutterCallback;
    Camera.PictureCallback pngCallBack;
    Context context;
    Button takeImg;
    Button startBtn;
    Button socketListen;
    static File storageDir;
    SocketConnection socketConnection;

    //Port Id number for connection with control app
    int port = 5678;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        /**
         * Strict mode is a developer tool which detects things one might be doing by accident and brings them to
         * the developer's attention so they can be fixed i.e. catching accidental disk errors or network access
         * on the app's main thread
         */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Checks how many cameras the device has
        if(camera.getNumberOfCameras()<2) {
            //if the only camera a device has then displays alert and closes app
            if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
                new AlertDialog.Builder(this)
                          .setTitle("Required Camera")
                        .setMessage("This app requires the device to have a back facing camera")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .show();
            }
        }
        socketListen = (Button)findViewById(R.id.listen);

        /**
         * Sets Listener for socketListen button.
         * Starts camera then reads in prompts from the control app, when received calls take picture and sends image back through socket
         */
        socketListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Uses AsyncTask to allow method to happen on background thread
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        boolean cameraOpen = true;
                        startCamera();
                        try{
                            //Creates SocketConnection object that starts connection, throws IOException
                            socketConnection = new SocketConnection(port);
                            }catch (Exception e){
                            e.printStackTrace();
                        }
                        try{
                            int currentInput = socketConnection.waitForPrompt();
                            boolean stopped = false;
                            if(currentInput==-5){
                                //Closes App
                                socketConnection.closeSocket();
                                MainActivity.super.recreate();
                            }
                             while(currentInput!=-1){
                                 if(currentInput==0){
                                    cameraOpen = false;
                                    camera.stopPreview();
                                    stopped = true;
                                }else{
                                    if(stopped){
                                        cameraOpen = true;
                                        camera.startPreview();
                                    }
                                    captureImage();
                                }
                                currentInput = socketConnection.waitForPrompt();
                            }
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        if(cameraOpen){
                            stopCamera();
                        }
                        socketConnection.closeSocket();
                    }
                });

            }
        });

        //Retreives the location in storage
        storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "AugustanaRobotImgs");
        //Checks if directory exists
        if(!storageDir.exists()){
            //Creates directory if it doesn't already exist
            storageDir.mkdir();
        }




        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        //Sets RawCallBack for camera
        rawCallBack = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                Log.d("Log","onPictureTaken - raw");
            }
        };

        //Sets ShutterCallBack for camera
        shutterCallback = new Camera.ShutterCallback() {
            public void onShutter() {
                Log.i("Log","onshutter'd");
            }
        };

        /**
         *
         */
        pngCallBack = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream outputStream;
                //Checks the state of the devices External Storage
                String state = Environment.getExternalStorageState();
                //File location where new image will be placed
                File image = new File(storageDir, "image.png");

                try{
                    outputStream = new FileOutputStream(image);
                    outputStream.write(data);
                    outputStream.close();
                } catch(FileNotFoundException e){
                    e.printStackTrace();
                } catch(IOException e){
                    e.printStackTrace();
                } finally {
                }
                camera.stopPreview();
                camera.startPreview();
                try{
                    socketConnection.sendImage(image);
                }catch(IOException e){
                    e.printStackTrace();
                }


            }
        };
    }


    public void captureImage(){
        camera.takePicture(shutterCallback,rawCallBack,pngCallBack);
    }


    private void startCamera(){
        if(Camera.getNumberOfCameras()>0){
            camera = Camera.open();
        }else{
            return;
        }

        Camera.Parameters param;
        param = camera.getParameters();

        param.setPreviewFrameRate(40);
        param.setPreviewSize(surfaceView.getWidth(),surfaceView.getHeight());
        camera.setParameters(param);
        camera.setDisplayOrientation(90);

        try{
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        }catch (Exception e){
            Log.e("MainActivity","init_camera: "+e);
        }
        Log.d("CAMERA","Started");
    }

    public void stopCamera(){
        camera.stopPreview();
        camera.release();
    }

    public void endApp(){
        finish();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}

