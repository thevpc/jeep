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
public class JNodeSuffixedIntNumber extends JNodeVariable {
    private String number;
    private String suffix;
    public JNodeSuffixedIntNumber(String number, String suffix) {
        super(number+suffix);
        this.number=number;
        this.suffix=suffix;
    }

    @Override
    public int id() {
        return JNodeDefaultIds.NODE_SUFFIXED_INT_NUMBER;
    }

//    @Override
//    public Object evaluate(JContext context) {
//        return context.vars().getValue(getName());
//    }

    public String toString() {
        return  getName();
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
