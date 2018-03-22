package org.usfirst.frc.team1277.robot.commands;

import org.usfirst.frc.team1277.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class LiftDown extends Command {

	
	private final double LIFT_SPEED = -0.2;
	
	public LiftDown() {
		requires(Robot.lift);
	}
	
	protected void initialize() {
		
	}

	protected void execute() {
		Robot.lift.lift(LIFT_SPEED);
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}
	
	protected void end() {
		Robot.lift.liftHold();
	}

	protected void interrupted() {
		Robot.lift.liftHold();
	}	
}
