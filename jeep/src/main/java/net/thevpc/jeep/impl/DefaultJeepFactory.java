package net.thevpc.jeep.impl;

import net.thevpc.jeep.*;
import net.thevpc.jeep.impl.functions.JFunctionsImpl;
import net.thevpc.jeep.impl.types.DefaultJTypes;
import net.thevpc.jeep.impl.vars.JVarsImpl;
import net.thevpc.jeep.*;
import net.thevpc.jeep.core.tokens.DefaultJTokens;

public class DefaultJeepFactory implements JeepFactory {
    @Override
    public JVars createVars(JContext context) {
        JContext p = context.parent();
        return new JVarsImpl(context, p==null?null:p.vars());
    }

    @Override
    public JFunctions createFunctions(JContext context) {
        JContext p = context.parent();
        return new JFunctionsImpl(context, p==null?null:p.functions());
    }

    @Override
    public JParsers createParsers(JContext context) {
        JContext p = context.parent();
        return new DefaultJParsers(context, p==null?null:p.parsers());
    }

    @Override
    public JTokens createTokens(JContext context, JTokenConfig tokenConfig) {
        JContext p = context.parent();
        return new DefaultJTokens(context, p==null?null:p.tokens(),tokenConfig);
    }

    @Override
    public JOperators createOperators(JContext context) {
        JContext p = context.parent();
        return new DefaultJOperators(context,
                (JFunctionsImpl) context.functions(),
                p==null?null:p.operators());
    }

    @Override
    public JResolvers createResolvers(JContext context) {
        JContext p = context.parent();
        return new DefaultJResolvers(context,
                p==null?null:p.resolvers());
    }

    @Override
    public JTypes createTypes(JContext context, ClassLoader classLoader) {
        return new DefaultJTypes(context,classLoader);
    }

    @Override
    public JEvaluators createEvaluators(JContext context) {
        JContext p = context.parent();
        return new DefaultEvaluators(context,
                p==null?null:p.evaluators());
    }
}
