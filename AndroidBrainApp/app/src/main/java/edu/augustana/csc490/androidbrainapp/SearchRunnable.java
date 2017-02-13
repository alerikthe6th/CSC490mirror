package edu.augustana.csc490.androidbrainapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

/**
 * Created by Alerik Vi on 2/11/2017.
 */

public class SearchRunnable implements Runnable {
    public static final int COLS = 5;
    private Bitmap img;
    private Color2 target;
    private boolean isFinished;
    private Activity activity;
    private Context context;

    public int region;

    public SearchRunnable(Bitmap img, Color2 target, Context context){
        this.img = img;
        this.target = target;
       // this.mainActivity = mainActivity;
        isFinished = false;
        this.context = context;
    }
    @Override
    public void run() {
        Log.d("within ST run","within ST run");
        try{

            int colWidth = img.getWidth()/COLS;
            int maxFreq = -1;
            int index = -1;
            int temp;
            for(int i = 0; i < COLS; i++) {
                temp = findColor(img, i*colWidth, (i+1)*colWidth);
                System.out.println("value of temp: " + temp);
                if(temp > maxFreq){
                    maxFreq = temp;
                    index = i;
                }
            }

            final int region = index;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                   // context.afterThreadFinished(region);
                }
            });
            Log.d("color found","color found with region: " + region);
        //   region = index;
            setFinished(true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public int findColor(Bitmap image, int startX, int stopX) {

        // image size
        int height = image.getHeight();
        int pixel, frequency = 0;

        Color2 temp = new Color2();

        // start the scan through the first region
        for (int y = 0; y < height; y++) {
            for (int x = startX; x < stopX; x++) {
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

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean val){
        this.isFinished = val;
    }
}
