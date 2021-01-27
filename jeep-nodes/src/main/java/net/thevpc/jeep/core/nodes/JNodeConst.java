/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.core.nodes;

import net.thevpc.jeep.JType;

/**
 *
 * @author thevpc
 */
public class JNodeConst extends JNodeVariable {

    private final Object value;
    private final JType type;

    public JNodeConst(String name, Object value, JType type) {
        super(name);
        this.value = value;
        this.type = type;
        if(type==null){
            throw new RuntimeException("Missing type");
        }
    }

    @Override
    public int id() {
        return JNodeDefaultIds.NODE_CONST;
    }

//    @Override
//    public JType getType(JContext context) {
//        return type;
//    }


//    @Override
//    public Object evaluate(JContext context) {
//        return value;
//    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getName();//+"=" + getValue();
    }
    

    
}
