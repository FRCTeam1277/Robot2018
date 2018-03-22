package org.usfirst.frc.team1277.robot.commands;

import org.usfirst.frc.team1277.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class LiftClimbingBar extends Command {

	
	private final double LIFT_SPEED = 0.9, HOLD_SPEED = 0.65;
	
	public LiftClimbingBar() {
		requires(Robot.lift);
	}
	
	protected void initialize() {
		
	}

	protected void execute() {
		if (Robot.lift.getHeight() >= Robot.lift.getMAX_HEIGHT()) Robot.lift.liftHold();
		else Robot.lift.lift(LIFT_SPEED);
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}
	
	protected void end() {
		Robot.lift.lift(HOLD_SPEED);
	}

	protected void interrupted() {
		Robot.lift.lift(HOLD_SPEED);
	}	

}
