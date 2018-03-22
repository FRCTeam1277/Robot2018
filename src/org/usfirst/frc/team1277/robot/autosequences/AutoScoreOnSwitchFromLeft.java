package org.usfirst.frc.team1277.robot.autosequences;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoScoreOnSwitchFromLeft extends CommandGroup {

    public AutoScoreOnSwitchFromLeft() {
    	
    	String gameData;
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		
        if(gameData.length() > 0) {
        	if(gameData.charAt(0) == 'L') {
        		addSequential(new ScoreOnSwitchLeftFromLeft());
        	} 
        	else {
        		addSequential(new DriveToAutoLine());
        	}
        }
        else {
        	addSequential(new DriveToAutoLine());
        }
    }
}
