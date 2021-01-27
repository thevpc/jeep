/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.core.nodes;


/**
 * @author thevpc
 */
public class JNodeDotOperatorApply extends JNodeStatement {

    private final String name;
    private final JDefaultNode parent;

    public JNodeDotOperatorApply(String name, JDefaultNode parent) {
        super();
        this.name = name;
        this.parent = parent;
    }

    @Override
    public int id() {
        return JNodeDefaultIds.NODE_SYNTACTIC;
    }

//    @Override
//    public JType getType(JContext context) {
//        return context.types().forName(Object.class);
//    }

    public String getName() {
        return name;
    }

    public JDefaultNode getParent() {
        return parent;
    }

    @Override
    public String toString() {
        return parent.toString() +
                "." + name;
    }


}
