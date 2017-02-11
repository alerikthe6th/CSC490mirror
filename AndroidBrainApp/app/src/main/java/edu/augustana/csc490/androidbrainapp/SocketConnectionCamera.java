package edu.augustana.csc490.androidbrainapp;

/**
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


    public SocketConnectionCamera (int portNum, String ip,String fp) throws Exception{
        Log.d("SocketConnectionRobot", "Here");
        socket = new Socket(ip, portNum);
        Log.d("SocketConnectionRobot","Socket Created");
        os = new OutputStreamWriter(socket.getOutputStream());
        filePath = fp;
        bis = new BufferedInputStream(socket.getInputStream());
        dis = new DataInputStream(bis);
        count = 0;
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "AugustanaRobotImgs");
        imageFile = new File(storageDir,"image.png");
        Log.d("imageFile status:","imageFile: " + imageFile.toString());
    }
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

    private Bitmap loadIntoMem() throws IOException{
        Log.d("Socket", "In LOADINTOMEM");
        int fileLength = dis.readInt();
        Log.d("Socket","File Length = "+fileLength);
        byte[] fileByArray = new byte[fileLength];
        dis.readFully(fileByArray);
        Log.d("Socket","Read File IN!!!!!!");
        Log.d("Socket","Start to write to file");
        fos = new FileOutputStream(imageFile, false);
        fos.write(fileByArray);
        fos.close();
        Log.d("Socket","File writen Complete");
        Log.d("File","Image File Size = "+imageFile);

        return BitmapFactory.decodeFile(filePath);
    }
    public void closeSocket(){
        try{
            Log.d("Socket","Socket Closed");
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
