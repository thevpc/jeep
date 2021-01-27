/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.core.nodes;

import net.thevpc.jeep.*;

import java.util.Arrays;

/**
 *
 * @author thevpc
 */
public class JNodeArray extends JDefaultNode {

    private final String arrayType;
    private final JDefaultNode[] values;

    public JNodeArray(String arrayType, JDefaultNode[] values) {
        super();
        this.values = values;
        this.arrayType= arrayType;
    }

    @Override
    public int id() {
        return JNodeDefaultIds.NODE_ARRAY;
    }

    public String getArrayType() {
        return arrayType;
    }

//    @Override
//    public JType getType(JContext context) {
//        JType o = null;
//        for (JDefaultNode value : values) {
//            if (value != null) {
//                o = o == null ? value.getType(context) : o.firstCommonSuperType(value.getType(context));
//            }
//        }
//        if(o==null){
//            return context.types().forName(Object.class).toArray(1);
//        }else{
//            return o.toArray(1);
//        }
//    }

    public JDefaultNode get(int i) {
        return values[i];
    }
    
    public JDefaultNode[] getValues() {
        return values;
    }


    @Override
    public String toString() {
        return Arrays.toString(values);
    }
}
