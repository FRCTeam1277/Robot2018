package org.usfirst.frc.team1277.robot.commands;

import org.usfirst.frc.team1277.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class LiftToSwitch extends Command {
	
	private final double SWITCH_HEIGHT = 15000, ERROR = 512; //Counts (4096 per Revolution, 36000 at Top)
	
	public LiftToSwitch() {
		requires(Robot.lift);
	}
	
	@Override
	protected boolean isFinished(){
		return (Math.abs(Robot.lift.getHeight() - SWITCH_HEIGHT) <= ERROR);
	}

	protected void execute(){
		Robot.lift.liftToPosition(SWITCH_HEIGHT);
	}
	
	protected void end() {
		
	}
	
	protected void interrupted() {
		
	}
}
