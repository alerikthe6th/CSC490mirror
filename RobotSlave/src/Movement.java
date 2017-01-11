import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.hardware.Button;
import lejos.hardware.motor.*;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;

public class Movement {
	private static MovePilot pilot;
	private static EV3LargeRegulatedMotor motorRight;
	private static EV3LargeRegulatedMotor motorLeft;

	public Movement() {
		/*
		 * This is the code we should use to move robot. The differential Pilot
		 * has been deprecated but the robots have an older version on the SD
		 * card
		 */
		motorRight = new EV3LargeRegulatedMotor(MotorPort.D);
		motorLeft = new EV3LargeRegulatedMotor(MotorPort.A);
		
		Wheel wheel1 = WheeledChassis.modelWheel(motorRight, 81.6).offset(-90);
		Wheel wheel2 = WheeledChassis.modelWheel(motorLeft, 81.6).offset(90);

		Chassis chassis = new WheeledChassis(new Wheel[] { wheel1, wheel2 }, WheeledChassis.TYPE_DIFFERENTIAL);
		pilot = new MovePilot(chassis);
 
	}

	/*
	 * 
	 * For forward and backward format, Direction[F,B], Speed[001-500], Distance. Ex: F1305 or B350450
	 * For left and right format, Angle[positive:left (1 - 360), negative:right (-1 - -360)], Speed[001-500]
	 */
	
	public boolean newCommand(String str) {
			if (str.equalsIgnoreCase("Move")) {
				forward(150, 200);
			} else if (str.equalsIgnoreCase("Back")) {
				backward(150, 200);
			} else if (str.equalsIgnoreCase("Left")) {
				turnLeft();
			} else if (str.equalsIgnoreCase("Right")) {
				turnRight();
			} else if(str.equalsIgnoreCase("stop")){
				freeze();
			}
			else {
			
				char dir = str.charAt(0);
				int strLength = str.length();
				int power;
				if(dir=='F'||dir=='B'){
					power = Integer.parseInt(str.substring(1, 4));
					int dist = Integer.parseInt(str.substring(4));
					if(dir=='F'){
						forward(power,dist);
					}
					else{
						backward(power,dist);
					}
				}
				else{
					int angle = Integer.parseInt(str.substring(0,strLength-3));
					power = Integer.parseInt(str.substring(strLength-3));
					turn(power, angle);
				}
			}
			return false;
	}
	
	private void turn(int speed, int angle){
		
		pilot.setAngularSpeed(speed);
		pilot.arc(0, angle,true);
	}

	private void forward(int speed, int distance) {
		//pilot.forward();
		motorRight.forward();
		motorLeft.forward();
	}

	private void backward(int speed, int distance) {
		pilot.backward();
	}

	/*TODO if there is a forward or backward then can't turn because call to MovePilot
	 * 
	 */
	private void turnLeft() {
		//turn(150,90);
		//motorRight.forward();
		pilot.rotate(-5);
		
	}

	private void turnRight() {
		//turn(150,-90);
		//motorLeft.forward();
		pilot.rotateRight();
	}
	
	private static void freeze(){
		System.out.println("In freeze");
		pilot.stop();
	}

}
