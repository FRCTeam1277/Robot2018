package org.usfirst.frc.team1277.robot.commands;

import org.usfirst.frc.team1277.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class LiftShiftGear extends Command {
	
	private boolean finished, abort;
	private final double SLOW_LIFT_SPEED = 0.5, SLOW_DROP_SPEED = -0.1, SHIFT_HEIGHT = 128; //Counts (4096 per Revolution, 36000 at Top)

    public LiftShiftGear() {
    	requires(Robot.lift);
    }
    
    protected void initialize() {
    	abort = false;
    	finished = false;
    	if (Robot.lift.getHeight() > 0) {
    		abort = true;
    		return;
    	}
    	Robot.lift.shift();
    	Robot.lift.lift(SLOW_LIFT_SPEED);
    }

    protected void execute() {
    	Robot.lift.positionCorrections(); 
    	if (Robot.lift.getHeight() > SHIFT_HEIGHT) {
    		Robot.lift.lift(SLOW_DROP_SPEED);
    		finished = true;
    	}
    }

    protected boolean isFinished() {
        return ((finished && Robot.lift.getHeight() == 0) || abort);
    }
    
    protected void end() {
    	Robot.lift.liftHold();
    }
    
    protected void interrupted() {
    	Robot.lift.liftHold();
    }
}
