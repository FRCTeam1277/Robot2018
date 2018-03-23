package org.usfirst.frc.team1277.robot.autosequences;

import org.usfirst.frc.team1277.robot.commands.ClawPushCubeOut;
import org.usfirst.frc.team1277.robot.commands.DriveTo;
import org.usfirst.frc.team1277.robot.commands.LiftToSwitch;
import org.usfirst.frc.team1277.robot.commands.RotatorRetract;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ScoreOnSwitchLeftFromCenterNoVision extends CommandGroup {

    public ScoreOnSwitchLeftFromCenterNoVision() {
        
    	addSequential(new RotatorRetract());
    	addSequential(new LiftToSwitch());
    	addSequential(new DriveTo(0, 20));
    	addSequential(new DriveTo(-60, 0));
    	addSequential(new DriveTo(0, 80));
    	addSequential(new ClawPushCubeOut());
    	
    }
}
