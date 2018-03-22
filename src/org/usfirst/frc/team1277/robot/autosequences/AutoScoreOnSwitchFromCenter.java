package org.usfirst.frc.team1277.robot.autosequences;

import org.usfirst.frc.team1277.robot.commands.DriveTo;
import org.usfirst.frc.team1277.robot.commands.LiftToSwitch;
import org.usfirst.frc.team1277.robot.commands.RotatorRetract;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoScoreOnSwitchFromCenter extends CommandGroup {

    public AutoScoreOnSwitchFromCenter() {
        
    	String gameData;
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		
        if(gameData.length() > 0) {
        	if(gameData.charAt(0) == 'L') {
        		addSequential(new ScoreOnSwitchLeftFromCenterNoVision());
        	} 
        	else {
        		addSequential(new ScoreOnSwitchRightFromCenterNoVision());
        	}
        }
        else {
        	addSequential(new RotatorRetract());
        	addSequential(new LiftToSwitch());
        	addSequential(new DriveTo(0, 20));
        	addSequential(new DriveTo(-40, 0));
        	addSequential(new DriveTo(0, 80));
        }
    }
}
