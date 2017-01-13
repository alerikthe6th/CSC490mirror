
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

	public static Vector movmentQ;
	public static MovePilot pilot;
	private static ForwardThread ft;
	private static BackThread bt;
	private static LeftThread lt;
	private static RightThread rt;

	public static void main(String[] args) throws Exception {
		Movement move = new Movement();

		movmentQ = new Vector();
		
		EV3LargeRegulatedMotor left = new EV3LargeRegulatedMotor(MotorPort.B);
		EV3LargeRegulatedMotor right = new EV3LargeRegulatedMotor(MotorPort.C);
		

		Wheel wheel1 = WheeledChassis.modelWheel(left, 81.6).offset(-90);
		Wheel wheel2 = WheeledChassis.modelWheel(right, 81.6).offset(90);

		Chassis chassis = new WheeledChassis(new Wheel[] { wheel1, wheel2 }, WheeledChassis.TYPE_DIFFERENTIAL);
		pilot = new MovePilot(chassis);
		
		Vector threads = new Vector();
		
		ft = new ForwardThread(pilot);
		bt = new BackThread(pilot);
		lt = new LeftThread(pilot);
		rt = new RightThread(pilot);

		int port = 4567;
		ServerSocket server = new ServerSocket(port);
		server.setSoTimeout(0);
		System.out.println("waiting for connection");
		try {
			Socket client = server.accept();
			System.out.println("connected to client");

			BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
			String str = br.readLine();

			while (str != null) {
				Thread current;
				
				System.out.println(str);
				if (str.equalsIgnoreCase("move")) {
					System.out.println("HERE");
					ft.start();
				} else if (str.equalsIgnoreCase("back")) {
					bt.start();
				}
				
				System.out.println("thread set");
			
				if (str.equalsIgnoreCase("stop")) {
					
				}
				System.out.println(str);
				str = br.readLine();
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

	public ForwardThread(MovePilot p) {
		pilot = p;
	}

	@Override
	public void run() {
		pilot.travel(20);

		if (interrupted()) {
			return;
		}
	}
}

class BackThread extends Thread {
	private MovePilot pilot;

	public BackThread(MovePilot p) {
		pilot = p;

	}

	@Override
	public void run() {
		pilot.travel(-20);
		if (interrupted()) {
			return;
		}
	}
}

class LeftThread extends Thread {
	private MovePilot pilot;

	public LeftThread(MovePilot p) {

		pilot = p;
	}

	@Override
	public void run() {
		pilot.arc(0, -10);
		if (interrupted()) {
			return;
		}
	}

}

class RightThread extends Thread {
	private MovePilot pilot;

	public RightThread(MovePilot p) {

		pilot = p;
	}

	@Override
	public void run() {
		pilot.arc(0, 10);
		if(interrupted()){
			return;
		}
	}
}
