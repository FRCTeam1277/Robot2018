package org.usfirst.frc.team1277.robot.commands;

import org.usfirst.frc.team1277.robot.OI;
import org.usfirst.frc.team1277.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Drive extends Command {
	
	private final double MOVE_SPEED = 1.0, ROTATE_SPEED = 0.7, MOVE_EXPONENT = 1.0, ROTATE_EXPONENT = 1.5;
    private final double MOVE_DEADZONE = 0.15, ROTATE_DEADZONE = 0.1;
    private final double PARALLEL_MOVE_LOWEST_SPEED = 0.1, PERPENDICULAR_MOVE_LOWEST_SPEED = 0.3, ROTATE_LOWEST_SPEED = 0.1;

	public Drive() {
        requires(Robot.driveTrain);
    }

    protected void initialize() {
    	
    }

    protected void execute() {
    	double parallelMove = -OI.getJoystick().getRawAxis(1), 
    			perpendicularMove = -OI.getJoystick().getRawAxis(0),
    			rotate = OI.getJoystick().getRawAxis(2);
    	
    	//Debug
    	SmartDashboard.putNumber("Foward Control", parallelMove);
    	SmartDashboard.putNumber("Sideways Control", perpendicularMove);
    	SmartDashboard.putNumber("Turn Control", rotate);

    	//Exponential Adjustment
    	parallelMove *= Math.pow(Math.abs(parallelMove), MOVE_EXPONENT - 1.0f);
    	perpendicularMove *= Math.pow(Math.abs(perpendicularMove), MOVE_EXPONENT - 1.0f);
    	rotate *= Math.pow(Math.abs(rotate), ROTATE_EXPONENT - 1.0f);
    	
    	//Adjust Parallel Move
    	if (parallelMove >= MOVE_DEADZONE || parallelMove <= -MOVE_DEADZONE) parallelMove = Math.signum(parallelMove) * 
    			(Math.pow(((Math.abs(parallelMove) - MOVE_DEADZONE) / (1 - MOVE_DEADZONE)), MOVE_EXPONENT) * 
    			(MOVE_SPEED - PARALLEL_MOVE_LOWEST_SPEED) + PARALLEL_MOVE_LOWEST_SPEED);
    	else parallelMove = 0.0f;
    	
    	//Adjust Perpendicular Move
    	if (perpendicularMove >= MOVE_DEADZONE || perpendicularMove <= -MOVE_DEADZONE) perpendicularMove = Math.signum(perpendicularMove) * 
    			(Math.pow(((Math.abs(perpendicularMove) - MOVE_DEADZONE) / (1 - MOVE_DEADZONE)), MOVE_EXPONENT) * 
    			(MOVE_SPEED - PERPENDICULAR_MOVE_LOWEST_SPEED) + PERPENDICULAR_MOVE_LOWEST_SPEED);
    	else perpendicularMove = 0.0f;
    	
    	//Adjust Rotate
    	if (rotate >= ROTATE_DEADZONE || rotate <= -ROTATE_DEADZONE) rotate = Math.signum(rotate) * 
    			(Math.pow(((Math.abs(rotate) - ROTATE_DEADZONE) / (1 - ROTATE_DEADZONE)), ROTATE_EXPONENT) * 
    			(ROTATE_SPEED - ROTATE_LOWEST_SPEED) + ROTATE_LOWEST_SPEED);
    	else rotate = 0.0f;
    	
    	//Drive
    	Robot.driveTrain.drive(parallelMove, perpendicularMove, rotate, OI.robotOrientedDrive.get(), false);
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    	Robot.driveTrain.drive(0, 0, 0, true, false);
    }

    protected void interrupted() {
    	Robot.driveTrain.drive(0, 0, 0, true, false);
    }
}
