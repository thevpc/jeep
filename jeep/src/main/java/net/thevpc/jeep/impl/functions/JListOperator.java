/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.impl.functions;

import net.thevpc.jeep.*;
import net.thevpc.jeep.core.JFunctionBase;

/**
 *
 * @author thevpc
 */
public class JListOperator extends JFunctionBase {
    
    private JInvoke operator;
    private JType operandTypeArr;

    public JListOperator(JInvoke operator, String name, JType resultType, JType operandType) {
        //            super(name, resultType, true, toArrayClass(operandType));
        super(name, resultType, new JType[]{operandType.toArray(1)}, true,"<unknown-source>");
        this.operator = operator;
        this.operandTypeArr = operandType.toArray(1);
    } //            super(name, resultType, true, toArrayClass(operandType));

    @Override
    public Object invoke(JInvokeContext icontext) {
        return operator.invoke(icontext);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String n = getName();
        if (n == null) {
            n = "<IMPLICIT>";
        }
        sb.append(n).append("(");
        sb.append(operandTypeArr.componentType().getSimpleName()).append("...");
        sb.append(")");
        return "ExpressionListOperator{" + "operator=" + operator + ", resultType=" + getReturnType() + ", operandTypeArr=" + operandTypeArr + '}';
    }

    @Override
    public JTypes getTypes() {
        return operandTypeArr.getTypes();
    }



}
