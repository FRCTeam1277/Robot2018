package org.usfirst.frc.team1277.robot.autosequences;

import org.usfirst.frc.team1277.robot.commands.DeployClaw;
import org.usfirst.frc.team1277.robot.commands.DriveTo;
import org.usfirst.frc.team1277.robot.commands.LiftToSwitch;
import org.usfirst.frc.team1277.robot.commands.ReleaseCube;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ScoreOnSwitchRightFromCenterNoVision extends CommandGroup {

    public ScoreOnSwitchRightFromCenterNoVision() {
       
    	addSequential(new DeployClaw());
    	addSequential(new LiftToSwitch());
    	addSequential(new DriveTo(0, 20));
    	addSequential(new DriveTo(60, 0));
    	addSequential(new DriveTo(0, 80));
    	addSequential(new ReleaseCube());
    	
    }
}
