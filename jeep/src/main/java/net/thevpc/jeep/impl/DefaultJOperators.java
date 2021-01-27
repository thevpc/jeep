/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.impl;

import net.thevpc.jeep.*;
import net.thevpc.jeep.impl.functions.JFunctionsImpl;
import net.thevpc.jeep.impl.functions.JSignature;
import net.thevpc.jeep.util.JTypeUtils;
import net.thevpc.jeep.util.JeepUtils;
import net.thevpc.jeep.*;

import java.util.*;

/**
 * @author thevpc
 */
public class DefaultJOperators implements JOperators {

    private final JFunctionsImpl functions;
    private final JOperators parent;
    private final Map<JOperator, Integer> opPrecedence = new HashMap<>();
    private Set<String> opNames;
    private final Set<String> abstractListOpNames = new HashSet<>();

    private final Map<String, String> operatorsAliases = new HashMap<>();
    private final Map<String, String> canonicalToAbsolute = new HashMap<>();
    private JContext context;

    public DefaultJOperators(JContext context, JFunctionsImpl functionsRepository, JOperators parent) {
        this.context = context;
        this.functions = functionsRepository;
        this.parent = parent;
    }

    public JOperators declareOperator(JOperator operator) {
        declareOperator(operator, JOperatorPrecedences.getDefaultPrecedence(operator));
        return this;
    }

    @Override
    public JOperators undeclareOperator(JOperator operator) {
        if (opPrecedence.remove(operator) != null) {
            functions.invalidateFunctionsCache(operator.name());
            opNames = null;
        }
        return this;
    }

    @Override
    public JOperators declareOperator(JOperator operator, int precedence) {
        if (operator.name().length() > 0) {
            JTokenConfigBuilder config = new JTokenConfigBuilder(context.tokens().config());
            config.addOperator(operator.name());
            context.tokens().setConfig(config);
        }
        opPrecedence.put(operator, precedence);
        opNames = null;
        functions.invalidateFunctionsCache(operator.name());
        return this;
    }

    @Override
    public JOperators declareBinaryOperator(String operator) {
        declareOperator(JOperator.infix(operator));
        return this;
    }

    @Override
    public JOperators declarePrefixUnaryOperator(String operator) {
        declareOperator(JOperator.prefix(operator));
        return this;
    }

    @Override
    public JOperators declareBinaryOperator(String operator, int precedence) {
        declareOperator(JOperator.infix(operator), precedence);
        return this;
    }

    @Override
    public JOperators declareBinaryOperators(int precedence, String... operators) {
        for (String operator : operators) {
            declareBinaryOperator(operator, precedence);
        }
        return this;
    }

    @Override
    public JOperators declareBinaryOperators(String... operator) {
        for (String o : operator) {
            declareBinaryOperator(o);
        }
        return this;
    }

    @Override
    public JOperators declareListOperator(final String name, int operatorPrecedence) {
        return declareListOperator(name, JTypeUtils.forObject(context.types()), operatorPrecedence);
    }

    @Override
    public JOperators declareListOperator(String name, Class argType, int operatorPrecedence) {
        return declareListOperator(name, context.types().forName(argType.getName()), operatorPrecedence);
    }

    @Override
    public JOperators declareListOperator(String name, String argType, int operatorPrecedence) {
        return declareListOperator(name, context.types().forName(argType), operatorPrecedence);
    }

    @Override
    public JOperators declareListOperator(final String name, JType argType, int operatorPrecedence) {
        return declareListOperator(name, argType, argType, operatorPrecedence/*, new JInvoke() {
            @Override
            public Object invoke(JInvokeContext context) {
                JEvaluable[] arguments = context.arguments();
                if (arguments.length == 1) {
                    Object y = context.evaluate(arguments[0]);
                    if (y instanceof Object[]) {
                        return new JUplet(name, (Object[]) y, new JType[]{argType});
                    } else {
                        return new JUplet(name, new Object[]{y}, new JType[]{argType});
                    }
                } else {
                    Object[] ll = new Object[arguments.length];
                    for (int i = 0; i < arguments.length; i++) {
                        ll[i] = context.evaluate(arguments[i]);
                    }
                    return new JUplet(name, ll, new JType[]{argType.toArray()});
                }
            }
        }*/);
    }

    @Override
    public boolean isListSeparator(String op) {
        if (parent != null) {
            return parent.isListSeparator(op);
        }
        return abstractListOpNames.contains(op);
    }

    public JOperators undeclareOperator(String op, JType... operands) {
        undeclareOperatorAlias(op, operands);
        JSignature asig1 = new JSignature(getCanonicalName(op), operands, true);
        JSignature asig2 = new JSignature(getCanonicalName(op), operands, true);
        for (JSignature asig : new JSignature[]{asig1, asig2}) {
            functions.removeFunction(asig);
            for (Map.Entry<String, String> entry : new HashMap<String, String>(operatorsAliases).entrySet()) {
                if (entry.getValue().equals(op)) {
                    undeclareOperatorAlias(entry.getKey(), operands);
                }
            }
            opNames = null;
        }
        return this;
    }

    @Override
    public JOperators declareOperatorAlias(String alias, String referenceOp, boolean varArgs, JType... operands) {
        varArgs = false;//!!!!
        String canonicalName = getCanonicalName(referenceOp);
        JSignature sig = new JSignature(canonicalName, operands, false);
        String aliasCanonicalName = getCanonicalName(alias);
//        JSignature aliasSig = new JSignature(aliasCanonicalName, operands);
        JeepUtils.validateFunctionName(alias);
        JFunction u = functions.findFunctionMatchOrNull(sig, JCallerInfo.NO_CALLER);
        if (u == null
                && !opNames().contains(canonicalName)
                && !operatorsAliases.containsKey(canonicalName)) {
            throw new IllegalArgumentException("operator not found : " + referenceOp);
        }
        if (operatorsAliases.containsKey(aliasCanonicalName)) {
//            String s2 = operatorsAliases.get(aliasCanonicalName);
//            if (!canonicalName.equals(canonicalName)) {
//                throw new IllegalArgumentException("Alias '" + alias + "' already bound to : " + u.getOp().getName());
//            } else {
//                //ignore !!
//            }
        } else {
            operatorsAliases.put(aliasCanonicalName, canonicalName);
            functions.invalidateFunctionsCache();
            if (aliasCanonicalName.length() > 0) {
                JTokenConfigBuilder config = new JTokenConfigBuilder(context.tokens().config());
                config.addOperator(aliasCanonicalName);
                context.tokens().setConfig(config);
            }
        }

        functions.invalidateFunctionsCache(new JSignature(alias, operands, false));
        return this;
    }

    @Override
    public JOperators declareOperator(JFunction fct) {
        String opName = fct.getName();
        if (opName.length() > 0) {
            JTokenConfigBuilder config = new JTokenConfigBuilder(context.tokens().config());
            config.addOperator(opName);
            context.tokens().setConfig(config);
        }
        functions.declare(fct);
        return this;
    }

//    @Override
//    public JOperators declareListOperator(String name, String returnType, String argType, int operatorPrecedence, JInvoke operator) {
//        return declareListOperator(name,
//                context.types().forName(returnType),
//                context.types().forName(argType),
//                operatorPrecedence, operator
//        );
//    }
    @Override
    public JOperators declareListOperator(String name, String returnType, String argType, int operatorPrecedence) {
        return declareListOperator(name,
                context.types().forName(returnType),
                context.types().forName(argType),
                operatorPrecedence
        );
    }

//    @Override
//    public JOperators declareListOperator(String name, Class returnType, Class argType, int operatorPrecedence, JInvoke operator) {
//        return declareListOperator(name,
//                context.types().forName(returnType.getName()),
//                context.types().forName(argType.getName()),
//                operatorPrecedence, operator
//        );
//    }
    @Override
    public JOperators declareListOperator(String name, Class returnType, Class argType, int operatorPrecedence) {
        return declareListOperator(name,
                context.types().forName(returnType.getName()),
                context.types().forName(argType.getName()),
                operatorPrecedence
        );
    }

//    @Override
//    public JOperators declareListOperator(String name, JType returnType, JType argType, int operatorPrecedence, JInvoke operator) {
//        JOperators e = declareOperator(new JListOperator(operator, name, returnType, argType));
//        declareOperator(JOperator.list(name),operatorPrecedence);
//        abstractListOpNames.add(name);
//        return e;
//    }
    @Override
    public JOperators declareListOperator(String name, JType returnType, JType argType, int operatorPrecedence) {
        //JOperators e = declareOperator(new JListOperator(operator, name, returnType, argType));
        declareOperator(JOperator.list(name), operatorPrecedence);
        abstractListOpNames.add(name);
        return this;
    }

    @Override
    public boolean isPrefixUnaryOperator(String op) {
        return isOperator(JOperator.prefix(op));
    }

    @Override
    public boolean isOperator(JOperator operator) {
        if (parent != null) {
            return parent.isOperator(operator);
        }
        if (opPrecedence.containsKey(operator)) {
            return true;
        }
        final String n = operator.name();
        final String t = operatorsAliases.get(n);
        if (t != null) {
            return isOperator(new JOperator(t, operator.type()));
        }
        return false;
    }

    @Override
    public boolean isPostfixUnaryOperator(String op) {
        return isOperator(JOperator.postfix(op));
    }

    @Override
    public JOperators declareCStyleOperators() {
        declarePostfixUnaryOperators("++", "--");
        declarePrefixUnaryOperators("+", "-", "++", "--", "!", "~");
        declareBinaryOperators("="); //assign
        declareBinaryOperators("+", "-", "*", "/", "%", "^");
        declareBinaryOperators("<", ">", "<=", ">=", "==", "!=", "&", "&&", "|", "||");
        return this;
    }

    @Override
    public JOperators declarePrefixUnaryOperator(String operator, int precedence) {
        declareOperator(JOperator.prefix(operator), precedence);
        return this;
    }

    @Override
    public JOperators declarePrefixUnaryOperators(String... operator) {
        for (String o : operator) {
            declarePrefixUnaryOperator(o);
        }
        return this;
    }

    @Override
    public JOperators declarePrefixUnaryOperators(int precedence, String... operator) {
        for (String o : operator) {
            declarePrefixUnaryOperator(o, precedence);
        }
        return this;
    }

    @Override
    public JOperators declarePostfixUnaryOperators(int precedence, String... operator) {
        for (String o : operator) {
            declarePostfixUnaryOperator(o, precedence);
        }
        return this;
    }

    @Override
    public JOperators undeclarePrefixUnaryOperator(String operator) {
        undeclareOperator(JOperator.prefix(operator));
        return this;
    }

    @Override
    public JOperators undeclarePrefixUnaryOperators(String... operators) {
        for (String s : operators) {
            undeclarePrefixUnaryOperator(s);
        }
        return this;
    }

    @Override
    public JOperators declarePostfixUnaryOperator(String operator) {
        declareOperator(JOperator.postfix(operator));
        return this;
    }

    @Override
    public JOperators declarePostfixUnaryOperator(String operator, int precedence) {
        declareOperator(JOperator.postfix(operator), precedence);
        return this;
    }

    @Override
    public JOperators declareSpecialOperator(String operator) {
        declareOperator(JOperator.special(operator), 1);
        return this;
    }

    @Override
    public JOperators declareSpecialOperators(String... operators) {
        for (String s : operators) {
            declareSpecialOperator(s);
        }
        return this;
    }

    @Override
    public JOperators declarePostfixUnaryOperators(String... operator) {
        for (String o : operator) {
            declarePostfixUnaryOperator(o);
        }
        return this;
    }

    @Override
    public JOperators undeclarePostfixUnaryOperator(String operator) {
        undeclareOperator(JOperator.postfix(operator));
        return this;
    }

    @Override
    public JOperators undeclarePostfixUnaryOperators(String... operators) {
        for (String s : operators) {
            undeclarePostfixUnaryOperator(s);
        }
        return this;
    }

    @Override
    public boolean isImplicitOperatorEnabled() {
        if (functions.findFunctionsByName("").length > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isOperator(String operator) {
        String canonicalName = getCanonicalName(operator);
        if (opNames().contains(canonicalName)) {
            return true;
        }

        final String t = operatorsAliases.get(operator);
        if (t != null) {
            if (opNames().contains(getCanonicalName(t))) {
                return true;
            }
        }

        if (parent != null) {
            return parent.isOperator(canonicalName);
        }

        return false;
    }

    ///////////////
    @Override
    public void undeclareOperatorAlias(String alias, JType[] operands) {
        String canonicalName = getCanonicalName(alias);
        operatorsAliases.remove(canonicalName);
        opNames = null;
        functions.undeclareAlias(alias);
    }

    @Override
    public boolean isBinaryOperator(String operator) {
        return isOperator(JOperator.infix(operator));
    }

    public String getCanonicalName(String name) {
        if (!context.tokens().config().isCaseSensitive()) {
            String canonical = name.toUpperCase();
            String old = canonicalToAbsolute.get(canonical);
            if (old == null) {
                canonicalToAbsolute.put(canonical, name);
                return canonical;
            } else if (old.equals(name)) {
                //okkay
                return canonical;
            } else {
                throw new IllegalArgumentException("Clash of names " + name + " <> " + old);
            }
        }
        return name;
    }

    public String getRootOperatorName(String alias) {
        String a = getCanonicalName(alias);
        int maxDepth = 100;
        HashSet<String> visited = new HashSet<>();
        while (true) {
            String old = operatorsAliases.get(a);
            if (old == null) {
                return a;
            } else {
                visited.add(a);
                a = old;
                maxDepth--;
                if (maxDepth < 0) {
                    throw new IllegalArgumentException("Cycle ref detected for alias " + alias + " -> " + visited);
                }
            }
        }
    }

    public Set<String> getAllNames(String name) {
        Map<String, Set<String>> c = buildCache();
        Set<String> v = c.get(name);
        if (v == null) {
            v = new LinkedHashSet<>();
            v.add(name);
        }
        return v;
    }

    private Map<String, Set<String>> buildCache() {
        Map<String, Set<String>> cache = new HashMap<>();
        for (Map.Entry<String, String> entry : operatorsAliases.entrySet()) {
            String a = entry.getKey();
            Set<String> g1 = cache.get(a);
            String b = entry.getValue();
            Set<String> g2 = cache.get(b);
            LinkedHashSet<String> g = new LinkedHashSet<>();
            g.add(getOriginalName(a));
            g.add(getOriginalName(b));
            if (g1 != null) {
                g.addAll(g1);
            }
            if (g2 != null) {
                g.addAll(g2);
            }
            for (String s : g) {
                cache.put(s, g);
            }
        }
        return cache;
    }

    public String getOriginalName(String name) {
        if (!context.tokens().config().isCaseSensitive()) {
            String o = canonicalToAbsolute.get(name);
            if (o != null) {
                return o;
            }
        }
        return name;
    }

    protected Set<String> opNames() {
        if (opNames == null) {
            Set<String> opNames = new HashSet<>();
            for (JOperator jOperator : opPrecedence.keySet()) {
                opNames.add(jOperator.name());
            }
            this.opNames = opNames;
        }
        return opNames;
    }

    @Override
    public int getOperatorPrecedence(JOperator operator) {
        Integer p = opPrecedence.get(operator);
        if (p == null) {
            final String n = operator.name();
            final String t = operatorsAliases.get(n);
            if (t != null) {
                p = opPrecedence.get(new JOperator(t, operator.type()));
                if(p!=null){
                    return p;
                }
            }
            if (parent != null) {
                return parent.getOperatorPrecedence(operator);
            }
            throw new IllegalArgumentException("Not an operator " + operator);
        }
        return p;
    }
}
