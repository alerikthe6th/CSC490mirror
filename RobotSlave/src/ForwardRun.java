import java.io.BufferedReader;
import java.io.IOException;

import lejos.hardware.motor.Motor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;

public class ForwardRun implements Runnable{
	private MovePilot pilot;
	private BufferedReader br;
	
	public ForwardRun(BufferedReader reader){

		Wheel wheel1 = WheeledChassis.modelWheel(Motor.A, 81.6).offset(-90);
		Wheel wheel2 = WheeledChassis.modelWheel(Motor.D, 81.6).offset(90);

		Chassis chassis = new WheeledChassis(new Wheel[] { wheel1, wheel2 }, WheeledChassis.TYPE_DIFFERENTIAL);
		pilot = new MovePilot(chassis);
		
		br = reader;
	}

	@Override
	public void run() {
		try {
			while(!br.readLine().equalsIgnoreCase("stop")){
				pilot.travel(20);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	

}
