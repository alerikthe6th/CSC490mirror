
import lejos.hardware.Button;
import lejos.hardware.Sound;
import java.net.Socket;
import java.net.SocketException;
import java.util.Vector;
import java.io.BufferedReader;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import java.io.InputStreamReader;
import java.net.ServerSocket;

public class RobotSlave extends Thread {
	public static MovePilot pilot;
	private static Vector<String> command;
	private static Vector<Thread> threads;
	static EV3LargeRegulatedMotor left;
	static EV3LargeRegulatedMotor right;
	
	public static void main(String[] args) throws Exception {
		
		//declaring motors
		left = new EV3LargeRegulatedMotor(MotorPort.A);
		right = new EV3LargeRegulatedMotor(MotorPort.D);
		
		//setting synchronizing so can start and stop motor together
		left.synchronizeWith(new RegulatedMotor[]{right});
	

		Wheel wheel1 = WheeledChassis.modelWheel(left, 81.6).offset(-90);
		Wheel wheel2 = WheeledChassis.modelWheel(right, 81.6).offset(90);

		//
		Chassis chassis = new WheeledChassis(new Wheel[] { wheel1, wheel2 }, WheeledChassis.TYPE_DIFFERENTIAL);
		pilot = new MovePilot(chassis);
		
		command = new Vector<String>();
		threads = new Vector<Thread>();

		String lastCom = "stop";

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
					lastCom = "move";
					ForwardThread ft = new ForwardThread();
					ft.start();
					threads.add(ft);
					
				} else if (str.equalsIgnoreCase("back")) {
					lastCom = "back";
					BackThread bt = new BackThread();
					bt.start();
					threads.add(bt);
			
				} else if(str.equalsIgnoreCase("left")){
					 lastCom = "left";
					 LeftThread lt = new LeftThread();
					 lt.start();
					 threads.add(lt);
				}else if(str.equalsIgnoreCase("right")){
					lastCom = "right";
					RightThread rt = new RightThread();
					rt.start();
					threads.add(rt);
				}else if (str.equalsIgnoreCase("stop")) {
					if(lastCom!="stop"){
						stopThreads();
					}
					lastCom="stop";
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
	
				System.out.println("Geting new command");
				str = br.readLine();
				
			}
		} catch (SocketException e) {
			System.out.println(e);
		}
		System.out.println("press any button to exit");
		Button.waitForAnyPress();
	}
	private static void stopThreads(){
		if(RobotSlave.left.isMoving()&&RobotSlave.right.isMoving()){
			RobotSlave.left.startSynchronization();
			RobotSlave.left.stop();
			RobotSlave.right.stop();
			RobotSlave.left.endSynchronization();
		}
		else if(RobotSlave.right.isMoving()){
			RobotSlave.right.stop();
		}
		else{
			RobotSlave.left.stop();
		}
		System.out.println("All Thread Stoped");
	}

}

class ForwardThread extends Thread {

	public ForwardThread() {

	}

	@Override
	public void run() {
		RobotSlave.left.startSynchronization();
		RobotSlave.left.forward();
		RobotSlave.right.forward();
		RobotSlave.left.endSynchronization();
		return;
	}
}

class BackThread extends Thread {

	public BackThread() {
	}

	@Override
	public void run() {

		RobotSlave.left.startSynchronization();
		RobotSlave.left.backward();
		RobotSlave.right.backward();
		RobotSlave.left.endSynchronization();
		return;	}
}

class LeftThread extends Thread {

	public LeftThread() {
		
	}

	@Override
	public void run() {
		RobotSlave.right.forward();
		
		return;
	}

}

class RightThread extends Thread {

	public RightThread() {
	
	}

	@Override
	public void run() {
		RobotSlave.left .forward();
	return;
		}
}

