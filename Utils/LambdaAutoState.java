package org.firstinspires.ftc.teamcode.InfiniteFailsafes.Utils;
import org.firstinspires.ftc.teamcode.InfiniteFailsafes.Hardware.Robot;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class LambdaAutoState extends AutoState {

    private Consumer<LambdaAutoState> onEnterLogic = s -> {};
    private BiConsumer<LambdaAutoState, Robot> updateLogic = (s, r) -> {};
    private Consumer<LambdaAutoState> onEndLogic = s -> {};

    private List<Transition> transitions = new ArrayList<>();
    private AutoState defaultNextState = null;
    final Robot robot;
    private AutoState resolvedNextState = null; // To store which state to go to

    private static class Transition {
        Function<LambdaAutoState, Boolean> condition;
        Supplier<AutoState> nextStateSupplier; // Can be null for linear transitions
        Transition(Function<LambdaAutoState, Boolean> condition, Supplier<AutoState> nextStateSupplier) {
            this.condition = condition;
            this.nextStateSupplier = nextStateSupplier;
        }
    }

    public LambdaAutoState(Robot robot) { this.robot = robot; }

    // Configuration Methods
    public void onEnter(Consumer<LambdaAutoState> logic) { this.onEnterLogic = logic; }
    public void update(BiConsumer<LambdaAutoState, Robot> logic) { this.updateLogic = logic; }
    public void onEnd(Consumer<LambdaAutoState> logic) { this.onEndLogic = logic; }
    public void transitionTo(AutoState nextState) { this.defaultNextState = nextState; }
    public void addLinearTransition(Function<LambdaAutoState, Boolean> condition) {
        this.transitions.add(new Transition(condition, null)); // No specific destination
    }
    public void addPointerTransition(Function<LambdaAutoState, Boolean> condition, Supplier<AutoState> nextStateSupplier) {
        this.transitions.add(new Transition(condition, nextStateSupplier));
    }

    @Override
    public boolean isFinished() {
        // Check ALL possible transitions.
        for (Transition t : transitions) {
            if (t.condition.apply(this)) {
                // We found a reason to finish!
                // Figure out where to go next and store it.
                if (t.nextStateSupplier != null) {
                    resolvedNextState = t.nextStateSupplier.get(); // Go to the specific state
                } else {
                    resolvedNextState = defaultNextState; // Go to the next state in the chain
                }
                return true;
            }
        }
        // If no transition condition was met, we are not finished.
        return false;
    }

    @Override
    public AutoState getNextState() {
        return resolvedNextState;
    }

    // Other methods are the same...
    @Override public void onEnter() { onEnterLogic.accept(this); }
    @Override public void update() { updateLogic.accept(this, robot); }
    @Override public void end() { onEndLogic.accept(this); }
}
