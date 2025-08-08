package org.firstinspires.ftc.teamcode.InfiniteFailsafes;

import com.arcrobotics.ftclib.command.CommandScheduler;

public class StateManager {

    private static StateManager instance = null;

    private StateManager(){

    }

    public static synchronized StateManager getInstance(){
        if(instance == null){
            instance = new StateManager();
        }
        return instance;
    }

    private AutoState currentState = null;
    private AutoState previousState = null; // Adaugam o referinta la starea anterioara

    public StateManager(AutoState autoState){
        currentState = autoState;
    }

    public void tick() {

        CommandScheduler.getInstance().run();

        if (currentState == null) {
            return;
        }

        if (currentState != previousState) {
            if (previousState != null) {
                previousState.end();
            }
            currentState.onEnter();
            previousState = currentState;
        }

        currentState.update();

        if (currentState.isFinished()) {
            currentState = currentState.getNextState();
        }
    }


    /** WARNING: This method should NOT be called from within an AutoState class. */
    public void setCurrentState(AutoState newState) {
        this.currentState = newState;
        this.previousState = null; // Force the newState onEnter() to be ran in next tick
    }


    public AutoState getCurrentState(){
        return currentState;
    }


}
