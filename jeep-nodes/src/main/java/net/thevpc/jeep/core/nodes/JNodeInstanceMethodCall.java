/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.core.nodes;

import net.thevpc.jeep.util.JeepUtils;
import net.thevpc.jeep.*;

import java.util.Arrays;

/**
 * @author thevpc
 */
public class JNodeInstanceMethodCall extends JNodeStatement {

    private final String name;
    private final JDefaultNode object;
    private final JDefaultNode[] args;

    public JNodeInstanceMethodCall(String name, JDefaultNode object, JDefaultNode[] args) {
        super();
        this.name = name;
        this.args = args;
        this.object = object;
    }

    @Override
    public int id() {
        return JNodeDefaultIds.NODE_INSTANCE_METHOD;
    }

//    @Override
//    public JType getType(JContext context) {
//        String name = getName();
//        JType itype = context.typeOfNode(object);
//        if(itype==null){
//            return null;
//        }
//        JType[] argTypes = context.typeOfNodes(args);
//
//        JMethod f = context.functions().findMatchOrNull(itype,name, argTypes);
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

    private String toPar(JDefaultNode e) {
        if (e instanceof JNodeLiteral
                || e instanceof JNodeArray
                || e instanceof JNodeConst
                || e instanceof JNodeVariable
                || e instanceof JNodeVarName) {
            return e.toString();
        }
        return "(" + e.toString() + ")";
    }

    @Override
    public String toString() {
        String n = getName();
        if (JeepUtils.isDefaultOp(n)) {
            switch (args.length) {
                case 1: {
                    return /*"(" + */ getName() + toPar(args[0])/*+ ")"*/;
                }
                case 2: {
                    return /*"(" +*/ toPar(args[0]) + getName() + toPar(args[1]) /*+ ")"*/;
                }
            }
        }
        StringBuilder sb = new StringBuilder().append(object.toString()).append(".").append(n).append("(");
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

    public JDefaultNode[] getOperands() {
        return Arrays.copyOf(args, args.length);
    }


}
