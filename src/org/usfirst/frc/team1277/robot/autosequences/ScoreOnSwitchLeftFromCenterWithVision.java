package org.usfirst.frc.team1277.robot.autosequences;

import org.usfirst.frc.team1277.robot.commands.DeployClaw;
import org.usfirst.frc.team1277.robot.commands.DriveTo;
import org.usfirst.frc.team1277.robot.commands.DriveToVisionTargetLeft;
import org.usfirst.frc.team1277.robot.commands.LiftToSwitch;
import org.usfirst.frc.team1277.robot.commands.ReleaseCube;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ScoreOnSwitchLeftFromCenterWithVision extends CommandGroup {

    public ScoreOnSwitchLeftFromCenterWithVision() {
        
    	addSequential(new DeployClaw());
    	addSequential(new LiftToSwitch());
    	addSequential(new DriveTo(0, 6));
    	addSequential(new DriveToVisionTargetLeft());
    	addSequential(new ReleaseCube());
    	
    }
}
