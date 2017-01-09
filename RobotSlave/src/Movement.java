import lejos.hardware.motor.Motor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;

public class Movement {
	private static MovePilot pilot;

	public Movement() {
		/*
		 * This is the code we should use to move robot. The differential Pilot
		 * has been deprecated but the robots have an older version on the SD
		 * card
		 */
		Wheel wheel1 = WheeledChassis.modelWheel(Motor.D, 81.6).offset(-90);
		Wheel wheel2 = WheeledChassis.modelWheel(Motor.A, 81.6).offset(90);

		Chassis chassis = new WheeledChassis(new Wheel[] { wheel1, wheel2 }, WheeledChassis.TYPE_DIFFERENTIAL);
		pilot = new MovePilot(chassis);

	}

	/*
	 * 
	 * For forward and backward format, Direction[F,B], Speed[001-500], Distance. Ex: F1305 or B350450
	 * For left and right format, Angle[positive:left (1 - 360), negative:right (-1 - -360)], Speed[001-500]
	 */
	
	public boolean newCommand(String str) {
			if (str.equals("Move")) {
				forward(150, 200);
			} else if (str.equals("Back")) {
				backward(150, 200);
			} else if (str.equals("Left")) {
				turnLeft();
			} else if (str.equals("Right")) {
				turnRight();
			} else {
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
	
	private static void turn(int speed, int angle){
		pilot.setAngularSpeed(speed);
		pilot.arc(0, angle,true);
	}

	private static void forward(int speed, int distance) {
		pilot.setLinearSpeed(speed);
		pilot.travel(distance,true);
	}

	private static void backward(int speed, int distance) {
		pilot.setLinearSpeed(speed);
		pilot.travel(distance * -1,true);
	}

	private static void turnLeft() {
		turn(150,90);
	}

	private static void turnRight() {
		turn(150,-90);
	}
	
	public static void freeze(){
		pilot.stop();
	}

}
