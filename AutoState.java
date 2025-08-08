package org.firstinspires.ftc.teamcode.DECODE.AutoStateMachine;

import android.util.Log;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AutoState {

    public boolean endState = false; // that means that, by default, the state keeps running no matter what

    public enum COMMAND_STATE{
        INITIALIZED,
        EXECUTING,
        FINISHED
    }

    public Map<String,COMMAND_STATE> m_cmds;

    public AutoState(){
        m_cmds = new HashMap<>();
    }


    /**
     * Executes once on state entry.
     */
    public void onEnter(){};

    /**
     * Run continuously while in the state.
     */
    public void update(){};

    /**
     * Is checked every loop to see if all the actions related to the state are done.
     * @return true if the actions have finished, false if the actions have NOT finished
     */
    public boolean isFinished(){
        return endState; // finishes instantly
    };

    /**
     * Executes this once after isFinished return true
     */
    public void end(){};

    /**
     * Decide what state to come after the current one.
     * @return
     */
    public AutoState getNextState(){return null;};

    /**
     * Logic for CommandState handling, displayed in the diagram :
     * ┌───────────────────────────────────────┐
     * │Sequential Command                     │
     * │                                       │
     * │   ┌──────────────────────────────┐    │
     * │   │Sequential Command            │    │
     * │   │ Set state to: INITIALIZED    │    │
     * │   │   ┌─────────────────────┐    │    │
     * │   │   │ Paralel Command     │    │    │
     * │   │   │                     │    │    │
     * │   │   │ Set state to:       │    │    │
     * │   │   │  EXECUTING          │    │    │
     * │   │   │ ┌─────────────────┐ │    │    │
     * │   │   │ │Command Meant to │ │    │    │
     * │   │   │ │   be    ran     │ │    │    │
     * │   │   │ └─────────────────┘ │    │    │
     * │   │   └─────────────────────┘    │    │
     * │   │                              │    │
     * │   └──────────────────────────────┘    │
     * │   Set state to:                       │
     * │      FINISHED                         │
     * └───────────────────────────────────────┘
     *
     */
    public Command trackCommand(String name, Command command) {
        return command
                .alongWith(new InstantCommand(()->m_cmds.put(name, COMMAND_STATE.EXECUTING)))
                .beforeStarting(() -> m_cmds.put(name, COMMAND_STATE.INITIALIZED))
                .whenFinished(() -> m_cmds.put(name, COMMAND_STATE.FINISHED));
    }

    public Command trackCommand(String name, Command command, boolean showInLogs) {
        if(!showInLogs){
            return command
                    .alongWith(new InstantCommand(()->m_cmds.put(name, COMMAND_STATE.EXECUTING)))
                    .beforeStarting(() -> m_cmds.put(name, COMMAND_STATE.INITIALIZED))
                    .whenFinished(() -> m_cmds.put(name, COMMAND_STATE.FINISHED));
        }
        else{
            return command
                    .alongWith(
                            new InstantCommand(
                                    ()->{
                                        m_cmds.put(name, COMMAND_STATE.EXECUTING);
                                        Log.d("AutoState Command Tracker", "Command " + name + " is in EXECUTING State");
                                    }
                            )
                    )
                    .beforeStarting(
                            () ->{
                                m_cmds.put(name, COMMAND_STATE.INITIALIZED);
                                Log.d("AutoState Command Tracker", "Command " + name + " is in INITIALIZED State");
                            }
                    )
                    .whenFinished(
                            () ->{
                                m_cmds.put(name, COMMAND_STATE.FINISHED);
                                Log.d("AutoState Command Tracker", "Command " + name + " is in FINISHED State");
                            }
                    );
        }

    }

}
