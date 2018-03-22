package org.usfirst.frc.team1277.robot.commands;

import org.usfirst.frc.team1277.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class GrabCube extends Command {
	
	public GrabCube() {
		requires(Robot.claw);
	}
	
	protected void initialize() {
		Robot.claw.stopClaw();
	}

	protected void execute() {
		Robot.claw.closeClaw();
	}
	
	@Override
	protected boolean isFinished() {
		return Robot.claw.gotStopped();
	}
	
	protected void end() {
		Robot.claw.stopClaw();
	}

	protected void interrupted() {
		Robot.claw.stopClaw();
	}
}
