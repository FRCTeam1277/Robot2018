package org.usfirst.frc.team1277.robot.commands;

import org.usfirst.frc.team1277.robot.OI;
import org.usfirst.frc.team1277.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class RotatorToggle extends Command {

	private final double HOLD_SPEED = -0.4, RESIST_FULL_SPEED = -0.15, RESIST_EMPTY_SPEED = -0.01;
	private final double HOLD_POSITION = 35, SENSITIVITY = 0.1, MAX_SPEED = 0.5; //Counts (497 per Revolution)
	private double initialPosition;
	private boolean lifted, previousButton;

    public RotatorToggle() {
    	requires(Robot.clawRotator);
    }

    protected void initialize() {
    	initialPosition = 0;
    	lifted = false;
    	previousButton = OI.rotatorToggle.get();
    }

    protected void execute() {
    	double speed;
    	
    	//Toggle When Button is Pressed
    	if (OI.rotatorToggle.get() && !previousButton) lifted = !lifted;
    	previousButton = OI.rotatorToggle.get();
    	SmartDashboard.putBoolean("Lifted Claw", lifted);
    	
    	//Determine Speed
    	if (lifted) {
    		speed = HOLD_SPEED + SENSITIVITY * (HOLD_POSITION - (Robot.clawRotator.getPosition() - initialPosition));
    		if (Math.abs(speed - HOLD_SPEED) >= MAX_SPEED) speed = (speed - HOLD_SPEED) / Math.abs(speed - HOLD_SPEED) * MAX_SPEED + HOLD_SPEED;
    	}
    	else {
    		if (Robot.claw.holdingCube()) speed = RESIST_FULL_SPEED;
    		else speed = RESIST_EMPTY_SPEED;
    	}
    	
    	//Rotate Claw
    	Robot.clawRotator.rotate(speed);
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
