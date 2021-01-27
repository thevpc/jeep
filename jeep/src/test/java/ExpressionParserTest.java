//import net.thevpc.common.strings.*;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//
///**
// * Created by vpc on 4/16/17.
// */
//public class ExpressionParserTest {
//    @Test
//    public void test1(){
//        ExpressionParser e = new ExpressionParser();
//        e.setCaseSensitive(false);
//        e.declareConst("a", 15);
//        e.declareConst("c", 15);
//        e.declareBinaryOperator(" ", 1,",");
//        e.declareBinaryOperator("+", 1);
//        e.declareBinaryOperator("*", 2);
//        try {
//            Expr expr = e.parse("(a 'b' c)*A+pi()+sin(25)");
//            final String str=expr.toString();
//            Object o = e.evaluate(expr, new AbstractExpressionManager() {
//                @Override
//                public Object evalVar(ExprContext context, ExprVar var) {
//                    return var.getName();
//                }
//
//                @Override
//                public Object evalStr(ExprContext context, ExprStr strVal) {
//                    switch (strVal.getValue()){
//                        case "b":{
//                            return 15;
//                        }
//                    }
//                    return 0;
//                }
//
//                @Override
//                public Object evalFunction(ExprContext context, ExprFunction fct) {
//                    switch (fct.getName()) {
//                        case "pi": {
//                            return Math.PI;
//                        }
//                        case "sin": {
//                            Number eval = (Number)fct.getParameter(0).eval(context, this);
//                            return Math.sin(eval.doubleValue());
//                        }
//                    }
//                    throw new UnsupportedOperationException(fct.getName());
//                }
//
//                @Override
//                public Object evalOperator(ExprContext context, ExprOperator op) {
//                    Number a = (Number) (op.getOperand(0).eval(context, this));
//                    Number b = (Number) (op.getOperand(1).eval(context, this));
//                    switch (op.getName()) {
//                        case "+": {
//                            return a.doubleValue() + b.doubleValue();
//                        }
//                        case "*": {
//                            return a.doubleValue() * b.doubleValue();
//                        }
//                        case " ": {
//                            return a.doubleValue() + b.doubleValue();
//                        }
//                    }
//                    throw new UnsupportedOperationException(op.getName());
//                }
//            });
//            Assertions.assertEquals("","(((((const(a, 15) 'b') const(c, 15))*const(A, 15))+pi())+sin(val(25)))",str);
//            Assertions.assertEquals("",678.009240903492,o);
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }
//    }
//}
