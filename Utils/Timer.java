package org.firstinspires.ftc.teamcode.InfiniteFailsafes.Utils;

public class Timer {

    private long startTime;

    /**
     * This will return the System current time in milliseconds
     */
    public Timer() {
        reset();
    }

    // Reset the timer to the current time
    public void reset() {
        startTime = System.currentTimeMillis(); // You could use nanoTime() for higher precision if needed
    }

    // Get the elapsed time since reset in milliseconds
    public double getTime() {
        return System.currentTimeMillis() - startTime; // Returns time in milliseconds
    }
}