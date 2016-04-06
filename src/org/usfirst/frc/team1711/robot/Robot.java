package org.usfirst.frc.team1711.robot;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Joystick;
import org.usfirst.frc.team1711.vision.VisionSystem2;

import edu.wpi.first.wpilibj.CameraServer;

import org.usfirst.frc.team1711.robot.RobotMap;
import edu.wpi.first.wpilibj.AnalogPotentiometer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */

public class Robot extends SampleRobot 
{
    //Vision thread
	boolean isDone;
	
	//Auton power level
    double autonPower;
    
    //Object references
    Joystick driveStick;
    Joystick shooterStick;
    Shooter shooter;
    Drive drive;
    Winch winch;
    VisionSystem2 vision;
    AnalogPotentiometer autonPot;
    
    boolean driveAuton = true;


    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() 
    {
        //Define robot objects if they exist
    	if(RobotMap.joystick0 != -1)
    	{
            driveStick = new Joystick(RobotMap.joystick0);	
    	}

    	if(RobotMap.joystick1 != -1)
    	{
            shooterStick = new Joystick(RobotMap.joystick1);
    	}

    	if(RobotMap.autonPot != -1)
    	{
            autonPot = new AnalogPotentiometer(RobotMap.autonPot);
    	}
    	
    	//Instances of various classes
        shooter = new Shooter();
        drive = new Drive();
        winch = new Winch();
        
        // create the vision system which executes on its own 
        // thread
        vision = new VisionSystem2();
        
        //Initialize the vision system *NEEDS WORK*
        vision.init();
        shooter.cameraCenter();
    }
    
	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString line to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the switch structure below with additional strings.
	 * If using the SendableChooser make sure to add them to the chooser code above as well.
	 */
    public void autonomousInit() 
    {
    	
    }

    public void autonomous() 
    {	
    	if(driveAuton = true)
    	{
    		//Sets a default power level thats defined in RobotMap
	    	autonPower = RobotMap.autonDefaultPowerLevel;
	    	
	    	//If the potentiometer is set to 1, use the highest power level
	    	if (autonPot.get() > .5) 
	    	{
	    		autonPower = RobotMap.autonHighPowerLevel;
	    	}
	    	//If the potentiometer is set to 0, use the lower power level
	    	if (autonPot.get() < .5) 
	    	{
	    		autonPower = RobotMap.autonLowPowerLevel;
	    	} 
	    	
	    	//Lower the shooter, wait for 1 second, then start moving
	    	shooter.lowerPitch();
	    	try
	    	{
	    		Thread.sleep(1000);
			} 
	    	catch (InterruptedException e) 
	    	{
				e.printStackTrace();
			}
	    	
	    	//Drive the robot forward, which includes a timer
	    	drive.driveForward(autonPower, RobotMap.autonRunTime, RobotMap.autonLeftBias, RobotMap.autonRightBias);   	
    	}
    	
    	if(driveAuton = false)
    	{
    		//Sets a default power level thats defined in RobotMap
	    	autonPower = RobotMap.autonDefaultPowerLevel;
	    	
	    	//If the potentiometer is set to 1, use the highest power level
	    	if (autonPot.get() > .5) 
	    	{
	    		autonPower = RobotMap.autonHighPowerLevel;
	    	}
	    	//If the potentiometer is set to 0, use the lower power level
	    	if (autonPot.get() < .5) 
	    	{
	    		autonPower = RobotMap.autonLowPowerLevel;
	    	} 
	    	
	    	//Lower the shooter, wait for 1 second, then start moving
	    	shooter.lowerPitch();
	    	try
	    	{
	    		Thread.sleep(1000);
			} 
	    	catch (InterruptedException e) 
	    	{
				e.printStackTrace();
			}
	    	
	    	//Drive the robot forward, which includes a timer
	    	drive.driveForward(autonPower, (RobotMap.autonRunTime) - .5, RobotMap.autonLeftBias, RobotMap.autonRightBias); 
	    	//turn the robot
	    	drive.driveAngle(45, .5);
	    	//aim the shooter
	    	shooter.partialLift(.660);
	    	//shoot the ball
	    	shooter.shoot();
    	}
    }  

    public void operatorControl() 
    {
    	isDone = false;
    	winch.init();
    	
    	//Begin vision processing on a new thread
    	Thread thread = new Thread() 
    	{
    		public void run() 
    		{
    			while (!isDone) 
    			{
    				vision.vision();
    			}
    		}
    	};
    	thread.start(); 
    
    	while (isOperatorControl() && isEnabled()) {

    	//Main operator control loop
        while (isOperatorControl() && isEnabled()) 
        {
        	// manual shooter operations
	    	shooter.pitchControl(shooterStick);
			shooter.fireControl(shooterStick);
	    	shooter.collectorControl(shooterStick);
	    	shooter.cameraAngle(shooterStick);
	    	shooter.shooterTrack();
	    	
	    	// manual winch operations
	    	winch.winchControl(shooterStick);
	    	
	    	// manual drive operations 
	    	drive.DriveArcade(driveStick);
        }
        //Stops motors and vision loop when robot is disabled
        isDone = true;
        drive.stopMotors();
    	}
    }
    	
    public void testPeriodic() 
    {
 
    }
    
}
