package org.usfirst.frc.team1277.robot.commands;

import org.usfirst.frc.team1277.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class LiftUp extends Command {
	
	private final double LIFT_SPEED = 0.6;
	
	public LiftUp() {
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
		Robot.lift.liftHold();
	}

	protected void interrupted() {
		Robot.lift.liftHold();
	}	
}