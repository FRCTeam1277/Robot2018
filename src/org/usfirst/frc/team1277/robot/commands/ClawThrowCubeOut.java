package org.usfirst.frc.team1277.robot.commands;

import org.usfirst.frc.team1277.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ClawThrowCubeOut extends Command {

	private final double THROW_SPEED = -1.0;
	private final double THROW_TIME = 30;
	private int counter;

    public ClawThrowCubeOut() {
        requires(Robot.claw);
    }

    protected void initialize() {
    	counter = 0;
    }

    protected void execute() {
    	Robot.claw.dirveWheels(THROW_SPEED, THROW_SPEED);
    	counter++;
    }

    protected boolean isFinished() {
        return (counter >= THROW_TIME);
    }

    protected void end() {
    	Robot.claw.dirveWheels(0, 0);
    }

    protected void interrupted() {
    	Robot.claw.dirveWheels(0, 0);
    }
}
