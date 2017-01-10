package edu.augustana.csc490.androidbrainapp;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.Socket;
import java.io.OutputStreamWriter;


/**
 * Created by hamby on 12/15/2016.
 */

public class SocketConnection {
    private Socket socket;
    private OutputStreamWriter os;

    public SocketConnection(int portNum, String ip) throws Exception{
        socket = new Socket(ip, portNum);
        os = new OutputStreamWriter(socket.getOutputStream());
    }

    public void sendMessage(String str) throws IOException {
        os.write(str + "\n");
        os.flush();
    }
}
