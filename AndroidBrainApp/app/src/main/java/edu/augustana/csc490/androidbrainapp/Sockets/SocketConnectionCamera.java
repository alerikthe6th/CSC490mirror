package edu.augustana.csc490.androidbrainapp.Sockets;

/**
 * This class allows us to connect our main device to a device that contains our camera sensor
 *
 * The constructor creates a socket that allows us to connect to the camera
 *
 * Created by Alerik Vi on 2/11/2017.
 */
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SocketConnectionCamera {

    private Socket socket;
    private OutputStreamWriter os;
    private String filePath;
    private FileOutputStream fos;
    private BufferedInputStream bis;
    private DataInputStream dis;
    private int count;
    private File imageFile;


    public SocketConnectionCamera (int portNum, String ip, String fp) throws Exception{

        //create the socket
        socket = new Socket(ip, portNum);

        //set the streams and file directories
        os = new OutputStreamWriter(socket.getOutputStream());
        filePath = fp;
        bis = new BufferedInputStream(socket.getInputStream());
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "AugustanaRobotImgs");
        count = 0;
        dis = new DataInputStream(bis);
        imageFile = new File(storageDir,"image.png");
    }

    /**
     * sends a request for th eimage with the key string "pic" that returns a bitmap image
     * that from loadIntoMem()
     *
     * @return
     * @throws IOException
     */
    public Bitmap requestImg() throws IOException{
        Log.d("Socket", "Send pic");
        os.write("pic"+"\n");
        os.flush();
        return loadIntoMem();
    }
    public void stopTransfer() throws IOException{
        os.write("stop"+"\n");
        os.flush();
    }
    public void restartTransfer() throws IOException{
        os.write("pic"+"\n");
        os.flush();
    }

    /**
     * a file length from the data input stream is obtained and
     * creates a byte array to store the image to the designated filepath
     *
     * @return
     * @throws IOException
     */
    private Bitmap loadIntoMem() throws IOException{
        int fileLength = dis.readInt();
        byte[] fileByArray = new byte[fileLength];
        dis.readFully(fileByArray);
        fos = new FileOutputStream(imageFile, false);
        fos.write(fileByArray);
        fos.close();

        return BitmapFactory.decodeFile(filePath);
    }

    /**
     * void method that closes the socket
     */
    public void closeSocket(){
        try{
            Log.d("Socket","Socket Closed");
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendClosingMessage() throws IOException{
        os.write("closing"+"\n");
    }


}
