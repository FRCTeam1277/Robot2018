package org.usfirst.frc.team1277.robot.autosequences;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoScoreOnSwitchFromRight extends CommandGroup {

    public AutoScoreOnSwitchFromRight() {

    	String gameData;
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		
        if(gameData.length() > 0) {
        	if(gameData.charAt(0) == 'L') {
        		addSequential(new DriveToAutoLine());
        	} 
        	else {
        		addSequential(new ScoreOnSwitchRightFromRight());
        	}
        }
        else {
        	addSequential(new DriveToAutoLine());
        }
    }
}
