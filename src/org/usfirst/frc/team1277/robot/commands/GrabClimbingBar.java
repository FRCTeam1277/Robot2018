package org.usfirst.frc.team1277.robot.commands;

import org.usfirst.frc.team1277.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class GrabClimbingBar extends Command {
	

	private final double BAR_HEIGHT = 33000, ERROR = 512; //Counts (4096 per Revolution)
	
	public GrabClimbingBar() {
		requires(Robot.lift);
	}
	
	protected void initialize() {
		
	}

	protected void execute() {
		Robot.lift.liftToPosition(BAR_HEIGHT);
	}
	
	@Override
	protected boolean isFinished() {
		return (Math.abs(Robot.lift.getHeight() - BAR_HEIGHT) <= ERROR);
	}
	
	protected void end() {

	}

	protected void interrupted() {

	}	
}
