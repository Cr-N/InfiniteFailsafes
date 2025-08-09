package org.firstinspires.ftc.teamcode.InfiniteFailsafes.Utils;
import org.firstinspires.ftc.teamcode.InfiniteFailsafes.Hardware.Robot;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class FailsafeBuilder {

    private final Robot robot;
    private final Map<Object, LambdaAutoState> states = new HashMap<>();
    private LambdaAutoState currentStateConfig = null;
    private Object firstStateKey = null;

    public FailsafeBuilder(Robot robot) {
        this.robot = robot;
    }

    public FailsafeBuilder state(Object key) {
        LambdaAutoState newState = states.computeIfAbsent(key, k -> new LambdaAutoState(robot));
        if (firstStateKey == null) {
            firstStateKey = key;
        }
        if (currentStateConfig != null) {
            // This tells the PREVIOUS state that its default "next" state is this new one.
            currentStateConfig.transitionTo(newState);
        }
        currentStateConfig = newState;
        return this;
    }

    public FailsafeBuilder onEnterSchedule(Consumer<LambdaAutoState> commandScheduler) {
        if (currentStateConfig == null) throw new IllegalStateException("No state defined.");
        currentStateConfig.onEnter(commandScheduler);
        return this;
    }

    public FailsafeBuilder update(BiConsumer<LambdaAutoState, Robot> logic) {
        if (currentStateConfig == null) throw new IllegalStateException("No state defined.");
        currentStateConfig.update(logic);
        return this;
    }

    // This is for a LINEAR transition (goes to the next state in the chain)
    public FailsafeBuilder transition(Function<LambdaAutoState, Boolean> condition) {
        if (currentStateConfig == null) throw new IllegalStateException("No state defined.");
        // We add this as a transition, but we DON'T give it a specific destination.
        // It will use the default "next state" when it's triggered.
        currentStateConfig.addLinearTransition(condition);
        return this;
    }

    // This is for a POINTER transition (jumps to a specific state)
    public FailsafeBuilder transition(Function<LambdaAutoState, Boolean> condition, Object nextStateKey) {
        if (currentStateConfig == null) throw new IllegalStateException("No state defined.");
        // We add this as a transition AND give it a specific destination.
        currentStateConfig.addPointerTransition(condition, () -> states.get(nextStateKey));
        return this;
    }

    public FailsafeBuilder onEnd(Consumer<LambdaAutoState> logic) {
        if (currentStateConfig == null) throw new IllegalStateException("No state defined.");
        currentStateConfig.onEnd(logic);
        return this;
    }

    public AutoState build() {
        if (firstStateKey == null) throw new IllegalStateException("Cannot build with no states.");
        return states.get(firstStateKey);
    }
}
