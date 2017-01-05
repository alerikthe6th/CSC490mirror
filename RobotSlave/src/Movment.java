import lejos.hardware.motor.Motor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;

public class Movment {
	private static MovePilot pilot;
	public Movment(){
		/*
		 * This is the code we should use to move robot. The differential Pilot has been deprecated but the robots have an older version on the SD card
		 */
		Wheel wheel1 = WheeledChassis.modelWheel(Motor.D, 81.6).offset(-90);
		Wheel wheel2 = WheeledChassis.modelWheel(Motor.A, 81.6).offset(90);
		
		Chassis chassis = new WheeledChassis(new Wheel[] {wheel1, wheel2}, WheeledChassis.TYPE_DIFFERENTIAL);
		pilot = new MovePilot(chassis);

	}
	public boolean newCommand(String str){
		if(str.equals("Move")){
			forward(150,200);
		}
		else if(str.equals("Back")){
			backward(150, 200);
		}
		else if(str.equals("Left")){
			turnLeft();
		}
		else if(str.equals("Right")){
			turnRight();
		}
		else{
			return false;
		}
		return true;
	}
	private static void forward(int speed, int distance){
		pilot.setLinearSpeed(speed);
		pilot.travel(distance);
	}
	
	private static void backward(int speed, int distance){
		pilot.setLinearSpeed(speed);
		pilot.travel(distance*-1);
	}
	
	private static void turnLeft(){
		pilot.arc(0, 90);
	}
	
	private static void turnRight(){
		pilot.arc(0, -90);
	}

}
