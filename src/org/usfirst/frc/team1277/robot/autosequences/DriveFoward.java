package org.usfirst.frc.team1277.robot.autosequences;

import org.usfirst.frc.team1277.robot.commands.DriveTo;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class DriveFoward extends CommandGroup {

    public DriveFoward() {
    	
    	addSequential(new DriveTo(0, 120));
    	
    }
}
