/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.core.nodes;

import net.thevpc.jeep.util.JNodeUtils;
import net.thevpc.jeep.util.JeepUtils;

/**
 * @author thevpc
 */
public class JNodeUnknownMethodCall extends JNodeStatement {

    private String name;
    private JDefaultNode parent;
    private JDefaultNode[] args;

    public JNodeUnknownMethodCall(JDefaultNode parent,String name,JDefaultNode[] args) {
        super();
        this.name = name;
        this.parent = parent;
        this.args = args;
    }

    public JNodeUnknownMethodCall setName(String name) {
        this.name = name;
        return this;
    }

    public JNodeUnknownMethodCall setParent(JDefaultNode parent) {
        this.parent = parent;
        return this;
    }

    public JNodeUnknownMethodCall setArgs(JDefaultNode[] args) {
        this.args = args;
        return this;
    }

    public JDefaultNode[] getArgs() {
        return args;
    }

    public JDefaultNode getParent() {
        return parent;
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

    @Override
    public String toString() {
        String n = getName();
        if (JeepUtils.isDefaultOp(n)) {
            switch (args.length) {
                case 1: {
                    return /*"(" + */ getName() + JNodeUtils.toPar(args[0])/*+ ")"*/;
                }
                case 2: {
                    return /*"(" +*/ JNodeUtils.toPar(args[0]) + getName() + JNodeUtils.toPar(args[1]) /*+ ")"*/;
                }
            }
        }
        StringBuilder sb = new StringBuilder().append(n).append("(");
        for (int i = 0; i < args.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            String sargi = args[i].toString();
            sb.append(sargi);
        }
        sb.append(")");
        return sb.toString();
    }


}
