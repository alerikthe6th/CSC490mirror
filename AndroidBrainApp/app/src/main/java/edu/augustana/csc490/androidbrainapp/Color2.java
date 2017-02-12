package edu.augustana.csc490.androidbrainapp;


/**
 *
 * this class just keeps track of color values and possibly sets some of the
 * RGB components.
 *
 * Created by Alerik Vi on 2/9/2017.
 */

public class Color2 {

    public static final int ERROR_MARGIN = 15;
    int red;
    int green;
    int blue;

    public Color2(int r, int g, int b) {
        red = r;
        green = g;
        blue = b;
    }

    public Color2(){
        red = 0;
        green = 0;
        blue = 0;
    }

    //getters
    public int getRed() {
        return red;
    }
    public int getGreen() {
        return green;
    }
    public int getBlue() {
        return blue;
    }

    //setters
    public void setRed(int r) {
        red = r;
    }
    public void setGreen(int g) {
        green = g;
    }
    public void setBlue(int b) {
        blue = b;
    }

    public boolean compareColor(Color2 other){
        boolean redCheck = false;
        boolean greenCheck = false;
        boolean blueCheck = false;
        int otherRed = other.getRed();
        int otherGreen = other.getGreen();
        int otherBlue = other.getBlue();

        if( Math.abs(this.getRed() - otherRed) < ERROR_MARGIN){
            redCheck = true;
        } else {
            return false;
        }
        if( Math.abs(this.getGreen() - otherGreen) < ERROR_MARGIN){
            greenCheck = true;
        } else {
            return false;
        }
        if( Math.abs(this.getBlue() - otherBlue) < ERROR_MARGIN){
            blueCheck = true;
        } else {
            return false;
        }
        return (redCheck && greenCheck && blueCheck);
    }
}