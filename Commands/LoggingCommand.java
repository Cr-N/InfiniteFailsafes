package org.firstinspires.ftc.teamcode.InfiniteFailsafes.Commands;

import com.arcrobotics.ftclib.command.Command;

public class LoggingCommand implements Command {
    private final Command wrapped;
    private final String name;

    public LoggingCommand(Command command, String name) {
        this.wrapped = command;
        this.name = name;
    }

    @Override
    public void initialize() {
        System.out.println("[LOG] Starting command: " + name);
        wrapped.initialize();
    }

    @Override
    public void execute() {
        wrapped.execute();
    }

    @Override
    public void end(boolean interrupted) {
        System.out.println("[LOG] Ending command: " + name + (interrupted ? " (interrupted)" : ""));
        wrapped.end(interrupted);
    }

    @Override
    public boolean isFinished() {
        return wrapped.isFinished();
    }

    // Important: propagate requirements
    @Override
    public java.util.Set<com.arcrobotics.ftclib.command.Subsystem> getRequirements() {
        return wrapped.getRequirements();
    }
}
