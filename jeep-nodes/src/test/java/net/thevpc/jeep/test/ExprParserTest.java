package net.thevpc.jeep.test;

import net.thevpc.jeep.*;
import net.thevpc.jeep.core.DefaultJeep;
import net.thevpc.jeep.core.imports.PlatformHelperImports;
import net.thevpc.jeep.core.nodes.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author thevpc
 */
public class ExprParserTest {

    public static void createEvaluator() {
        DefaultJeep e = new DefaultJeep();
        e.resolvers().importType(PlatformHelperImports.class);
        e.resolvers().addResolver(new JResolver() {
            @Override
            public JVar resolveVariable(String name, JContext context) {
                return context.vars().declareVar(name, String.class, name);
            }
        });
        e.operators().declarePrefixUnaryOperators("+", "-");
        e.operators().declareBinaryOperators("+", "-", "*");
        e.operators().declareBinaryOperators(",");
        e.operators().declareBinaryOperators(";");
        e.operators().declareListOperator("", e.types().forName(String.class.getName()), e.types().forName(Object.class.getName()),
                JOperatorPrecedences.getDefaultPrecedence(JOperator.infix("+")));
        e.parsers().setFactory(JDefaultNodeParser::new);
        e.evaluators().setFactory(new JEvaluatorFactory() {
            @Override
            public JEvaluator create(JContext context) {
                return new JEvaluator() {
                    @Override
                    public Object evaluate(JNode node, JInvokeContext context) {
                        JDefaultNode dn = (JDefaultNode) node;
                        switch (dn.id()) {
                            case JNodeDefaultIds.NODE_OP_BINARY_INFIX: {
                                JNodeInfixBinaryOperatorCall c = (JNodeInfixBinaryOperatorCall) dn;
                                System.out.println("evaluate NODE_OP_BINARY_INFIX(" + c.getName() + "): " + node);
                                Object a = evaluate(c.getArg1(), context);
                                Object b = evaluate(c.getArg2(), context);
                                return "(" + a + c.getName() + b + ")";
                            }
                            case JNodeDefaultIds.NODE_PARS: {
                                System.out.println("evaluate NODE_PARS: " + node);
                                JNodePars c = (JNodePars) dn;
                                JDefaultNode[] o = c.getItems();
                                if (o.length == 1) {
                                    return evaluate(o[0], context);
                                }
                                StringBuilder sb = new StringBuilder("(");
                                for (int i = 0; i < o.length; i++) {
                                    Object a = evaluate(o[i], context);
                                    if (i > 0) {
                                        sb.append(",");
                                    }
                                    sb.append(a);
                                }
                                sb.append(")");
                                return sb.toString();
                            }
                            case JNodeDefaultIds.NODE_FUNCTION_CALL: {
                                JNodeFunctionCall c = (JNodeFunctionCall) dn;
                                System.out.println("evaluate NODE_FUNCTION_CALL(" + c.getName() + "): " + node);
                                switch (c.getName()) {
                                    case "": {
                                        StringBuilder sb = new StringBuilder();
                                        JDefaultNode[] o = c.getArgs();
                                        for (int i = 0; i < o.length; i++) {
                                            Object a = evaluate(o[i], context);
                                            if (i > 0) {
                                                sb.append("_");
                                            }
                                            sb.append(a);
                                        }
                                        return sb.toString();
                                    }
                                }
                                StringBuilder sb = new StringBuilder();
                                sb.append(c.getName());
                                sb.append("(");
                                JDefaultNode[] o = c.getArgs();
                                for (int i = 0; i < o.length; i++) {
                                    Object a = evaluate(o[i], context);
                                    if (i > 0) {
                                        sb.append(",");
                                    }
                                    sb.append(a);
                                }
                                sb.append(")");
                                return sb.toString();
                            }
                            case JNodeDefaultIds.NODE_VAR_NAME: {
                                System.out.println("evaluate NODE_VAR_NAME : " + node);
                                JNodeVarName c = (JNodeVarName) dn;
                                return c.getName();
                            }
                            case JNodeDefaultIds.NODE_BRACES: {
                                System.out.println("evaluate NODE_BRACES : " + node);
                                JNodeBraces c = (JNodeBraces) dn;
                                JDefaultNode[] o = c.getItems();
                                StringBuilder sb = new StringBuilder("{");
                                for (int i = 0; i < o.length; i++) {
                                    Object a = evaluate(o[i], context);
                                    if (i > 0) {
                                        sb.append(",");
                                    }
                                    sb.append(a);
                                }
                                sb.append("}");
                                return sb.toString();
                            }
                        }
                        return node;
                    }
                };
            }
        });

//        System.out.println(e.evaluate("2*3 + 4 * 5"));
        JContext eval = e.newContext();
        Object tt;

        System.out.println(">> " + "(a b t) ; c d ; e f");
        tt = eval.evaluate("(a b t) ; c d ; e f", null);
        String ss = tt.toString();
        Assertions.assertEquals("((a_b_t;c_d);e_f)", ss);
        System.out.println(tt);

        System.out.println(">> " + "{(a b t) ; c d ; e f}");
        tt = eval.evaluate("{(a b t) ; c d ; e f}", null);
        ss = tt.toString();
        Assertions.assertEquals("{((a_b_t;c_d);e_f)}", ss);
        System.out.println(ss);
    }

    @Test
    public void testExpr() {
        createEvaluator();
    }
}
