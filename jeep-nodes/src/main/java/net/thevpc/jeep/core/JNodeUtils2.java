package net.thevpc.jeep.core;

import net.thevpc.jeep.*;
import net.thevpc.jeep.core.eval.JEvaluableNode;
import net.thevpc.jeep.core.nodes.*;
import net.thevpc.jeep.*;
import net.thevpc.jeep.core.nodes.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JNodeUtils2 {

    public static JType getTypeOrNull(JDefaultNode arg) {
        if (arg == null) {
            return null;
        }
        JType t = arg.getType();
        if (t == null) {
            if (
                    arg instanceof JNodeLiteral
                            && ((JNodeLiteral) arg).getValue() == null
            ) {
                //this is ok
            } else {
                return null;
            }
        }
        return t;
    }

    public static JNodeFunctionCall createFunctionCall(JFunction f, JDefaultNode... nargs) {
        JNodeFunctionCall jnf = new JNodeFunctionCall(f.getName(), nargs);
        jnf.setImpl(new JInvokablePrefilled(f, JNodeUtils2.getEvaluatables((JDefaultNode[])nargs)));
        jnf.setType(f.getReturnType());
        return jnf;
    }

    public static String toPar(JNode e) {
        if (!formatRequirePar(e)) {
            return e.toString();
        }
        return "(" + e.toString() + ")";
    }

    public static boolean formatRequirePar(JNode e) {
        if (e instanceof JNodeLiteral
                || e instanceof JNodeArray
                || e instanceof JNodeVariable) {
            return false;
        }
        return true;
    }

    public static Object getIncDefaultValue(Object u) {
        if (u == null) {
            return 1;
        }
        if (u instanceof JNodeLiteral) {
            u = ((JNodeLiteral) u).getValue();
        }
        if (u == null) {
            return null;
        }
        if (u instanceof Number) {
            return 1;
        }
        throw new IllegalArgumentException("Invalid inc default value for " + u.getClass());
    }

    public static JNode[] toArray(JNode node) {
        if (node == null) {
            return new JNode[0];
        }
        if (node instanceof JNodeFunctionCall) {
            JNodeFunctionCall op = (JNodeFunctionCall) node;
            if (op.isUnary("")) {
                JNode[] a = op.getArgs();
                if (a.length == 1 && a[0] instanceof JNodeArray) {
                    List<JNode> ok = new ArrayList<JNode>();
                    for (JNode value : ((JNodeArray) a[0]).getValues()) {
                        ok.addAll(Arrays.asList(toArray(value)));
                    }
                    return ok.toArray(new JNode[0]);
                }
                return a;
            }
        }
        return new JNode[]{node};
    }

    public static boolean isTypeSet(JNode... arg) {
        for (JNode jNode : arg) {
            if (!isTypeSet(jNode)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isTypeSet(JDefaultNode arg) {
        if (arg == null) {
            return false;
        }
        JType t = arg.getType();
        if (t == null) {
            if (
                    arg instanceof JNodeLiteral
                            && ((JNodeLiteral) arg).getValue() == null
            ) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    public static JType getType(JDefaultNode arg) {
        if (arg == null) {
            return null;
        }
        JType t = arg.getType();
        if (t == null) {
            if (
                    arg instanceof JNodeLiteral
                            && ((JNodeLiteral) arg).getValue() == null
            ) {
                //this is ok
            } else {
                throw new JParseException("Type Not Found for Node " + arg.getClass().getSimpleName() + " as " + arg);
            }
        }
        return t;
    }

    public static JType[] getTypesOrNull(JDefaultNode[] args) {
        if (args == null) {
            return null;
        }
        JType[] argTypes = new JType[args.length];
        for (int i = 0; i < argTypes.length; i++) {
            JType typeOrNull = getTypeOrNull(args[i]);
            if (typeOrNull == null) {
                return null;
            }
            argTypes[i] = typeOrNull;
        }
        return argTypes;
    }

    public static JType[] getTypes(JDefaultNode[] args) {
        if (args == null) {
            return null;
        }
        JType[] argTypes = new JType[args.length];
        for (int i = 0; i < argTypes.length; i++) {
            argTypes[i] = getType(args[i]);
        }
        return argTypes;
    }

    public static JEvaluable[] getEvaluatables(JDefaultNode... args) {
        JType[] ntypes = getTypes(args);
        JEvaluable[] ev = new JEvaluable[args.length];
        for (int i = 0; i < ev.length; i++) {
            ev[i] = new JEvaluableNode(args[i], ntypes[i]);
        }
        return ev;
    }

    public static JType firstCommonSuperType(JDefaultNode arg1, JDefaultNode arg2) {
        if (isTypeSet(arg1) && isTypeSet(arg2)) {
            JType t1 = arg1.getType();
            JType t2 = arg2.getType();
            if (t1 == null) {
                return t2;
            }
            if (t2 == null) {
                return t1;
            }
            return t1.firstCommonSuperType(t2);
        }
        return null;
    }
}
