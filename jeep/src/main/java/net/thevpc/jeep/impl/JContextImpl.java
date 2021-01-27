/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.impl;

import net.thevpc.jeep.*;
import net.thevpc.jeep.*;

/**
 * @author thevpc
 */
public class JContextImpl extends AbstractJContext {

    private final JContext parentContext;
    private final JVars vars;
    private final JFunctions functions;
    private final JOperators operators;
    private final JResolvers resolvers;
    private final JTypes types;
    private final JParsers parsers;
    private final JTokens tokens;
    private final JEvaluators evaluators;

    public JContextImpl(JContext context) {
        this.parentContext = context;
        JeepFactory factory = context.manager().factory();
        this.vars = factory.createVars(this);
        this.functions = factory.createFunctions(this);
        this.operators = factory.createOperators(this);
        this.resolvers = factory.createResolvers(this);
        this.types = factory.createTypes(this,parentContext.types().hostClassLoader());
        this.parsers=factory.createParsers(this);
        this.tokens=factory.createTokens(this,null);
        this.evaluators = factory.createEvaluators(this);
    }

    @Override
    public JEvaluators evaluators() {
        return evaluators;
    }

    @Override
    public JParsers parsers() {
        return parsers;
    }

    @Override
    public JTypes types() {
        return types;
    }

    @Override
    public JContext newContext() {
        return new JContextImpl(this);
    }

    @Override
    public void debug(String string) {
        parentContext.debug(string);
    }

    @Override
    public JVars vars() {
        return vars;
    }

    @Override
    public JFunctions functions() {
        return functions;
    }

    @Override
    public JOperators operators() {
        return operators;
    }

    @Override
    public JResolvers resolvers() {
        return resolvers;
    }

    @Override
    public JNode parse(String expression) {
        return parsers().of(expression).parse();
    }

    @Override
    public Jeep manager() {
        return parentContext.manager();
    }

    public JContext parent(){
        return parentContext;
    }
    @Override
    public JTokens tokens() {
        return tokens;
    }
}
