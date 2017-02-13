
import lejos.hardware.Button;
import lejos.hardware.Sound;
import java.net.Socket;
import java.net.SocketException;
import java.util.Vector;
import java.io.BufferedReader;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import java.io.InputStreamReader;
import java.net.ServerSocket;
/**
 * This is the code to load onto a Mindstorm Robot to what it work with the Augustana SI control app.
 * Requires LegoMinstorm EV3 robot with Lejos.
 * @author Abby Thomson
 *
 */
public class RobotSlave extends Thread {
	public static MovePilot pilot;
	private static Vector<String> command;
	private static Vector<Thread> threads;
	protected static EV3LargeRegulatedMotor left;
	protected static EV3LargeRegulatedMotor right;
	protected static EV3TouchSensor touch;

	public static void main(String[] args) throws Exception {
		
		//declaring sensor
		touch = new EV3TouchSensor(SensorPort.S1);

		// declaring motors
		left = new EV3LargeRegulatedMotor(MotorPort.A);
		right = new EV3LargeRegulatedMotor(MotorPort.D);

		// setting synchronizing so can start and stop motor together
		left.synchronizeWith(new RegulatedMotor[] { right });
		
		//sets the motor acceleration, lower than 6000 default for smoother motions
		left.setAcceleration(3000);
		right.setAcceleration(3000);

		Wheel wheel1 = WheeledChassis.modelWheel(left, 81.6).offset(-90);
		Wheel wheel2 = WheeledChassis.modelWheel(right, 81.6).offset(90);

		Chassis chassis = new WheeledChassis(new Wheel[] { wheel1, wheel2 }, WheeledChassis.TYPE_DIFFERENTIAL);
		pilot = new MovePilot(chassis);

		//Vectors that keep track of Commands passes in and active Threads
		command = new Vector<String>();
		threads = new Vector<Thread>();

		String lastCom = "stop";
		
		String[] testCom = new String[]{"setforward","stop"};
		//port number for connection with app
		int port = 4567;
		ServerSocket server = new ServerSocket(port);
		
		server.setSoTimeout(0);
		System.out.println("waiting for connection");
	
		try {
			//Connects to client socket on app
			Socket client = server.accept();
			System.out.println("connected to client");

			//Declares BufferedReader, used to read in commands from app
			BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
			String str = br.readLine();
			command.add(str);

			//checks if command is from image processing, is so starts thread with touch sensor.
			if(str.contains("set")){
				SensorThread st = new SensorThread();
				st.start();
			}
			while (str != null) {
				System.out.println("Command: " + str);
				
				//Check if the robot is currently moving, if so call stopThreads();
				if(left.isMoving()||right.isMoving()){
					stopThreads();
				}
				
				//Check if command is a forward command, if so creates and starts a forward Thread
				if (str.contains("for")) {
					lastCom = "forward";
					System.out.println("Forward");
					ForwardThread ft = new ForwardThread();
					ft.start();
					threads.add(ft);
					if(str.equalsIgnoreCase("setforward")){
						Thread.sleep(1500);
						stopThreads();
					}

				} else if (str.equalsIgnoreCase("back")) {
					lastCom = "back";
					BackThread bt = new BackThread();
					bt.start();
					threads.add(bt);

				} else if (str.contains("left")) {
					lastCom = "left";
					LeftThread lt = new LeftThread();
					lt.start();
					threads.add(lt);
					if(str.equalsIgnoreCase("setleft")){
						Thread.sleep(150);
						stopThreads();
					}
				} else if (str.contains("right")) {
					lastCom = "right";
					RightThread rt = new RightThread();
					rt.start();
					threads.add(rt);
					if(str.equalsIgnoreCase("setright")){
						Thread.sleep(150);
						stopThreads();
					}
				} else if (str.equalsIgnoreCase("stop")) {
					if (lastCom != "stop") {
						stopThreads();
					}
					lastCom = "stop";
				}else if (str.equalsIgnoreCase("einstein")) {
					System.out.println("Einstein's Hair!!!");
					Sound.setVolume(80);
					Sound.playTone(523, 250);
					Thread.sleep(200);
					Sound.playTone(523, 100);
					Thread.sleep(100);
					Sound.playTone(466, 100);
					Thread.sleep(100);
					Sound.playTone(523, 100);
					Thread.sleep(100);
				}
				
				System.out.println("Getting new command");
				str = br.readLine();
				
			}
		} catch (SocketException e) {
			System.out.println(e);
		}
		System.out.println("press any button to exit");
		Button.waitForAnyPress();
	}
	
	/**
	 * Checks if the robot robot is currently moving and if it is then stop it.
	 * @param
	 * @return
	 */
	public static void stopThreads() {
		if (RobotSlave.left.isMoving() && RobotSlave.right.isMoving()) {
			RobotSlave.left.startSynchronization();
			RobotSlave.left.stop();
			RobotSlave.right.stop();
			RobotSlave.left.endSynchronization();
		} else if (RobotSlave.right.isMoving()) {
			RobotSlave.right.stop();
		} else {
			RobotSlave.left.stop();
		}
	}

}



class ForwardThread extends Thread {
	
	public ForwardThread() {

	}
	/**
	 * Starts in syntonization the left and right motors to move the robot forward
	 * 
	 * @param
	 * @return
	 */
	@Override
	public void run() {
		System.out.println("In Forward Thread");
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
	/**
	 * Starts in syntonization the left and right motors to move the robot forward
	 * 
	 * @param
	 * @return
	 */
	@Override
	public void run() {

		RobotSlave.left.startSynchronization();
		RobotSlave.left.backward();
		RobotSlave.right.backward();
		RobotSlave.left.endSynchronization();
		return;
	}
}

class LeftThread extends Thread {

	public LeftThread() {

	}

	/**
	 * Starts the right motor to turn the robot to the left
	 * 
	 * @param
	 * @return
	 */
	@Override
	public void run() {
		RobotSlave.right.forward();
		return;
	}

}

class RightThread extends Thread {

	public RightThread() {

	}
	
	/**
	 * Starts the left motor to turn the robot to the right
	 * 
	 * @param
	 * @return
	 */
	@Override
	public void run() {
		RobotSlave.left.forward();
		return;
	}
}

class SensorThread extends Thread{
	public SensorThread(){
	}
	
	/**
	 * Starts gathering readings from the EV3 Touch Sensor and if the sensor is pushed, calls stopThreads().
	 * @param
	 * @return
	 */
	public void run(){
		SampleProvider bumpSampleProvider = RobotSlave.touch.getTouchMode();
		float[] bumpSensorData = new float[bumpSampleProvider.sampleSize()];
		
		//loops until motors start, needed because thread begins execution before motors start.
		while(!RobotSlave.left.isMoving()||!RobotSlave.right.isMoving()){
			System.out.println("In NOT moving While");
		}
		//As long as the robot is moving, fetches  a reading from the touch sensor and if pressed calls stopThreads()
		while(RobotSlave.left.isMoving()||RobotSlave.right.isMoving()){
			System.out.println("In Moving While");
			bumpSampleProvider.fetchSample(bumpSensorData, 0);
			System.out.println(bumpSensorData[0]);
			if(bumpSensorData[0]==1){
				RobotSlave.stopThreads();
				Sound.beepSequenceUp();
			}

		}
	}
}
