/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.core.nodes;

/**
 * @author thevpc
 */
public abstract class JNodeVariable extends JDefaultNode {

    private final String name;

    public JNodeVariable(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
