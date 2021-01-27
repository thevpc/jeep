/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.core.nodes;


/**
 *
 * @author thevpc
 */
public class JNodeVarName extends JNodeVariable {

    public JNodeVarName(String name) {
        super(name);
    }

//    @Override
//    public Object evaluate(JContext context) {
//        return context.vars().getValue(getName());
//    }

    public String toString() {
        return getName();
    }

    @Override
    public int id() {
        return JNodeDefaultIds.NODE_VAR_NAME;
    }

//    @Override
//    public JType getType(JContext context) {
//        JVar o = context.vars().find(getName());
//        if(o==null){
//            return null;
//        }
//        return o.type();
////        return o==null?null:o.getEffectiveType(evaluator);
//    }


}
