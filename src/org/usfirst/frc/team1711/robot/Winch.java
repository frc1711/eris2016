package org.usfirst.frc.team1711.robot;

import java.util.Date;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;

public class Winch {
	Jaguar frontCIM;
	Jaguar rearCIM;
	Servo hookServo;
	boolean hookReleased=false;
	boolean bidirectional=false;
	long timerStart=0;
	Date date=new Date();
	
	public Winch() {
		frontCIM=new Jaguar(RobotMap.motorWinchFront);
		rearCIM=new Jaguar(RobotMap.motorWinchRear);
		hookServo=new Servo(RobotMap.servoWinchHook);
	}

	public void init() {
		// reset all the conditional flags
		hookReleased=false;
		bidirectional=false;
		timerStart=0;
	}
	
	public void winchControl(Joystick stick) {
		// can only use the winch when the left hand trigger is pressed
		if(stick.getRawAxis(RobotMap.shooterStickBtnShoot)>0.8) {
			
			// get the current position of the winch stick
			double power=stick.getRawAxis(RobotMap.shooterStickStickWinch);

			// to save accidently retracting the tape into the machine, it will only
			// be possible to pay it out for one second before being able to retract
			if(bidirectional==false && power>0) {

				// if the hook is not released and we are attempting to pay out
				// the tape, then fire the hook release servo
				if(hookReleased==false && power>0.1) {
					hookServo.setAngle(180);
					hookReleased=true;
					// wait half a second for servo to unclamp hook (this will freeze robot for 1/2 sec)
					try {
						Thread.sleep(500);
					} catch(Exception e) {}
				} 

				// if timerStart=0, then get the current millseconds
				if(timerStart==0)
					timerStart=date.getTime();
				else
					if(date.getTime()-timerStart>1000)
						bidirectional=true;
				
				
				// set the motor power
				frontCIM.set(power);
				rearCIM.set(power);
			}

			// we can only retract if bidirectional allowed
			if (bidirectional==true) {
				// set the motor power
				frontCIM.set(power);
				rearCIM.set(power);
			}
		}
	}
}