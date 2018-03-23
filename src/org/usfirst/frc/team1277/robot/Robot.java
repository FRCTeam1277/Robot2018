/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team1277.robot;

import org.usfirst.frc.team1277.robot.autosequences.AutoScoreOnScaleFromLeft;
import org.usfirst.frc.team1277.robot.autosequences.AutoScoreOnScaleFromRight;
import org.usfirst.frc.team1277.robot.autosequences.AutoScoreOnSwitchFromCenter;
import org.usfirst.frc.team1277.robot.autosequences.AutoScoreOnSwitchFromLeft;
import org.usfirst.frc.team1277.robot.autosequences.AutoScoreOnSwitchFromRight;
import org.usfirst.frc.team1277.robot.autosequences.DriveFoward;
import org.usfirst.frc.team1277.robot.autosequences.DriveToAutoLine;
import org.usfirst.frc.team1277.robot.autosequences.DriveToSwitchLeft;
import org.usfirst.frc.team1277.robot.commands.DriveTimed;
import org.usfirst.frc.team1277.robot.subsystems.Claw;
import org.usfirst.frc.team1277.robot.subsystems.ClawRotator;
import org.usfirst.frc.team1277.robot.subsystems.DriveTrain;
import org.usfirst.frc.team1277.robot.subsystems.Lift;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends TimedRobot {
	
	public static OI oi;
	public static DriveTrain driveTrain;
	public static Claw claw;
	public static ClawRotator clawRotator;
	public static Lift lift;

	Command autonomousCommand;
	SendableChooser<Command> chooser = new SendableChooser<>();

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		//    private double MOVE_P_GAIN = 0.00015, MOVE_I_GAIN = 0.0, MOVE_D_GAIN = 0.001, MOVE_F_GAIN = 0.0, TOLLERANCE_COUNTS = 512; //4096 counts per wheel revolution, final

    	NetworkTableInstance instance = NetworkTableInstance.getDefault();
    	NetworkTable gains = instance.getTable("testing");
    	gains.getEntry("MOVE_P_GAIN").setDouble(0.00015);
    	gains.getEntry("MOVE_I_GAIN").setDouble(0.0);
    	gains.getEntry("MOVE_D_GAIN").setDouble(0.001);
    	gains.getEntry("MOVE_F_GAIN").setDouble(0.0);
		
    	
		RobotMap.init();
		claw = new Claw();
		clawRotator = new ClawRotator();
		lift = new Lift();
		oi = new OI();
		driveTrain = new DriveTrain();
		driveTrain.driveTrainInit();
		OI.getAhrs().zeroYaw();
		
		//Start autonomous
		chooser.addDefault("Drive To Auto Line", new DriveToAutoLine());
		chooser.addObject("Drive Foward", new DriveFoward());
		chooser.addObject("Timed Drive", new DriveTimed(0, 0.5, 2));
		chooser.addObject("Drive To Switch Left", new DriveToSwitchLeft());
		
		chooser.addObject("Score On Scale From Left", new AutoScoreOnScaleFromLeft());
		chooser.addObject("Score On Switch From Left", new AutoScoreOnSwitchFromLeft());
		
		chooser.addObject("Score On Switch From Center", new AutoScoreOnSwitchFromCenter());
		
		chooser.addObject("Score On Scale From Right", new AutoScoreOnScaleFromRight());
		chooser.addObject("Score On Switch From Right", new AutoScoreOnSwitchFromRight());
		
		SmartDashboard.putData("Auto mode", chooser);
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
		OI.getAhrs().zeroYaw();
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		autonomousCommand = chooser.getSelected();
		OI.getAhrs().zeroYaw();
		driveTrain.restartPositionTracking(true);
		
		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */

		// schedule the autonomous command (example)
		if (autonomousCommand != null) {
			autonomousCommand.start();
		}
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null) {
			autonomousCommand.cancel();
		}
		
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
