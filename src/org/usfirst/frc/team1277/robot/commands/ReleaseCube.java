package org.usfirst.frc.team1277.robot.commands;

import org.usfirst.frc.team1277.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ReleaseCube extends Command {
	
	public ReleaseCube() {
		requires(Robot.claw);
	}

	protected void initialize() {
		Robot.claw.stopClaw();
	}

	protected void execute() {
		Robot.claw.openClaw();
	}
	
	@Override
	protected boolean isFinished() {
		return Robot.claw.reachedClawLimit();
	}
	
	protected void end() {
		Robot.claw.stopClaw();
	}

	protected void interrupted() {
		Robot.claw.stopClaw();
	}
}
