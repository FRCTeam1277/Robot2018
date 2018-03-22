package org.usfirst.frc.team1277.robot.commands;

import org.usfirst.frc.team1277.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class RotatorToggle extends Command {

	private final double HOLD_SPEED = -0.4, RESIST_FULL_SPEED = -0.15, RESIST_EMPTY_SPEED = -0.01;
	private final double HOLD_POSITION = -90, SENSITIVITY = 0.1, MAX_SPEED = 0.4;
	private double initialPosition;
	private boolean lifted, previousButton;

    public RotatorToggle() {
    	requires(Robot.clawRotator);
    }

    protected void initialize() {
    	initialPosition = 0;
    	lifted = true;
    	previousButton = Robot.oi.getJoystick().getRawButton(10);
    	SmartDashboard.putBoolean("lifted", lifted);
    }

    protected void execute() {
    	double speed;
    	
    	if (Robot.oi.getJoystick().getRawButton(10) && !previousButton) lifted = !lifted;
    	SmartDashboard.putBoolean("lifted", lifted);
    	previousButton = Robot.oi.getJoystick().getRawButton(10);
    	if (lifted) {
    		speed = HOLD_SPEED + SENSITIVITY * (HOLD_POSITION - (Robot.clawRotator.getPosition() - initialPosition));
    		if (Math.abs(speed - HOLD_SPEED) >= MAX_SPEED) speed = (speed - HOLD_SPEED) / Math.abs(speed - HOLD_SPEED) * MAX_SPEED + HOLD_SPEED;
    		Robot.clawRotator.rotate(speed);
    		SmartDashboard.putNumber("Position", Robot.clawRotator.getPosition() - initialPosition);
    		SmartDashboard.putNumber("Speed", speed);
    	}
    	else {
    		Robot.clawRotator.rotate(RESIST_EMPTY_SPEED);
    	}
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    	Robot.clawRotator.rotate(RESIST_EMPTY_SPEED);
    }

    protected void interrupted() {
    	Robot.clawRotator.rotate(RESIST_EMPTY_SPEED);
    }
}
