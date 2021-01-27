/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.core.nodes;

import net.thevpc.jeep.JType;

/**
 * @author thevpc
 */
public class JNodeStaticFieldInvoke extends JNodeStatement {

    private final String name;
    private final JType otype;

    public JNodeStaticFieldInvoke(String name, JType otype) {
        super();
        this.name = name;
        this.otype = otype;
    }

    @Override
    public int id() {
        return JNodeDefaultIds.NODE_STATIC_FIELD;
    }

//    @Override
//    public Object evaluate(JContext context) {
//        return context.functions().evaluate(getName(), getArgs());
//    }

//    @Override
//    public JType getType(JContext context) {
//        String name = getName();
//        JField f = otype.matchedField(name);
//        if(f==null){
//            return null;
//        }
//        return f.type();
//    }

    public String getName() {
        return name;
    }

    public JType getDeclaringType() {
        return otype;
    }

    @Override
    public String toString() {
        return otype.getName()+"."+name;
    }

    public boolean is(String name) {
        return getName().equals(name);
    }

}
