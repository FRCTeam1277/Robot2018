package org.usfirst.frc.team1277.robot.commands;

import org.usfirst.frc.team1277.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class LiftClimb extends Command {

	
	private final double LIFT_SPEED = -0.9, HOLD_SPEED = -0.15;
	private final double CLIMBED_HEIGHT = 8500;
	
	public LiftClimb() {
		requires(Robot.lift);
	}
	
	protected void initialize() {
		
	}

	protected void execute() {
		if (Robot.lift.getHeight() <= CLIMBED_HEIGHT) {
			Robot.lift.lock();
			Robot.lift.lift(HOLD_SPEED);
		}
		else Robot.lift.lift(LIFT_SPEED);
	}
	
	@Override
	protected boolean isFinished() {
		return (Robot.lift.getHeight() <= CLIMBED_HEIGHT);
	}
	
	protected void end() {
		if (Robot.lift.getHeight() <= CLIMBED_HEIGHT) Robot.lift.lock();
		Robot.lift.lift(HOLD_SPEED);
	}

	protected void interrupted() {
		if (Robot.lift.getHeight() <= CLIMBED_HEIGHT) Robot.lift.lock();
		Robot.lift.lift(HOLD_SPEED);
	}	
}
