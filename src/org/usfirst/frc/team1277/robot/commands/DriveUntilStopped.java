package org.usfirst.frc.team1277.robot.commands;

import org.usfirst.frc.team1277.robot.OI;
import org.usfirst.frc.team1277.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveUntilStopped extends Command {
	
	private double speedX, speedY, count;
	private boolean moving, previouslyMoving, counting;
	private final double INITIAL_COUNT = 1;
	private final double  LOWEST_SPEED = 0.1, LOWEST_CONTROL = 0.005;
	

    public DriveUntilStopped(double speedX, double speedY) {
    	requires(Robot.driveTrain);
    	this.speedX = speedX;
    	this.speedY = speedY;
    }

    protected void initialize() {
    	moving = false;
    	previouslyMoving = false;
    	counting = false;
    	count = INITIAL_COUNT;
    	if (speedX == 0) Robot.driveTrain.setXPosition(Robot.driveTrain.getRobotPositionX());//consider keeping previous set position
    	if (speedY == 0) Robot.driveTrain.setYPosition(Robot.driveTrain.getRobotPositionY());
    }

    protected void execute() {
    	double moveX, moveY, rotate;
    	
    	if (speedX == 0) moveX = Robot.driveTrain.getRobotDriveX();
    	else moveX = speedX;
    	if (speedY == 0) moveY = Robot.driveTrain.getRobotDriveY();
    	else moveY = speedY;
    	rotate = Robot.driveTrain.getRobotRotate();
    	if (moveX > LOWEST_CONTROL && moveX < LOWEST_SPEED) moveX = LOWEST_SPEED;
    	else if (moveX < -LOWEST_CONTROL && moveX > -LOWEST_SPEED) moveX = -LOWEST_SPEED;
    	if (moveY > LOWEST_CONTROL && moveY < LOWEST_SPEED) moveY = LOWEST_SPEED;
    	else if (moveY < -LOWEST_CONTROL && moveY > -LOWEST_SPEED) moveY = -LOWEST_SPEED;
    	if (rotate > LOWEST_CONTROL && rotate < LOWEST_SPEED) rotate = LOWEST_SPEED;
    	else if (rotate < -LOWEST_CONTROL && rotate > -LOWEST_SPEED) rotate = -LOWEST_SPEED;
    	Robot.driveTrain.drive(moveY, moveX, rotate, true);
    	
    	//Check status
    	previouslyMoving = moving;
    	moving = OI.ahrs.isMoving();
    	if (moving) counting = false;
    	else if (!moving && previouslyMoving) counting = true;
    	if (counting) count --;
    	else count = INITIAL_COUNT;
    }

    protected boolean isFinished() {
        return (count <= 0);
    }

    protected void end() {
    	Robot.driveTrain.drive(0, 0, 0, true);
    }

    protected void interrupted() {
    	Robot.driveTrain.drive(0, 0, 0, true);
    }
}
