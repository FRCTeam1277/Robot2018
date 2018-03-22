package org.usfirst.frc.team1277.robot.commands;

import org.usfirst.frc.team1277.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class RotateTo extends Command {
	
	private double angle, count;
	private final double INITIAL_COUNT = 5, PI = Math.PI, ERROR = 0.5; //Degrees
	private final double  LOWEST_SPEED = 0.1, LOWEST_CONTROL = 0.005;

    public RotateTo(double angle) {
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
    	
    	rotate = Robot.driveTrain.getRobotRotate();
    	if (rotate > LOWEST_CONTROL && rotate < LOWEST_SPEED) rotate = LOWEST_SPEED;
    	else if (rotate < -LOWEST_CONTROL && rotate > -LOWEST_SPEED) rotate = -LOWEST_SPEED;
    	Robot.driveTrain.drive(0, 0, rotate, true);
    	if (Math.abs(Robot.driveTrain.getDesiredDirection() - Robot.driveTrain.getDirection()) <= ERROR * PI / 180f) count--;
    	else count = INITIAL_COUNT;
    	SmartDashboard.putNumber("Angle", angle);
    	SmartDashboard.putNumber("Error", Math.abs(Robot.driveTrain.getDesiredDirection() - Robot.driveTrain.getDirection()));
    	SmartDashboard.putNumber("Allowed Error", ERROR * PI / 180f);
    	SmartDashboard.putNumber("Count", count);
    }

    protected boolean isFinished() {
    	if (count <= 0) return true;
        return false;
    }

    protected void end() {
    	Robot.driveTrain.drive(0, 0, 0, true);
    }

    protected void interrupted() {
    	Robot.driveTrain.drive(0, 0, 0, true);
    }
}
