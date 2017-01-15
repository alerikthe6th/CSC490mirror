
import lejos.hardware.Bluetooth; 
import lejos.hardware.Button;

import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Vector;
import java.io.BufferedReader;
import java.io.IOException;

import lejos.hardware.lcd.*;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.utility.PilotProps;

import java.io.InputStreamReader;
import java.net.ServerSocket;

public class RobotSlave extends Thread {
	public static MovePilot pilot;
	private static ForwardThread ft;
	private static BackThread bt;
	private static LeftThread lt;
	private static RightThread rt;
	private static String lastCommand;
	private static Vector<String> command;
	private static Vector<Thread> threads;
	
	public static void main(String[] args) throws Exception {
		Movement move = new Movement();

		
		EV3LargeRegulatedMotor left = new EV3LargeRegulatedMotor(MotorPort.A);
		EV3LargeRegulatedMotor right = new EV3LargeRegulatedMotor(MotorPort.D);
		

		Wheel wheel1 = WheeledChassis.modelWheel(left, 81.6).offset(-90);
		Wheel wheel2 = WheeledChassis.modelWheel(right, 81.6).offset(90);

		Chassis chassis = new WheeledChassis(new Wheel[] { wheel1, wheel2 }, WheeledChassis.TYPE_DIFFERENTIAL);
		pilot = new MovePilot(chassis);
		
		command = new Vector<String>();
		threads = new Vector<Thread>();
		
	/*	ft = new ForwardThread(pilot,command);
		bt = new BackThread(pilot,command);
		lt = new LeftThread(pilot,command);
		rt = new RightThread(pilot,command);*/

		int port = 4567;
		ServerSocket server = new ServerSocket(port);
		server.setSoTimeout(0);
		System.out.println("waiting for connection");
		try {
			Socket client = server.accept();
			System.out.println("connected to client");

			BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
			String str = br.readLine();
			command.add(str);
			
			while (str != null) {
				System.out.println(str);
				if (str.equalsIgnoreCase("move")) {
					ForwardThread ft = new ForwardThread(pilot);
					ft.start();
					threads.add(ft);
					
				} else if (str.equalsIgnoreCase("back")) {
					BackThread bt = new BackThread(pilot);
					bt.start();
					threads.add(bt);
			
				} 
				else if (str.equalsIgnoreCase("stop")) {
					System.out.println("Threads sixe = "+threads.size());
					while(threads.size()>0){
						Thread oldThread = (Thread)threads.get(0);
						if(oldThread.isAlive()){
							try{
								System.out.println("Interupt");
								oldThread.interrupt();
							}
							catch(Exception E){
								System.out.println("Exception");
							}
						}
						else{
							threads.remove(0);
							System.out.println("HereHere");
							
						}
					}
					System.out.println("All Thread Stoped");
				}
				//System.out.println(str);
				str = br.readLine();
				//command.set(0, str);
			}
		} catch (SocketException e) {
			System.out.println(e);
		}
		System.out.println("press any button to exit");
		Button.waitForAnyPress();
	}

}

class ForwardThread extends Thread {
	private MovePilot pilot;
	//private Vector<String> command;

	public ForwardThread(MovePilot p) {
		pilot = p;
		//command=c;
		System.out.println("Thread created");
	}

	@Override
	public void run() {
		while(!this.isInterrupted()){
			try{
				System.out.println("moving forward ...");
				pilot.travel(50);
				//pilot.forward();
				//wait(1000);
				
			}
			catch(Exception E){
				break;
			}
		}
		return;
	}
}

class BackThread extends Thread {
	private MovePilot pilot;

	public BackThread(MovePilot p) {
		pilot = p;
	}

	@Override
	public void run() {

		while(!this.isInterrupted()){
			try{
				System.out.println("moving back ...");
				pilot.travel(-50);
				//pilot.forward();
				//wait(1000);
				
			}
			catch(Exception E){
				break;
			}
		}
		return;	}
}

class LeftThread extends Thread {
	private MovePilot pilot;
	private Vector command;

	public LeftThread(MovePilot p, Vector c) {
		command = c;
		pilot = p;
	}

	@Override
	public void run() {
		while(command.get(0).toString().equalsIgnoreCase("left")){
			pilot.arc(0, -10);
		}
		if (interrupted()) {
			return;
		}
	}

}

class RightThread extends Thread {
	private MovePilot pilot;
	private Vector command;

	public RightThread(MovePilot p, Vector c) {
		command = c;
		pilot = p;
	}

	@Override
	public void run() {
		while(command.get(0).toString().equalsIgnoreCase("right")){
			pilot.arc(0, 10);
		}
		if(interrupted()){
			return;
		}
	}
}
