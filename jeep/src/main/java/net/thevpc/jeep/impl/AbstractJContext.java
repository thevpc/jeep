/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.impl;

import net.thevpc.jeep.*;
import net.thevpc.jeep.impl.functions.DefaultJInvokeContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author thevpc
 */
public abstract class AbstractJContext implements JContext {
    private final Map<String, Object> userProperties = new HashMap<>();

    

    @Override
    public Map<String, Object> userProperties() {
        return userProperties;
    }

    public Object evaluate(JNode expression, Object userObject) {
        if (expression == null) {
            return null;
        }
        JEvaluator jEvaluator = evaluators().newEvaluator();
        return jEvaluator.evaluate(expression, new DefaultJInvokeContext(
                this,
                jEvaluator,
                null,
                new JEvaluable[0],
                null,null,userObject
        ));
    }

    public Object evaluate(String expression, Object userObject) {
        if (expression == null) {
            return null;
        }
        return evaluate(parse(expression), userObject);
    }


}
