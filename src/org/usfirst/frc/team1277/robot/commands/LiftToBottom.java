package org.usfirst.frc.team1277.robot.commands;

import org.usfirst.frc.team1277.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class LiftToBottom extends Command {
	
	private final double BOTTOM_HEIGHT = 0; //Counts (4096 per Revolution)

    public LiftToBottom() {
		requires(Robot.lift);
	}
	
	@Override
	protected boolean isFinished(){
		return false;
	}

	protected void execute(){
		Robot.lift.liftToPosition(BOTTOM_HEIGHT);
	}
	
	protected void end() {
		
	}
	
	protected void interrupted() {
		
	}
}
