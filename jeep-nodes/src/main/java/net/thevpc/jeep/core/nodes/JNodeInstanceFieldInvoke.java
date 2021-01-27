/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.core.nodes;

import net.thevpc.jeep.*;

/**
 * @author thevpc
 */
public class JNodeInstanceFieldInvoke extends JNodeStatement {

    private final String name;
    private final JDefaultNode object;

    public JNodeInstanceFieldInvoke(String name, JDefaultNode object) {
        super();
        this.name = name;
        this.object = object;
    }

    @Override
    public int id() {
        return JNodeDefaultIds.NODE_INSTANCE_FIELD;
    }

//    @Override
//    public Object evaluate(JContext context) {
//        return context.functions().evaluate(getName(), getArgs());
//    }

//    @Override
//    public JType getType(JContext context) {
//        String name = getName();
//        JType argTypes = context.typeOfNode(object);
//        if(argTypes==null){
//            return null;
//        }
//        JField f = argTypes.matchedField(name);
//        if(f==null){
//            return null;
//        }
//        return f.type();
//    }

    public String getName() {
        return name;
    }

    public JDefaultNode getObject() {
        return object;
    }

    @Override
    public String toString() {
        return object.toString()+"."+name;
    }

    public boolean is(String name) {
        return getName().equals(name);
    }

}
