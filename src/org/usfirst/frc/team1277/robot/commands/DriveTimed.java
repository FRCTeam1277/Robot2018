package org.usfirst.frc.team1277.robot.commands;

import org.usfirst.frc.team1277.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveTimed extends Command {

	private final double COUNTS_PER_SECOND = 50;
	private double speedX, speedY, count, endCount;

    public DriveTimed(double speedX, double speedY, double seconds) {
    	requires(Robot.driveTrain);
    	this.speedX = speedX;
    	this.speedY = speedY;
    	endCount = seconds * COUNTS_PER_SECOND;
    }

    protected void initialize() {
    	count = 0;
    }

    protected void execute() {
    	
    	//Drive
    	Robot.driveTrain.drive(speedY, speedX, 0, false, false);
    	
    	count --;
    }

    protected boolean isFinished() {
        return (count <= endCount);
    }

    protected void end() {
    	Robot.driveTrain.drive(0, 0, 0, true, false);
    }

    protected void interrupted() {
    	Robot.driveTrain.drive(0, 0, 0, true, false);
    }
}
