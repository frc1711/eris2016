package org.usfirst.frc.team1711.vision;

import org.usfirst.frc.team1711.robot.RobotMap;
import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ImageType;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;

public class VisionSystem2 
{
	//Object reference construction
	CameraServer server;
	Image frame;
	Servo angleServo;
	Servo tiltServo;
	
	//Variable construction
	int session;
	
	public void init()
	{
		//Create image
		frame = NIVision.imaqCreateImage(ImageType.IMAGE_RGB, 0);
		System.out.println("Created image frame");
			
		//Initialize the camera
		session = NIVision.IMAQdxOpenCamera("cam0", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		System.out.println("Camera initalized");
			
		//I don't know what this does
		NIVision.IMAQdxConfigureGrab(session);
			
		//Initialize servo objects if they exist
		if(RobotMap.shooterAngleServo != -1)
		{
			angleServo = new Servo(RobotMap.shooterAngleServo); 
		}
		
		if(RobotMap.shooterTiltServo != -1)
		{
			tiltServo = new Servo(RobotMap.shooterTiltServo); 
		}
	}
	
	//CENTER ANGLE SERVO
	
	public void vision() 
	{
		//Sends the frames from the camera to the server, which is then displayed on the dashboard
		NIVision.IMAQdxGrab(session, frame, 1);
		CameraServer.getInstance().setImage(frame);
	}
		
	public void shooterAngle(Joystick shooterStick)
	{
		//Sets the current servo angle for subsequent adjustments
		double servoAngle = angleServo.getAngle();
			
		//Angles the camera right
		if (shooterStick.getRawButton(RobotMap.cameraAngleRightButton)) 
		{
			angleServo.setAngle(servoAngle - 1);		
		}
			
		//Angles the camera left
		if (shooterStick.getRawButton(RobotMap.cameraAngleLeftButton)) 
		{
			angleServo.setAngle(servoAngle + 1);
		}
					
		//Centers the camera
		if (shooterStick.getRawButton(RobotMap.cameraCenterViewButton)) 
		{
			angleServo.setAngle(90);
		}
	}
		
	public void shooterTrack(double potAngle) //Correction should be set in RobotMap
	{
		//Sets the tilt camera to the same angle as the shooter
		//Alternatively, we could use a scaling factor -- it needs testing to determine which is better
		//3.003 = .333 (range of pot) * 100 (tiltServo.set takes a number between 0 and 1)
		tiltServo.set(potAngle*3.003+RobotMap.shooterServoCorrection);
	}
		
	public void throttleTilt(Joystick shooterStick)
	{
		//Tilts the camera via absolute control on the throttle
		tiltServo.set(shooterStick.getThrottle());
			
		//Centers the tilt camera if the centering button is pressed
		if(shooterStick.getRawButton(1)) //THIS NEEDS TO BE CHANGED TO ROBOTMAP IF IMPLIMENTED
		{
			tiltServo.set(.5);
		}
	}	
}
