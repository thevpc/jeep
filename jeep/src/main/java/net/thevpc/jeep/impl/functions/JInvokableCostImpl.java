package net.thevpc.jeep.impl.functions;

import net.thevpc.jeep.JInvokable;
import net.thevpc.jeep.JInvokableCost;

public class JInvokableCostImpl implements JInvokableCost {
    private JInvokable invokable;
    private Comparable compareKey;

    public JInvokableCostImpl(JInvokable invokable, Comparable compareKey) {
        this.invokable = invokable;
        this.compareKey = compareKey;
    }

    @Override
    public JInvokable getInvokable() {
        return invokable;
    }

    @Override
    public int compareTo(JInvokableCost o) {
        return compareKey.compareTo(((JInvokableCostImpl) o).compareKey);
    }

    @Override
    public String toString() {
        return String.valueOf(invokable)+" with cost "+compareKey;
    }
}
