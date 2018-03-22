package org.usfirst.frc.team1277.robot.commands;

import org.usfirst.frc.team1277.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class RetractClawDown extends Command {

	private final double RAISE_SPEED = 0.5, RESIST_SPEED = 0.1;
	private final double RESIST_END_POINT = 70, RAISE_TIME = 20;
	private double initialPosition, counter;
	
	public RetractClawDown() {
		requires(Robot.clawRotator);
	}
	
	protected void initialize() {
		initialPosition = Robot.clawRotator.getPosition();
		counter = 0;
		Robot.clawRotator.rotate(RAISE_SPEED);
		Robot.clawRotator.openStopper();
	}
	
	protected void execute() {
		if (counter >= RAISE_TIME) {
			Robot.clawRotator.rotate(RESIST_SPEED);
		}
		counter++;
	}
	
	@Override
	protected boolean isFinished() {
		if (Robot.clawRotator.getPosition() - initialPosition >= RESIST_END_POINT) {
			Robot.clawRotator.closeStopper();
			return true;
		}
		return false;
	}
	
	protected void end() {
		Robot.clawRotator.rotate(0);
	}

	protected void interrupted() {
		Robot.clawRotator.rotate(0);
	}
}
