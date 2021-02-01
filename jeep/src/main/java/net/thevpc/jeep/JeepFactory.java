package net.thevpc.jeep;

public interface JeepFactory {

    JCompilerLog createLog(JContext context);

    JVars createVars(JContext context);

    JFunctions createFunctions(JContext context);

    JParsers createParsers(JContext context);

    JTokens createTokens(JContext context, JTokenConfig tokenConfig);

    JOperators createOperators(JContext context);

    JResolvers createResolvers(JContext context);

    JTypes createTypes(JContext context, ClassLoader classLoader);

    JEvaluators createEvaluators(JContext context);
//        this.functions = new JFunctionsImpl(this, parentContext.functions());
//        this.operators = new DefaultJOperators(this, (JFunctionsImpl) functions, parentContext.operators());
//        this.resolvers = new DefaultJResolvers(this,parentContext.resolvers());
//        this.types = new DefaultJTypes(parentContext.types(),parentContext.types().hostClassLoader());
//        this.parsers=new DefaultJParsers(this,parentContext.parsers());
//        this.tokens=new DefaultJTokens(this,parentContext.tokens(),null);
//        this.evaluators = new DefaultEvaluators(this,parentContext.evaluators());
}
