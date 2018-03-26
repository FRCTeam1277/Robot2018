package org.usfirst.frc.team1277.robot.commands;

import org.usfirst.frc.team1277.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ClawPullCubeIn extends Command {
	
	private final double PULL_SPEED = 0.6, HOLD_SPEED = 0.1;

    public ClawPullCubeIn() {
        requires(Robot.claw);
    }

    protected void initialize() {
    	
    }

    protected void execute() {
    	Robot.claw.dirveWheels(PULL_SPEED, PULL_SPEED);
    }

    protected boolean isFinished() {
        return Robot.claw.holdingCube();
    }

    protected void end() {
    	Robot.claw.dirveWheels(HOLD_SPEED, HOLD_SPEED);
    }

    protected void interrupted() {
    	Robot.claw.dirveWheels(HOLD_SPEED, HOLD_SPEED);
    }
}
