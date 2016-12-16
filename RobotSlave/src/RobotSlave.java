
import lejos.hardware.Bluetooth;
import lejos.hardware.Button;

import java.net.Socket;
import java.net.SocketException;
import java.io.BufferedReader;
import lejos.hardware.lcd.*;
import java.io.InputStreamReader;
import java.net.ServerSocket;

public class RobotSlave {

	public static void main(String[] args) throws Exception{
		int port = 4567;
		ServerSocket server = new ServerSocket(port);
		server.setSoTimeout(0);
		System.out.println("waiting for connection");
		try{
			Socket client = server.accept();
			System.out.println("connected to client");
			
			BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
			String str = br.readLine();
			while(str != null){
				System.out.println(str);
				str = br.readLine();
			}
		} catch(SocketException e){
			System.out.println(e);
		}
		System.out.println("press any button to exit");
		Button.waitForAnyPress();
	}

}
