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
public class JNodeLiteral extends JDefaultNode {

    private final Object value;
    public JNodeLiteral(Object value) {
        super();
        this.value = value;
    }
    @Override
    public int id() {
        return JNodeDefaultIds.NODE_LITERAL;
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
        if(value instanceof String){
            StringBuilder sb=new StringBuilder("\"");
            for (char c : value.toString().toCharArray()) {
                switch (c){
                    case '\"':{
                        sb.append("\\\"");
                        break;
                    }
                    case '\n':{
                        sb.append("\\n");
                        break;
                    }
                    default:{
                        sb.append(c);
                    }
                }
            }
            sb.append("\"");
            return String.valueOf(sb);
        }
        return String.valueOf(value);
    }

}
