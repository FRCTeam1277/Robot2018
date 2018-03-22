package org.usfirst.frc.team1277.robot.commands;

import org.usfirst.frc.team1277.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class LiftHeightTracking extends Command {

    public LiftHeightTracking() {
    	requires(Robot.lift);
    }

    protected void initialize() {
    	
    }

    protected void execute() {
    	Robot.lift.trackHeight();
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    	
    }

    protected void interrupted() {
    	
    }
}
