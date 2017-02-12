package edu.augustana.csc490.androidbrainapp;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by Alerik Vi on 2/11/2017.
 */

public class SearchRunnable implements Runnable {
    public static final int COLS = 5;
    private Bitmap img;
    private Color2 target;

    public int region;

    public SearchRunnable(Bitmap img, Color2 target){
        this.img = img;
        this.target = target;
       // this.mainActivity = mainActivity;
    }
    @Override
    public void run() {
        try{
            int colWidth = img.getWidth()/COLS;
            int maxFreq = -1;
            int index = -1;
            int temp;
            for(int i = 0; i < COLS; i++){
                temp = findColor(img, i*colWidth, (i+1)*colWidth);
                if(temp > maxFreq){
                    maxFreq = temp;
                    index = i;
                }
            }
              region = index;
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
}
