package org.usfirst.frc.team1711.robot;

import edu.wpi.first.wpilibj.Joystick.AxisType;

public class RobotMap 
{
	/*This class is used to define constants and inputs
	 * so that we can easily access them from all different
	 * areas of the code. This is basically just a constants file.
	 * 
	 * If a object needs to be disabled
	 * the variable that sets the input of the 
	 * object should be set to -1. We have added 
	 * a check in the construction of all objects, using
	 * -1 to determine if we want to disable a device on the
	 * robot in an efficient manner.
	 */ 
	
	//Joystick inputs
	public static final int joystick0 = 0;
	public static final int joystick1 = 1;
	
	//Motor inputs
	public static final int motorDriveLeftMasterId = 1;
	public static final int motorDriveLeftSlaveId = 3;
	public static final int motorDriveRightMasterId = 2;
	public static final int motorDriveRightSlaveId = 0;
	
	//Drive encoder inputs and constants
	public static final int encoderDriveLeftA = 2;
	public static final int encoderDriveLeftB = 3;
	public static final int encoderDriveRightA = 0;
	public static final int encoderDriveRightB = 1;
	public static final double distancePerPulse = 2.800;
	
	//Shooter inputs
	public static final int motorShooterLeft = 7;
	public static final int motorShooterRight = 8;
	public static final int motorShooterPitchLeft = 0;
	public static final int motorShooterPitchRight = 1;
	public static final int motorShooterKicker = 9;
	public static final int motorWinchFront=2;
	public static final int motorWinchRear=3;
	public static final int servoWinchHook=4;
	
	//Shooter components
	public static final int shooterPot = 0;
	
	//switches for auton
	public static final int autonPot = 3;
	public static final int autonSwitch = 9;
	
	//Gyro inputs
	public static final int gyroFlat = 1;
	
	//Vision buttons
	
	
	//Vision camera inputs
	public static final int motorCameraAngle = 5;
		
	//drive button definitions
	public static final int driverStickBtnEncoderPulse=8;
	public static final int driveStickBtnSlow = 1;
	public static final int driveStickBtnGyro = 3;

	// shooter joystick button definitions
	public static final int shooterStickBtnShoot=3;	// on xbox controller, this is the right back trigger
	public static final int shooterStickStickWinch=1;	// on xbox controller, this is left stick
	public static final int shooterStickStickPitch=2;	// on xbox controller, this is right stick
	public static final int shooterStickBtnShooterEnable=3;
	public static final int shooterStickBtnShooterDisable=5;
	public static final int shooterStickBtnJogUp=7;
	public static final int shooterStickBtnJogDown=6;
	public static final int shooterStickBtnShooterCollect=1;
 
	public static final int motorCameraTilt = 6;
	
	//Autonomous constants
	public static final double autonLeftBias = .95;
	public static final double autonRightBias = 1.05;
	public static final double autonDefaultPowerLevel = -.65;
	public static final double autonHighPowerLevel = -.8;
	public static final double autonLowPowerLevel = -.55;
	public static final double autonRunTime = 2.5;
	
	//vision system enable
	public static final int visionSystemEnable = -1;
	
	//vision system camera bias
	public static final double shooterServoCorrection = 0;
	
	//vision servo camera controls
	public static final int shooterTiltServo = -1;
	public static final int shooterAngleServo = -1;

	public RobotMap() 
	{
		
	}

}
