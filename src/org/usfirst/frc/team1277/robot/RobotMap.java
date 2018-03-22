/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team1277.robot;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Spark;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	
	//DriveTrain
	public static TalonSRX driveTrainFrontLeft;
	public static TalonSRX driveTrainFrontRight;
	public static TalonSRX driveTrainRearLeft;
	public static TalonSRX driveTrainRearRight;
	
	//Lift
	public static TalonSRX liftMotor1;
	public static TalonSRX liftMotor2;
	public static DigitalInput liftLowerLimit; 
	public static Servo liftGearShifter;
	public static Relay liftLocker;
	
	//Claw
	public static Spark clawMotor;
	public static Encoder clawEncoder;
	public static DigitalInput clawLimit;
	
	//Claw Retractor
	public static Spark rotateClawMotor;
	public static Encoder rotateClawEncoder;
	public static Relay rotateClawStopper;
	
		
	public static void init(){
			
		//DriveTrain
		driveTrainFrontLeft = new TalonSRX(3);
		driveTrainFrontRight = new TalonSRX(2);
		driveTrainRearLeft = new TalonSRX(4);
		driveTrainRearRight = new TalonSRX(1);
		driveTrainFrontLeft.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.PIDLoopIdx, Constants.timeoutMs);
		driveTrainFrontRight.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.PIDLoopIdx, Constants.timeoutMs);
		driveTrainRearLeft.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.PIDLoopIdx, Constants.timeoutMs);
		driveTrainRearRight.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.PIDLoopIdx, Constants.timeoutMs);
		driveTrainFrontLeft.setSensorPhase(Constants.driveSensorPhase);
		driveTrainFrontRight.setSensorPhase(Constants.driveSensorPhase);
		driveTrainRearLeft.setSensorPhase(Constants.driveSensorPhase);
		driveTrainRearRight.setSensorPhase(Constants.driveSensorPhase);
		
		//Lift
		liftMotor1 = new TalonSRX(6);
		liftMotor2 = new TalonSRX(5);
		liftMotor2.set(com.ctre.phoenix.motorcontrol.ControlMode.Follower, liftMotor1.getDeviceID());
		liftMotor1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.PIDLoopIdx, Constants.timeoutMs);
		liftMotor1.setSelectedSensorPosition(0, Constants.PIDLoopIdx, Constants.timeoutMs);
		liftMotor1.setInverted(Constants.liftMotorInvert);
		liftMotor2.setInverted(Constants.liftMotorInvert);
		liftMotor1.setSensorPhase(Constants.liftSensorPhase);
		liftMotor2.setSensorPhase(Constants.liftSensorPhase);
		liftLowerLimit = new DigitalInput(4);
		liftGearShifter = new Servo(2);
		liftLocker = new Relay(1);
		
		//Claw
		clawMotor = new Spark(0);
		clawEncoder = new Encoder(0, 1);
		clawLimit = new DigitalInput(5);

		//Claw Retractor
		rotateClawMotor = new Spark(1);
		rotateClawEncoder = new Encoder(2, 3);
		rotateClawStopper = new Relay(0);
	}
}
