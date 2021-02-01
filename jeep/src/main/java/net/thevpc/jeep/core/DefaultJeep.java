/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.core;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.thevpc.jeep.*;
import net.thevpc.jeep.core.imports.PlatformHelperImports;
import net.thevpc.jeep.core.tokens.JTokenDef;
import net.thevpc.jeep.impl.AbstractJContext;
import net.thevpc.jeep.impl.DefaultJeepFactory;
import net.thevpc.jeep.impl.JContextImpl;

/**
 * <i>Mathematic expression evaluator.</i> Supports the following functions: +,
 * -, *, /, ^, %, cos, sin, tan, acos, asin, atan, sqrt, sqr, log, min, max,
 * ceil, floor, absdbl, neg, rndr.<br>
 * <pre>
 * Sample:
 * MathEvaluator m = new MathEvaluator();
 * m.declare("x", 15.1d);
 * System.out.println( m.evaluate("-5-6/(-2) + sqr(15+x)") );
 * </pre>
 *
 * @author Taha BEN SALAH
 * @version 1.0 %date April 2008
 */
public class DefaultJeep extends AbstractJContext implements Jeep {

    private JVars vars;
    private JFunctions functions;
    private JOperators operators;
    private JResolvers resolvers;
    private JTypes types;
    private JTokens tokens;
    private JParsers parsers;
    private JEvaluators evaluators;
    private JeepFactory factory;
    private JCompilerLog log;
    private ClassLoader classLoader;

    public DefaultJeep() {
        this(null, null);
    }

    /**
     * *
     * creates an empty MathEvaluator. You need to use setExpression(String s)
     * to assign a math expression string to it.
     */
    public DefaultJeep(JeepFactory factory, ClassLoader classLoader) {
        if (factory == null) {
            factory = new DefaultJeepFactory();
        }
        if (classLoader == null) {
            classLoader = Thread.currentThread().getContextClassLoader();
        }
        this.factory = factory;
        this.classLoader = classLoader;
        this.log = factory.createLog(this);
        this.vars = factory.createVars(this);
        this.functions = factory.createFunctions(this);
        this.operators = factory.createOperators(this);
        this.resolvers = factory.createResolvers(this);
        this.types = factory.createTypes(this, classLoader);
        this.tokens = factory.createTokens(this, new JTokenConfigDefinition(null));
        this.parsers = factory.createParsers(this);
        this.evaluators = factory.createEvaluators(this);
    }


    @Override
    public JCompilerLog log() {
        return log;
    }

    @Override
    public JContext log(JCompilerLog log) {
        this.log = log == null ? manager().factory().createLog(this) : log;
        return this;
    }

    @Override
    public JeepFactory factory() {
        return factory;
    }

    @Override
    public JContext parent() {
        return null;
    }

    public JEvaluators evaluators() {
        return evaluators;
    }

    public static String toStr(String name, JNode... args) {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        if (args != null) {
            sb.append("(");
            for (int i = 0, argsLength = args.length; i < argsLength; i++) {
                if (i > 0) {
                    sb.append(",");
                }
                JNode arg = args[i];
                sb.append(arg);
            }
            sb.append(")");
        }
        return sb.toString();
    }

//    public static Class toArrayClass(Class argType) {
//        try {
//            return Class.forName("[L" + argType.getName() + ";");
//        } catch (ClassNotFoundException ex) {
//            throw new IllegalArgumentException("Should never happen");
//        }
//    }
    @Override
    public JTokens tokens() {
        return tokens;
    }

    //    public Object evaluate(JNode node) {
//        if (JSharedContext.getCurrent() == null) {
//            return JSharedContext.invokeSilentCallable(this, new JSilentCallable<Object>() {
//                @Override
//                public Object call() {
//                    return node.evaluate(DefaultJeep.this);
//                }
//            });
//        } else {
//            return node.evaluate(DefaultJeep.this);
//        }
//    }
    @Override
    public JContext newContext() {
        return new JContextImpl(this);
    }

    @Override
    public void debug(String message) {
        //System.out.println("DEBUG "+message);
    }

    @Override
    public JVars vars() {
        return vars;
    }

    @Override
    public JTypes types() {
        return types;
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
    public JParsers parsers() {
        return parsers;
    }

    //    public JVar getVariable(String varName) {
//        JVar d = localVariables.get(varName);
//        if (d == null) {
//            d = definition.getVariable(varName);
//        }
//        return d;
//    }
    @Override
    public JNode parse(String expression) {
        return parsers().of(expression).parse();
    }

    @Override
    public Jeep manager() {
        return this;
    }

    //    Object getVariableValue(String varName, Jeep evaluator) {
//        JVar v = getVariable(varName);
//        return v.getValue(evaluator);
//    }
    public Jeep configureDefaults() {
        operators().declarePrefixUnaryOperators("-", "!", "~");
        operators().declareBinaryOperators("+", "-", "*", "/", "<", "<=", ">", ">=", "=", "!=");
        resolvers().importType(types().forName(PlatformHelperImports.class.getName()));
        return this;
    }

//    @Override
//    public JConverter1[] getConverters(Class operandType) {
//        LinkedHashSet<JConverter1> allImplicitConverters = new LinkedHashSet<>();
//        allImplicitConverters.add(null);
//        for (JConverter1 ic : JeepUtils.getTypeImplicitConversions(operandType)) {
//            if (ic != null) {
//                allImplicitConverters.add(ic);
//            }
//        }
//        for (JResolver resolver : resolvers().getResolvers()) {
//            JConverter1[] next = resolver.resolveImplicitConverters(operandType);
//            if (next != null) {
//                for (JConverter1 ic : next) {
//                    allImplicitConverters.add(ic);
//                }
//            }
//        }
//        return allImplicitConverters.toArray(new JConverter1[0]);
//    }
    //    @Override
//    public Object evaluateFunction(String name, JNode... args) {
//        return evaluate(resolveFunctionCallNode(name, args));
//    }
    private Object invokeInContext(JSilentCallable invocable) {
        if (JSharedContext.getCurrent() == null) {
            return JSharedContext.invokeSilentCallable(this, invocable);
        } else {
            return invocable.call();
        }
    }

    public void generateTokensClass(PrintStream out, String className) {
        if (out == null) {
            out = System.out;
        }
        String prefix = "    ";
        String simpleClassName = null;
        String simplePackage = null;
        if (className != null) {
            int i = className.lastIndexOf('.');
            if (i > 0) {
                simplePackage = className.substring(0, i);
                simpleClassName = className.substring(i + 1);
            } else {
                simpleClassName = className;
            }
        }
        if (simplePackage != null) {
            out.println("package " + simplePackage + ";");
            out.println();
        }
        if (simpleClassName != null) {
            out.println("public final class " + simpleClassName + " {");
            out.println(prefix + "private " + simpleClassName + "() {");
            out.println(prefix + "}");
            out.println();
        }
        Map<String, List<JTokenDef>> visited = new LinkedHashMap<>();

        out.println(prefix + "/**");
        out.println(prefix + " * End of File (no token to read)");
        out.println(prefix + " * <pre>");
        out.println(prefix + " * ID         : EOF");
        out.println(prefix + " * ID_NAME    : EOF");
        out.println(prefix + " * TYPE_ID    : EOF");
        out.println(prefix + " * TYPE_NAME  : EOF");
        out.println(prefix + " * STATE_ID   : <ANY>");
        out.println(prefix + " * STATE_NAME : N/A");
        out.println(prefix + " * LAYOUT     : <ANY>");
        out.println(prefix + " * </pre>");
        out.println(prefix + " */");
        out.println(prefix + "public static final int EOF = -1;");

        for (JTokenDef tokenDefinition : tokens().tokenDefinitions()) {
            List<JTokenDef> list = visited.computeIfAbsent(tokenDefinition.idName, k -> new ArrayList<>());
            list.add(tokenDefinition);
        }
        for (List<JTokenDef> value : visited.values()) {
            Set<Integer> ids = value.stream().map(x -> x.id).collect(Collectors.toCollection(LinkedHashSet::new));
            Set<String> idNames = value.stream().map(x -> x.idName).collect(Collectors.toCollection(LinkedHashSet::new));
            Set<Integer> typeIds = value.stream().map(x -> x.ttype).collect(Collectors.toCollection(LinkedHashSet::new));
            Set<String> typeNames = value.stream().map(x -> x.ttypeName).collect(Collectors.toCollection(LinkedHashSet::new));
            Set<Integer> stateIds = value.stream().map(x -> x.stateId).collect(Collectors.toCollection(LinkedHashSet::new));
            Set<String> stateNames = value.stream().map(x -> x.stateName).collect(Collectors.toCollection(LinkedHashSet::new));
            Set<String> layouts = value.stream().map(x -> escapeComments(x.imageLayout)).collect(Collectors.toCollection(LinkedHashSet::new));
            out.println();
            out.println(prefix + "/**");
            out.println(prefix + " * Token Id for " + elemStr(idNames));
            out.println(prefix + " * <pre>");
            out.println(prefix + " * ID         : " + elemStr(ids));
            out.println(prefix + " * ID_NAME    : " + elemStr(idNames));
            out.println(prefix + " * TYPE_ID    : " + elemStr(typeIds));
            out.println(prefix + " * TYPE_NAME  : " + elemStr(typeNames));
            out.println(prefix + " * STATE_ID   : " + elemStr(stateIds));
            out.println(prefix + " * STATE_NAME : " + elemStr(stateNames));
            out.println(prefix + " * LAYOUT     : " + elemStr(layouts));
            out.println(prefix + " * </pre>");
            out.println(prefix + " */");
            out.println(prefix + "public static final int " + elemStr(idNames) + " = " + elemStr(ids) + ";");
            if (ids.size() > 1) {
                out.println("/// ***************************** ");
                out.println("/// ERROR DETECTED :: ");
                for (JTokenDef tokenDefinition : tokens().tokenDefinitions()) {
                    if (ids.contains(tokenDefinition.id)) {
                        out.println("/// " + tokenDefinition);
                    }
                }
                out.println("/// ***************************** ");
//                throw new IllegalArgumentException("ERROR IDS");
            }
        }
        if (simpleClassName != null) {
            out.println("}");
        }
    }

    private static String elemStr(Set ids) {
        return ids.size() == 1 ? ids.toArray()[0].toString() : ids.toString();
    }

    private static final String escapeComments(String s) {
        if (s == null) {
            return "null";
        }
        return s.replace("*/", "*&#47;");
    }
}
