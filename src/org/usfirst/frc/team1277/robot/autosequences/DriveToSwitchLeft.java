package org.usfirst.frc.team1277.robot.autosequences;

import org.usfirst.frc.team1277.robot.commands.DriveTo;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class DriveToSwitchLeft extends CommandGroup {

    public DriveToSwitchLeft() {

    	addSequential(new DriveTo(0, 20));
    	addSequential(new DriveTo(-60, 0));
    	addSequential(new DriveTo(0, 80));
    	
    }
}
