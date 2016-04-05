package org.usfirst.frc.team1711.robot;


import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.AnalogPotentiometer; 
import edu.wpi.first.wpilibj.AnalogGyro; 
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.AnalogInput;

//button 6 & 7 shooter angle adjustment, button 3 enable shooting, button 5 disable shooting, button 1 fire the disable shooting. 


public class Shooter 
{
	// potentiometer outputs for shooter position
	double potHighest=0.300;
	double potMidPoint=0.633;
	double potLowest=0.685;
	
	Servo motorShooterKicker;
	Servo motorCameraAngle;
	Servo tiltServo;
	Talon motorShooterPitchLeft;
	Talon motorShooterPitchRight;
	Talon motorShooterLeft;
	Talon motorShooterRight;
	AnalogGyro gyro=null;
	AnalogPotentiometer pot=null;
	DigitalInput intakeLimit=null;
	AnalogInput ultra=null;
	int lastJogButton=0;
	boolean atMax=false;
	boolean atMin=false;
	boolean inFireMode=false;
	boolean inCollectMode=false;
	Joystick shooterStick;
	
	public Shooter()
	{
		//Creating Servo objects
		if(RobotMap.motorShooterKicker != -1)
		{
			motorShooterKicker = new Servo(RobotMap.motorShooterKicker); 
		}
		
		if(RobotMap.motorCameraAngle != -1)
		{
			motorCameraAngle = new Servo(RobotMap.motorCameraAngle);
		}
		
		if(RobotMap.motorCameraTilt != -1)
		{
			tiltServo = new Servo(RobotMap.motorCameraTilt);
		}
		
		//Creating Talon objects
		if(RobotMap.motorShooterPitchLeft != -1)
		{
			motorShooterPitchLeft = new Talon(RobotMap.motorShooterPitchLeft);
		}
		
		if(RobotMap.motorShooterPitchRight != -1)
		{
			motorShooterPitchRight = new Talon(RobotMap.motorShooterPitchRight);
		}
		
		if(RobotMap.motorShooterLeft != -1)
		{
			motorShooterLeft = new Talon(RobotMap.motorShooterLeft);
		}
		
		if(RobotMap.motorShooterRight != -1)
		{
			motorShooterRight = new Talon(RobotMap.motorShooterRight);
		}
		
		//creating joystick
		if(RobotMap.joystick1 != -1)
		{
			shooterStick = new Joystick(RobotMap.joystick1);
		}
		
		// define the shooter potentiometer
		if(RobotMap.shooterPot != -1)
		{
			pot=new AnalogPotentiometer(RobotMap.shooterPot);
		}
	}
		
	/**
	 * Enables/Disables the shooter and if a fire request
	 * spins up the wheels and ramps for shot then fires
	 * 
	 * @param joystick
	 */
	public void fireControl(Joystick shooterStick) {
		
		// look for a shoot request
		if(shooterStick.getRawAxis(RobotMap.shooterStickBtnShoot)<-0.5) {
			// perform the following on a thread
			// to allow the rest of the robot controls
			// to be enabled
			Thread thread = new Thread() {
			    public void run() {			    
			    	try {
				    	double initialSpeed=0.3;	// initial wheel speed
				    	double shootSpeed=0.8;		// shooting speed
				    	motorShooterLeft.set(-initialSpeed);
				    	motorShooterRight.set(-initialSpeed);
				    	if(shooterStick.getRawAxis(RobotMap.shooterStickBtnShoot)<-0.5) {
					    	sleep(750); 				// wait half second to get up to speed
					    	motorShooterLeft.set(-shootSpeed);
					    	motorShooterRight.set(-shootSpeed);
					    	if(shooterStick.getRawAxis(RobotMap.shooterStickBtnShoot)<-0.5) {
						    	sleep(750);					// start to spin-up
						    	motorShooterKicker.setAngle(0);	// SHOOT
						    	// wait for ball release
						    	sleep(1000);
					    	}
				    	}
						motorShooterKicker.setAngle(90);	// retract the kicker

						// shut down the motors and disable fire control
				    	motorShooterLeft.set(0);
				    	motorShooterRight.set(0);
						inFireMode=false;				
				    } catch(InterruptedException ie) {}
			    }
			};
		    thread.start();
		}
	}
	
	public void cameraCenter() 
	//centers the camera on the rotating mount in the x-y direction
	//called when the robot is initialized
	{
		if(motorCameraAngle != null)
		{
			motorCameraAngle.setAngle(80);
			System.out.println("set");
		}
	}
	
	public void cameraAngle (final Joystick shooterStick) 
	//controls the angle and tilt of the camera mount
	{
		if(shooterStick != null && motorCameraAngle != null)
		{
			double servoAngle = motorCameraAngle.getAngle();
			
			//angles the camera right
			if (shooterStick.getRawButton(RobotMap.cameraAngleRightButton)) {
				motorCameraAngle.setAngle(servoAngle - 1);		
			}
					
			//angles the camera left
			if (shooterStick.getRawButton(RobotMap.cameraAngleLeftButton)) {
				motorCameraAngle.setAngle(servoAngle + 1);
			}
			
			//centers the camera in the x-y direction
			if (shooterStick.getRawButton(RobotMap.cameraCenterViewButton)) {
				motorCameraAngle.setAngle(90);
			}
		}
	}
	public void collectorControl(Joystick shooterStick) 
	//controls the intake of the ball
	{
		if(shooterStick != null && motorShooterLeft != null && motorShooterRight != null && pot != null && motorShooterPitchLeft != null && motorShooterPitchRight != null)
		{
			if(shooterStick.getRawButton(RobotMap.shooterStickBtnShooterCollect)) {
		    	double collectorSpeed=0.5;	//  wheel speed

		    	// disable the fire mode
	    		inFireMode=false;
	    		
	    		// set the collect mode
	    		inCollectMode=true;
	    		motorShooterLeft.set(collectorSpeed);
	    		motorShooterRight.set(collectorSpeed);
	    		
	    		// move the shooter down to collect level
		    	if(pot.get()<potLowest) {
		    		motorShooterPitchLeft.set(-1);
		    		motorShooterPitchRight.set(-1);
		    	}
		    	else {
		    		motorShooterPitchLeft.set(0);
		    		motorShooterPitchRight.set(0);
		    	}
			}
			else {
				// if was in collect mode then switch off wheels and lift 
				// shooter to mid point. Thread the operation to not affect
				// machine performance
				if(inCollectMode) {
			    	motorShooterLeft.set(0);
			    	motorShooterRight.set(0);
			    	Thread thread = new Thread() {
					    public void run() {			    
					    	// move the pitch to mid level
					    	while(pot.get()>potMidPoint) {
					    		motorShooterPitchLeft.set(1);
					    		motorShooterPitchRight.set(1);
					    	}
					    	motorShooterPitchLeft.set(0);
					    	motorShooterPitchRight.set(0);
					    }
					};
				    thread.start();
				    inCollectMode=false;
				}		
			}
		}
		
	}
	public void liftPitch ()
	//this method raises the pitch to the max value
	//did not use the != null here, because if the shooter comes off, we won't use it in auton obviously
	{
		Thread thread = new Thread() {
			public void run() {
				{
					while (pot.get()>potHighest) {			// tested value for topmost
						motorShooterPitchLeft.set(1);
						motorShooterPitchRight.set(1);// upper limit hit, stop move
						
					}
				}
				atMax=true;
				motorShooterPitchLeft.set(0);
				motorShooterPitchRight.set(0);
				
			}
		};
		thread.start();
	}
	
	public void lowerPitch ()
	//this method lowers the pitch to the min value
	//did not use the != null here, because if the shooter comes off, we won't use it in auton obviously
	{
		Thread thread = new Thread() 
		{
			public void run() 
			{					
				while(pot.get()<.640) // tested value for bottommost
				{				
					motorShooterPitchLeft.set(-1);
					motorShooterPitchRight.set(-1);
					
				}
				// lower limit hit, stop move
				motorShooterPitchLeft.set(0);					
				motorShooterPitchRight.set(0); 
				atMin=true;
			}
		};
		thread.start();
	
	}
	
	public void partialLift(double potValue)
	//this method raises the pitch part of the way
	{ 
		Thread thread = new Thread() 
		{
			public void run() 
			{
				//if the shooter is too low, raise it
				while(pot.get() > potValue) 
				{
					motorShooterPitchLeft.set(-1);
					motorShooterPitchRight.set(-1);
				}
				//when the pot value is reached, stop
				motorShooterPitchLeft.set(0);
				motorShooterPitchLeft.set(0);
			}
		};
		thread.start();
	}
	
	public void partialLower(double potValue)
	//this method lowers the pitch part of the way
	{
		//if the shooter is too high, raise it
		while(pot.get() < potValue)
		{
			motorShooterPitchLeft.set(1);
			motorShooterPitchRight.set(1);
		}
		//when the pot value is reached, stop
		motorShooterPitchLeft.set(0);
		motorShooterPitchLeft.set(0);
	}

	public void pitchControl(Joystick shooterStick)
	{
		if(pot!=null && motorShooterPitchLeft!=null && motorShooterPitchRight!=null) 
		{
			if(pot.get()<potHighest) {			// tested value for topmost
				motorShooterPitchLeft.set(0);
				motorShooterPitchRight.set(0);// upper limit hit, stop move
				atMax=true;
				atMin = false;
				System.out.println("At max");
				
			}
			if(pot.get()>potLowest) {			// tested value for bottommost
				motorShooterPitchLeft.set(0);
				motorShooterPitchRight.set(0); // lower limit hit, stop move
				atMin=true;
				atMax = false;
				System.out.println("At min");
			}		
		}
		
		if(Math.abs(shooterStick.getRawAxis(1)) < .08)
		{
			motorShooterPitchLeft.set(shooterStick.getRawAxis(1));
			motorShooterPitchRight.set(shooterStick.getRawAxis(1));
		} 		
	}
	
	public void shooterTrack() //Correction should be set in RobotMap
	{
		if(RobotMap.shooterPot != -1)
		{
			//Sets the tilt camera to the same angle as the shooter
			//Alternatively, we could use a scaling factor -- it needs testing to determine which is better
			//3.003 = .333 (range of pot) * 100 (tiltServo.set takes a number between 0 and 1)
			double potAngle = pot.get();
			tiltServo.set(potAngle*3.003+RobotMap.shooterServoCorrection);
		}
	}
		
}
