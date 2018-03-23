package org.usfirst.frc.team1277.robot.commands;

import org.usfirst.frc.team1277.robot.Robot;
import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveToVisionCube extends Command {
	
	private final int FRAME_WIDTH = 640, FINAL_WIDTH = 200;
	private final double XSPEED_MULTIPLIER = 0.002, YSPEED_MULTIPLIER = 20.0;
	private final double  MAX_SPEED = 0.5, LOWEST_SPEED = 0.1, LOWEST_CONTROL = 0.005;
	private int mostSignificantObject;
	private double objectWidth;

    public DriveToVisionCube() {
    	requires(Robot.driveTrain);
    }

    protected void initialize() {
    	Robot.driveTrain.setDesiredDirection(Robot.driveTrain.getDirection());
    	objectWidth = 0;
    }

    protected void execute() {
    	NetworkTableInstance instance = NetworkTableInstance.getDefault();
    	NetworkTable cubetarget = instance.getTable("cubetarget");
    	double[] cubelistpixels = cubetarget.getEntry("cubelistpixels").getValue().getDoubleArray();
    	double[] vtargetobjx = cubetarget.getEntry("cubetargetx").getValue().getDoubleArray();
    	double[] vtargetobjy = cubetarget.getEntry("cubetargety").getValue().getDoubleArray();
    	double[] vtargetobjw = cubetarget.getEntry("cubetargetw").getValue().getDoubleArray();
    	double[] vtargetobjh = cubetarget.getEntry("cubetargeth").getValue().getDoubleArray();
    	
    	double moveX, moveY, rotate;
    	mostSignificantObject = -1;
    	
    	double numberOfObjects=vtargetobjx.length;
		SmartDashboard.putNumber("numberOfObjects", numberOfObjects);
    	if (numberOfObjects == 0) {//temporary fix
    		Robot.driveTrain.drive(0, 0, 0, false);
    	}
    	else {
        	for (int i = 0; i < numberOfObjects; i++) {
        		if (mostSignificantObject == -1) mostSignificantObject = i;
        		else if (cubelistpixels[i]> cubelistpixels[mostSignificantObject]){
        			mostSignificantObject = i;
        		}
        	}
        	
        	objectWidth = vtargetobjw[mostSignificantObject];
        	
        	if (mostSignificantObject == -1) {
        		moveX = 0;
        		moveY = 0;
        	}
        	else {
        		moveY = 1/Math.abs(vtargetobjx[mostSignificantObject]+vtargetobjw[mostSignificantObject]/2/*position*/ - (FRAME_WIDTH / 2));
        		moveX = ((vtargetobjx[mostSignificantObject]+vtargetobjw[mostSignificantObject]/2)/*position*/ - (FRAME_WIDTH / 2));
        		
        		//System.out.println(Math.abs(vtargetobjx[mostSignificantObject]+vtargetobjw[mostSignificantObject]/2/*position*/ - (FRAME_WIDTH / 2)));
        		/*
        		if ((Math.abs(vtargetobjx[mostSignificantObject]+vtargetobjw[mostSignificantObject]/2 - (FRAME_WIDTH / 2)) < 20)){
        			inline++;
        		}
        		if (inline>2) {
        			//System.out.println("exiting");
        			moveX=0;
        		}
        		*/
        		


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
        	
        	//rotate = 0;
        	
        	Robot.driveTrain.drive(moveY, -moveX, rotate, false);
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
