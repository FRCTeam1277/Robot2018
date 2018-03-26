package org.usfirst.frc.team1277.robot.commands;

import org.usfirst.frc.team1277.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveRotateTo extends Command {
	
	private double angle, count;
	private final double INITIAL_COUNT = 5, PI = Math.PI, ERROR = 0.5; //Degrees
	private final double  LOWEST_SPEED = 0.1, LOWEST_CONTROL = 0.005;

    public DriveRotateTo(double angle) {
    	requires(Robot.driveTrain);
    	this.angle = angle;
    }

    protected void initialize() {
    	Robot.driveTrain.updateDirection();
    	Robot.driveTrain.setDesiredDirection(angle);
    	count = INITIAL_COUNT;
    }

    protected void execute() {
    	double rotate;
    	
    	//Get PID Output
    	rotate = Robot.driveTrain.getRobotRotate();
    	
    	//Adjust for Low Speeds
    	if (rotate > LOWEST_CONTROL && rotate < LOWEST_SPEED) rotate = LOWEST_SPEED;
    	else if (rotate < -LOWEST_CONTROL && rotate > -LOWEST_SPEED) rotate = -LOWEST_SPEED;
    	
    	//Drive
    	Robot.driveTrain.drive(0, 0, rotate, true, false);
    	
    	//Check Rotation
    	if (Math.abs(Robot.driveTrain.getDesiredDirection() - Robot.driveTrain.getDirection()) <= ERROR * PI / 180f) count--;
    	else count = INITIAL_COUNT;
    	
    	//Dashboard
    	SmartDashboard.putNumber("Angle", angle);
    	SmartDashboard.putNumber("Error", Math.abs(Robot.driveTrain.getDesiredDirection() - Robot.driveTrain.getDirection()));
    	SmartDashboard.putNumber("Allowed Error", ERROR * PI / 180f);
    	SmartDashboard.putNumber("Count", count);
    }

    protected boolean isFinished() {
    	return (count <= 0);
    }

    protected void end() {
    	Robot.driveTrain.drive(0, 0, 0, true, false);
    }

    protected void interrupted() {
    	Robot.driveTrain.drive(0, 0, 0, true, false);
    }
}
