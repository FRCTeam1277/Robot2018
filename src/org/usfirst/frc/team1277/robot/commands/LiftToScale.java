package org.usfirst.frc.team1277.robot.commands;

import org.usfirst.frc.team1277.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class LiftToScale extends Command {
	
	private final double SCALE_HEIGHT = 35000, ERROR = 512; //Counts (4096 per Revolution, 36000 at Top)
	
	public LiftToScale() {
		requires(Robot.lift);
	}
	
	@Override
	protected boolean isFinished(){
		return (Math.abs(Robot.lift.getHeight() - SCALE_HEIGHT) <= ERROR);
	}

	protected void execute(){
		Robot.lift.liftToPosition(SCALE_HEIGHT);
	}
	
	protected void end() {
		
	}
	
	protected void interrupted() {
		
	}

}
