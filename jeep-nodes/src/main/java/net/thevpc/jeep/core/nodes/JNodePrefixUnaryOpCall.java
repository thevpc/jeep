/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.core.nodes;

import net.thevpc.jeep.JInvokablePrefilled;
import net.thevpc.jeep.util.JNodeUtils;

/**
 * @author thevpc
 */
public class JNodePrefixUnaryOpCall extends JNodeStatement {

    private String name;
    private JDefaultNode arg;
    private JInvokablePrefilled impl;

    public JNodePrefixUnaryOpCall(String name,JDefaultNode arg) {
        super();
        this.name = name;
        this.arg = arg;
    }

    public String getName() {
        return name;
    }

    public JInvokablePrefilled impl() {
        return impl;
    }

    public JNodePrefixUnaryOpCall setImpl(JInvokablePrefilled impl) {
        this.impl = impl;
        return this;
    }

    public JNodePrefixUnaryOpCall setArg(JDefaultNode arg) {
        this.arg = arg;
        return this;
    }

    public JDefaultNode getArg() {
        return arg;
    }

    @Override
    public int id() {
        return JNodeDefaultIds.NODE_OP_UNARY_PREFIX;
    }

//    @Override
//    public JType getType(JContext context) {
//        return context.types().forName(Object.class);
//    }


    @Override
    public String toString() {
        return name + JNodeUtils.toPar(arg);
    }


}
