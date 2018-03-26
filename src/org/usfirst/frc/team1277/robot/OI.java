/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team1277.robot;

import org.usfirst.frc.team1277.robot.commands.ClawPullCubeIn;
import org.usfirst.frc.team1277.robot.commands.ClawPushCubeOut;
import org.usfirst.frc.team1277.robot.commands.ClawThrowCubeOut;
import org.usfirst.frc.team1277.robot.commands.LiftClimb;
import org.usfirst.frc.team1277.robot.commands.LiftClimbingBar;
import org.usfirst.frc.team1277.robot.commands.LiftDown;
import org.usfirst.frc.team1277.robot.commands.LiftShiftGear;
import org.usfirst.frc.team1277.robot.commands.LiftToBottom;
import org.usfirst.frc.team1277.robot.commands.LiftToScale;
import org.usfirst.frc.team1277.robot.commands.LiftToSwitch;
import org.usfirst.frc.team1277.robot.commands.LiftUp;
import org.usfirst.frc.team1277.robot.commands.RotatorRetract;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	public static Joystick joystick = new Joystick(0);
	public static Joystick joystickSecondary = new Joystick(1);
	public static AHRS ahrs;
	
	//Drive buttons
	public static JoystickButton robotOrientedDrive = new JoystickButton(joystick, 10);
	
	//Claw buttons
	public static JoystickButton clawPullIn = new JoystickButton(joystick, 1); 
	public static JoystickButton clawPushOut = new JoystickButton(joystick, 3);
	public static JoystickButton clawThrowOut = new JoystickButton(joystick, 5);
	
	//ClawRotator buttons
	public static JoystickButton rotatorRetract = new JoystickButton(joystickSecondary, 1);
	public static JoystickButton rotatorToggle = new JoystickButton(joystick, 2);
	
	//Lift buttons
	public static JoystickButton liftUp = new JoystickButton(joystick, 6);
	public static JoystickButton liftDown = new JoystickButton(joystick, 4);
	public static JoystickButton liftDeployClimber = new JoystickButton(joystickSecondary, 6);
	public static JoystickButton liftClimb = new JoystickButton(joystickSecondary, 4);
	public static JoystickButton liftToSwitch = new JoystickButton(joystick, 7);
	public static JoystickButton liftToScale = new JoystickButton(joystick, 8);
	public static JoystickButton liftToBottom = new JoystickButton(joystick, 9);
	public static JoystickButton liftShiftGear = new JoystickButton(joystickSecondary, 11);
	
	public OI(){
		
		try {
			/***********************************************************************
			 * navX-MXP:
			 * - Communication via RoboRIO MXP (SPI, I2C, TTL UART) and USB.            
			 * - See http://navx-mxp.kauailabs.com/guidance/selecting-an-interface.
			 * 
			 * navX-Micro:
			 * - Communication via I2C (RoboRIO MXP or Onboard) and USB.
			 * - See http://navx-micro.kauailabs.com/guidance/selecting-an-interface.
			 * 
			 * Multiple navX-model devices on a single robot are supported.
			 ************************************************************************/
            ahrs = new AHRS(SPI.Port.kMXP);
        } catch (RuntimeException ex ) {
            DriverStation.reportError("Error instantiating navX MXP:  " + ex.getMessage(), true);
        };

        //Claw
        clawPullIn.whileHeld(new ClawPullCubeIn());
        clawPushOut.whenPressed(new ClawPushCubeOut());
        clawThrowOut.whenPressed(new ClawThrowCubeOut());
        
        //ClawRotator
        rotatorRetract.whenPressed(new RotatorRetract());
        
        //Lift
        liftUp.whileHeld(new LiftUp());
        liftDown.whileHeld(new LiftDown());
        liftDeployClimber.whileHeld(new LiftClimbingBar());
        liftClimb.whileHeld(new LiftClimb());
        liftToSwitch.whenPressed(new LiftToSwitch());
        liftToScale.whenPressed(new LiftToScale());
        liftToBottom.whenPressed(new LiftToBottom());
        liftShiftGear.whenPressed(new LiftShiftGear());
	}

	public static AHRS getAhrs() {
		return ahrs;
	}
	
	public static Joystick getJoystick() {
		return joystick;
	}
}
