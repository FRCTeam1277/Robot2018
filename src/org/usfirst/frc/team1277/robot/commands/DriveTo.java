package org.usfirst.frc.team1277.robot.commands;

import org.usfirst.frc.team1277.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveTo extends Command {
	
	private double displacementX, displacementY, initialPositionX, initialPositionY, count;
	private final double INITIAL_COUNT = 20, COUNTS_PER_INCH = 869, ERROR = 2048; //Counts (4096 per revolution)
	//private final double  LOWEST_SPEED = 0.1, LOWEST_CONTROL = 0.02;

    public DriveTo(double displacementX, double displacementY) {
    	requires(Robot.driveTrain);
    	this.displacementX = -displacementX * COUNTS_PER_INCH; //Look into why it is negative
    	this.displacementY = displacementY * COUNTS_PER_INCH;
    }

    protected void initialize() {
    	initialPositionX = Robot.driveTrain.getRobotPositionX();
    	initialPositionY = Robot.driveTrain.getRobotPositionY();
    	Robot.driveTrain.setXPosition(displacementX + initialPositionX);
    	Robot.driveTrain.setYPosition(displacementY + initialPositionY);
    	count = INITIAL_COUNT;
    }

    protected void execute() {
    	double moveX, moveY, rotate;
    	
    	//Get PID Output for Drive Controls
    	moveX = Robot.driveTrain.getRobotDriveX();
    	moveY = Robot.driveTrain.getRobotDriveY();
    	rotate = Robot.driveTrain.getRobotRotate();
    	/*
    	//Adjust for Low Speeds
    	if (moveX > LOWEST_CONTROL && moveX < LOWEST_SPEED) moveX = LOWEST_SPEED;
    	else if (moveX < -LOWEST_CONTROL && moveX > -LOWEST_SPEED) moveX = -LOWEST_SPEED;
    	if (moveY > LOWEST_CONTROL && moveY < LOWEST_SPEED) moveY = LOWEST_SPEED;
    	else if (moveY < -LOWEST_CONTROL && moveY > -LOWEST_SPEED) moveY = -LOWEST_SPEED;
    	if (rotate > LOWEST_CONTROL && rotate < LOWEST_SPEED) rotate = LOWEST_SPEED;
    	else if (rotate < -LOWEST_CONTROL && rotate > -LOWEST_SPEED) rotate = -LOWEST_SPEED;*/
    	
    	//Drive
    	Robot.driveTrain.drive(moveY, moveX, rotate, true, true);
    	
    	//Check Position
    	if (Math.abs(Robot.driveTrain.getDesiredRobotPositionX() - Robot.driveTrain.getRobotPositionX()) <= ERROR &&
    			Math.abs(Robot.driveTrain.getDesiredRobotPositionY() - Robot.driveTrain.getRobotPositionY()) <= ERROR) count--;
    	else count = INITIAL_COUNT;
    	
    	//Dashboard
    	SmartDashboard.putNumber("PositionX", Robot.driveTrain.getRobotPositionX());
    	SmartDashboard.putNumber("PositionY", Robot.driveTrain.getRobotPositionY());
    	SmartDashboard.putNumber("moveX", moveX);
    	SmartDashboard.putNumber("moveY", moveY);
    }

    protected boolean isFinished() {
    	return (count <= 0);
    }

    protected void end() {
    	Robot.driveTrain.drive(0, 0, 0, true, true);
    }

    protected void interrupted() {
    	Robot.driveTrain.drive(0, 0, 0, true, true);
    }
}
