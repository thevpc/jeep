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
public class JNodePostfixUnaryOpCall extends JNodeStatement {

    private String name;
    private JDefaultNode arg;
    private JInvokablePrefilled implFunction;

    public JNodePostfixUnaryOpCall(String name, JDefaultNode arg) {
        super();
        this.name = name;
        this.arg = arg;
    }
    public JInvokablePrefilled impl() {
        return implFunction;
    }

    public JNodePostfixUnaryOpCall setImpl(JInvokablePrefilled implFunction) {
        this.implFunction = implFunction;
        return this;
    }

    public JDefaultNode getArg() {
        return arg;
    }

    public JNodePostfixUnaryOpCall setArg(JDefaultNode arg) {
        this.arg = arg;
        return this;
    }

    @Override
    public int id() {
        return JNodeDefaultIds.NODE_OP_UNARY;
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
        return JNodeUtils.toPar(arg)+getName();
    }


}
