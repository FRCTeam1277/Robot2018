package org.usfirst.frc.team1277.robot.commands;

import org.usfirst.frc.team1277.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DeployClaw extends Command {
	
	private final double PULL_SPEED = -0.5, LET_DOWN_SPEED = 0.0, RESIST_SPEED = -0.15;
	private final double SECONDARY_PULL_SPEED = -0.4, SECONDARY_LET_DOWN_SPEED = 0.0;
	private final double LET_DOWN_END_POINT = 10, RESIST_END_POINT = 70, FINAL_COUNT = 20;
	private double initialPosition, count;
	private boolean switched, counting;
	private boolean secondary = false;
	
	public DeployClaw() {
		requires(Robot.clawRotator);
	}
	
	protected void initialize() {
		initialPosition = Robot.clawRotator.getPosition();
		if (!secondary) Robot.clawRotator.rotate(PULL_SPEED);
		else Robot.clawRotator.rotate(SECONDARY_PULL_SPEED);
		Robot.clawRotator.openStopper();
		count = 0;
		counting = true;
		switched = false;
	}
	
	@Override
	protected boolean isFinished() {
		if (!secondary && Robot.clawRotator.getPosition() - initialPosition >= RESIST_END_POINT ||
				secondary && switched) {
			Robot.clawRotator.rotate(0);
			secondary = !secondary;
			return true;
		}
		return false;
	}
	
	protected void execute() {
		if (count >= FINAL_COUNT) {
			if (!secondary) Robot.clawRotator.rotate(LET_DOWN_SPEED);
			else Robot.clawRotator.rotate(SECONDARY_LET_DOWN_SPEED);
			counting = false;
			count = 0;
		}
		else if (Robot.clawRotator.getPosition() - initialPosition >= LET_DOWN_END_POINT) {
			if (!secondary) Robot.clawRotator.rotate(RESIST_SPEED);
			else Robot.clawRotator.rotate(0);
			if (!switched) {
				Robot.clawRotator.closeStopper();
				switched = true;
			}
		}
		if (counting) count++;
		SmartDashboard.putNumber("Position", Robot.clawRotator.getPosition() - initialPosition);
	}
	
	protected void end() {
		Robot.clawRotator.rotate(0);
	}

	protected void interrupted() {
		Robot.clawRotator.rotate(0);
	}
}
