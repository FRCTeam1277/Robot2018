package org.usfirst.frc.team1277.robot.autosequences;

import org.usfirst.frc.team1277.robot.commands.DriveTo;
import org.usfirst.frc.team1277.robot.commands.LiftToSwitch;
import org.usfirst.frc.team1277.robot.commands.RotatorRetract;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class DriveToAutoLine extends CommandGroup {

    public DriveToAutoLine() {
        
    	addSequential(new RotatorRetract());
    	//addSequential(new GrabCube());
    	addSequential(new LiftToSwitch());
    	addSequential(new DriveTo(0, 120));
    	
    }
}
