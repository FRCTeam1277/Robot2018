package org.usfirst.frc.team1277.robot.commands;

import org.usfirst.frc.team1277.robot.Robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveToVisionTargetRight extends Command {
	
	private final int FRAME_WIDTH = 640, FINAL_WIDTH = 100;
	private final double XSPEED_MULTIPLIER = 0.002, YSPEED_MULTIPLIER = 20.0;
	private final double  MAX_SPEED = 0.5, LOWEST_SPEED = 0.1, LOWEST_CONTROL = 0.005;
	private int mostSignificantObject;
	private double objectWidth;
	
	int numberOfObjects = 0;

    public DriveToVisionTargetRight() {
    	requires(Robot.driveTrain);
    }

    protected void initialize() {
    	Robot.driveTrain.setDesiredDirection(Robot.driveTrain.getDirection());
    	objectWidth = 0;
    }

    protected void execute() {
    	
    	NetworkTableInstance instance = NetworkTableInstance.getDefault();
    	NetworkTable objects = instance.getTable("vtargetobj");
    	double numberOfObjects = objects.getEntry("objcount").getValue().getDouble();
    	double[] vtargetobjx = objects.getEntry("vtargetobjx").getValue().getDoubleArray();
    	//double[] vtargetobjy = objects.getEntry("vtargetobjy").getValue().getDoubleArray();
    	double[] vtargetobjw = objects.getEntry("vtargetobjw").getValue().getDoubleArray();
    	double[] vtargetobjh = objects.getEntry("vtargetobjh").getValue().getDoubleArray();
    	
    	double moveX, moveY, rotate;
    	mostSignificantObject = -1;
    	
    	if (numberOfObjects == 0) {//temporary fix
    		Robot.driveTrain.drive(0, 0, 0, false);
    	}
    	else {
    	for (int i = 0; i < numberOfObjects; i++) {
    		if (mostSignificantObject == -1) mostSignificantObject = i;
    		else if ((vtargetobjw[i] * vtargetobjh[i])/*area*/ * (vtargetobjx[i] + (vtargetobjw[i] / 2))/*position from right*/ > 
					(vtargetobjw[mostSignificantObject] * vtargetobjh[mostSignificantObject])/*area*/ * 
					(vtargetobjx[mostSignificantObject] + (vtargetobjw[mostSignificantObject] / 2))/*position from right*/) {
    			mostSignificantObject = i;
    		}
    	}
    	
    	objectWidth = vtargetobjw[mostSignificantObject];
    	
    	if (mostSignificantObject == -1) {
    		moveX = 0;
    		moveY = 0;
    	}
    	else {
    		moveX = (vtargetobjx[mostSignificantObject]+vtargetobjw[mostSignificantObject]/2)/*position*/ - (FRAME_WIDTH / 2);
    		moveY = 1 / Math.abs(vtargetobjx[mostSignificantObject]+vtargetobjw[mostSignificantObject]/2/*position*/ - (FRAME_WIDTH / 2));
    	}
    	moveX *= XSPEED_MULTIPLIER;
    	moveY *= YSPEED_MULTIPLIER;
    	if (moveX > MAX_SPEED) moveX = MAX_SPEED;
    	else if (moveX < -MAX_SPEED) moveX = -MAX_SPEED;
    	if (moveY > MAX_SPEED) moveY = MAX_SPEED;
    	else if (moveY < -MAX_SPEED) moveY = -MAX_SPEED;
    	
    	//Rotate Corrections
    	rotate = Robot.driveTrain.getRobotRotate();
    	if (rotate > LOWEST_CONTROL && rotate < LOWEST_SPEED) rotate = LOWEST_SPEED;
    	else if (rotate < -LOWEST_CONTROL && rotate > -LOWEST_SPEED) rotate = -LOWEST_SPEED;
    	
    	Robot.driveTrain.drive(moveY, moveX, rotate, false);
    	}
    }

    protected boolean isFinished() {
        return (objectWidth >= FINAL_WIDTH);
    }
    
    protected void end() {
    	Robot.driveTrain.drive(0, 0, 0, true);
    }

    protected void interrupted() {
    	Robot.driveTrain.drive(0, 0, 0, true);
    }
}
