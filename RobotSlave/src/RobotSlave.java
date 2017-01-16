
import lejos.hardware.Bluetooth; 
import lejos.hardware.Button;
import lejos.hardware.Sound;

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
					ForwardThread ft = new ForwardThread();
					ft.start();
					threads.add(ft);
					
				} else if (str.equalsIgnoreCase("back")) {
					BackThread bt = new BackThread();
					bt.start();
					threads.add(bt);
			
				} else if(str.equalsIgnoreCase("left")){
					 LeftThread lt = new LeftThread();
					 lt.start();
					 threads.add(lt);
				}else if(str.equalsIgnoreCase("right")){
					RightThread rt = new RightThread();
					rt.start();
					threads.add(rt);
				}else if (str.equalsIgnoreCase("stop")) {
					//System.out.println("Threads sixe = "+threads.size());
					/*while(threads.size()>0){
						Thread oldThread = (Thread)threads.get(0);
						if(oldThread.isAlive()){
							try{
								//System.out.println("Interupt");
								oldThread.interrupt();
								oldThread.join();
							}
							catch(Exception E){
								//System.out.println("Exception");
							}
						}
						else{
							threads.remove(0);
							//System.out.println("HereHere");
							
						}
					}
					System.out.println("All Thread Stoped");*/
					stopThreads();
				}else if(str.equalsIgnoreCase("einstein")){
					System.out.println("Einstein's Hair!!!");
					Sound.setVolume(80);
					Sound.playTone(523, 250);
					Thread.sleep(200);
					Sound.playTone(523,100);
					Thread.sleep(100);
					Sound.playTone(466, 100);
					Thread.sleep(100);
					Sound.playTone(523, 100);
					Thread.sleep(100);
					
				}
				//System.out.println(str);
				System.out.println("Geting new command");
				str = br.readLine();
				//command.set(0, str);
			}
		} catch (SocketException e) {
			System.out.println(e);
		}
		System.out.println("press any button to exit");
		Button.waitForAnyPress();
	}
	private static void stopThreads(){
		while(threads.size()>0){
			Thread oldThread = (Thread)threads.get(0);
			if(oldThread.isAlive()){
				try{
					//System.out.println("Interupt");
					oldThread.interrupt();
					oldThread.join();
				}
				catch(Exception E){
					//System.out.println("Exception");
				}
			}
			else{
				threads.remove(0);
				//System.out.println("HereHere");
				
			}
		}
		System.out.println("All Thread Stoped");
	}

}

class ForwardThread extends Thread {

	public ForwardThread() {

		System.out.println("Thread created");
	}

	@Override
	public void run() {
		while(!this.isInterrupted()){
			try{
				System.out.println("moving forward ...");
				RobotSlave.pilot.travel(100);
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

	public BackThread() {
		System.out.println("Back Thread Created");
	}

	@Override
	public void run() {

		while(!this.isInterrupted()){
			try{
				System.out.println("moving back ...");
				RobotSlave.pilot.travel(-100);
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

	public LeftThread() {
		
	}

	@Override
	public void run() {
		while(!this.isInterrupted()){
			try{
				System.out.println("moving left ...");
				RobotSlave.pilot.arc(0, -20);
				
				
			}
			catch(Exception E){
				break;
			}
		}
		return;
	}

}

class RightThread extends Thread {

	public RightThread() {
	
	}

	@Override
	public void run() {while(!this.isInterrupted()){
		try{
			System.out.println("moving right ...");
			RobotSlave.pilot.arc(0, 20);
			
		} 
		catch(Exception E){
			break;
		}
	}
	return;
		}
}

