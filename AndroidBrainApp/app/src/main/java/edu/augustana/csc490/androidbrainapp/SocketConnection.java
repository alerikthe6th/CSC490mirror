package edu.augustana.csc490.androidbrainapp;

import java.io.IOException;
import java.net.Socket;
import java.io.OutputStreamWriter;


/**
 * The SocketConnection class creates a socket object with a port number and IP address
 * to connect an android device to a lejos Robot. the void method sendMessage() allows to send
 * strings to the robot
 *
 * Created by hamby on 12/15/2016.
 */

public class SocketConnection {

    //data fields
    private Socket socket;
    private OutputStreamWriter os;

    /**
     * Constructor that creates a socket using port number and IP address parameters passed in
     *
     * @param portNum
     * @param ip
     * @throws Exception
     */
    public SocketConnection(int portNum, String ip) throws Exception{
        socket = new Socket(ip, portNum);
        os = new OutputStreamWriter(socket.getOutputStream());
    }

    /**
     * sendMessage sends a string of text to the robot, typically sent as a command
     * i.e. "move", "back", "left", "right"
     *
     * @param str
     * @throws IOException
     */
    public void sendMessage(String str) throws IOException {
        os.write(str + "\n");
        os.flush();
    }
}
