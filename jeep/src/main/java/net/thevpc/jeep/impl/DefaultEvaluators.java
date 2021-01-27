package net.thevpc.jeep.impl;

import net.thevpc.jeep.JContext;
import net.thevpc.jeep.JEvaluator;
import net.thevpc.jeep.JEvaluatorFactory;
import net.thevpc.jeep.JEvaluators;
//import net.thevpc.jeep.core.DefaultJEvaluator;

public class DefaultEvaluators implements JEvaluators {
    private JContext context;
    private JEvaluatorFactory factory;
    private JEvaluators parent;

    public DefaultEvaluators(JContext context,JEvaluators parent) {
        this.context = context;
        this.parent = parent;
    }

    @Override
    public JEvaluatorFactory getFactory() {
        return factory;
    }

    @Override
    public JEvaluators setFactory(JEvaluatorFactory factory) {
        this.factory = factory;
        return this;
    }

    public JEvaluator newEvaluator() {
        if (factory != null) {
            return factory.create(context);
        }
        if(parent!=null){
            JEvaluator e = parent.newEvaluator();
            if(e!=null){
                return e;
            }
        }
        throw new IllegalArgumentException("Missing Evaluator Instance");
//        return DefaultJEvaluator.INSTANCE;
    }

}
