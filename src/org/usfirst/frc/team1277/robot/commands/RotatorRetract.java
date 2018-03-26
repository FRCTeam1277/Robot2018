package org.usfirst.frc.team1277.robot.commands;

import org.usfirst.frc.team1277.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class RotatorRetract extends Command {

	private final double RAISE_SPEED = -0.5, LET_DOWN_SPEED = 0.0, RESIST_SPEED = -0.15;
	private final double RESIST_END_POINT = 70, LET_DOWN_END_POINT = 10, RAISE_TIME = 20; //Counts (497 per Revolution)
	private double initialPosition, counter;
	
	public RotatorRetract() {
		requires(Robot.clawRotator);
	}
	
	protected void initialize() {
		initialPosition = Robot.clawRotator.getPosition();
		counter = 0;
		
		//Take Weight off Stopper and Open it
		Robot.clawRotator.rotate(RAISE_SPEED);
		Robot.clawRotator.openStopper();
	}
	
	protected void execute() {
		
		//Allow Stopper to Open Then Let Claw Down
		if (counter >= RAISE_TIME) {
			Robot.clawRotator.rotate(LET_DOWN_SPEED);
		}
		else counter++;
		
		//Once Down Far Enough Resist Falling
		if (Robot.clawRotator.getPosition() - initialPosition >= LET_DOWN_END_POINT) {
			Robot.clawRotator.rotate(RESIST_SPEED);
		}
	}
	
	@Override
	protected boolean isFinished() {
		return (Robot.clawRotator.getPosition() - initialPosition >= RESIST_END_POINT);
	}
	
	protected void end() {
		Robot.clawRotator.rotate(0);
		Robot.clawRotator.closeStopper();
	}

	protected void interrupted() {
		Robot.clawRotator.rotate(0);
		Robot.clawRotator.closeStopper();
	}
}
