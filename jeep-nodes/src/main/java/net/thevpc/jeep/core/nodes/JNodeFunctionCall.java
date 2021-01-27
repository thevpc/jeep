/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.core.nodes;

import net.thevpc.jeep.JInvokablePrefilled;
import net.thevpc.jeep.util.JNodeUtils;
import net.thevpc.jeep.util.JeepUtils;
import net.thevpc.jeep.*;

import java.util.Arrays;

/**
 * @author thevpc
 */
public class JNodeFunctionCall extends JNodeStatement {

    private String name;
    private JDefaultNode[] args;
    private JInvokablePrefilled impl;

    public JNodeFunctionCall(String name, JDefaultNode[] args) {
        super();
        this.name = name;
        this.args = args;
    }

    @Override
    public int id() {
        return JNodeDefaultIds.NODE_FUNCTION_CALL;
    }

    public JInvokablePrefilled impl() {
        return impl;
    }

    public void setImpl(JInvokablePrefilled impl) {
        this.impl = impl;
    }

//    @Override
//    public Object evaluate(JContext context) {
//        return context.functions().evaluate(getName(), getArgs());
//    }


    public JNodeFunctionCall setName(String name) {
        this.name = name;
        return this;
    }

    public JNodeFunctionCall setArgs(JDefaultNode[] args) {
        this.args = args;
        return this;
    }

//    @Override
//    public JType getType(JContext context) {
//        String name = getName();
//        JType[] argTypes = JeepUtils.getTypesOrNulls(args);
//        JFunction f = context.functions().findMatchOrNull(name, argTypes);
//        if(f==null){
//            if(name.isEmpty()){
//                throw new NoSuchElementException("Implicit JFunction not found "+ Arrays.asList(getArgs()));
//            }
//            throw new NoSuchElementException("JFunction not found "+ name +Arrays.asList(getArgs()));
////            return Object.class;
//        }
//        return f.returnType();
//    }

    public String getName() {
        return name;
    }

    public JDefaultNode[] getArgs() {
        return Arrays.copyOf(args, args.length);
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

    public JDefaultNode get(int index) {
        return args[index];
    }

    public JDefaultNode getOperand(int index) {
        return args[index];
    }

    public boolean isBinary(String name) {
        return getName().equals(name) && isBinary();
    }

    public boolean isUnary(String name) {
        return getName().equals(name) && isUnary();
    }

    public boolean is(String name) {
        return getName().equals(name);
    }

    public boolean isBinary() {
        return args.length == 2;
    }

    public boolean isUnary() {
        return args.length == 1;
    }

    public int getOperandsCount() {
        return args.length;
    }



}
