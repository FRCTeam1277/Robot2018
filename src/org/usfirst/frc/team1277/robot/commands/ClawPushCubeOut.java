package org.usfirst.frc.team1277.robot.commands;

import org.usfirst.frc.team1277.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ClawPushCubeOut extends Command {

	private final double PUSH_SPEED = -0.3;
	private final double PUSH_TIME = 80;
	private int counter;

    public ClawPushCubeOut() {
        requires(Robot.claw);
    }

    protected void initialize() {
    	counter = 0;
    }

    protected void execute() {
    	Robot.claw.dirveWheels(PUSH_SPEED, PUSH_SPEED);
    	counter++;
    }

    protected boolean isFinished() {
        return (counter >= PUSH_TIME);
    }

    protected void end() {
    	Robot.claw.dirveWheels(0, 0);
    }

    protected void interrupted() {
    	Robot.claw.dirveWheels(0, 0);
    }
}
