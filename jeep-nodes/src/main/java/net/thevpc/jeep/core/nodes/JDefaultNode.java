package net.thevpc.jeep.core.nodes;

import net.thevpc.jeep.JNode;
import net.thevpc.jeep.JNodeCopyFactory;
import net.thevpc.jeep.JType;

public abstract class JDefaultNode extends AbstractJNode{
    private JType type;
    public JDefaultNode() {
    }

    public JType getType() {
        return type;
    }

    @Deprecated
    public JDefaultNode setType(JType type) {
        this.type = type;
        return this;
    }

    public abstract int id();

    @Override
    public void copyFrom(JNode other, JNodeCopyFactory copyFactory) {
        super.copyFrom(other);
        if (other instanceof JDefaultNode) {
            setType(((JDefaultNode)other).getType());
        }
    }
}
