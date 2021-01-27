package net.thevpc.jeep.core;

import net.thevpc.jeep.*;
import net.thevpc.jeep.impl.functions.*;
import net.thevpc.jeep.util.JInvokeUtils;
import net.thevpc.jeep.util.JTypeUtils;
import net.thevpc.jeep.*;
import net.thevpc.jeep.impl.functions.*;

import java.util.*;
import java.util.stream.Collectors;

public class UtilClassJResolver implements JResolver {

    private final List<JType> importedTypes = new ArrayList<>();
    private final List<JType> importedMethods = new ArrayList<>();
    private final List<JType> importedFields = new ArrayList<>();
    private final boolean importsFirst;
    private final boolean checkEmptyArgMethods;

    public UtilClassJResolver(JType... types) {
        this(false, false, types);
    }

    public UtilClassJResolver(boolean importsFirst, boolean checkEmptyArgMethods, JType... types) {
        this.importsFirst = importsFirst;
        this.checkEmptyArgMethods = checkEmptyArgMethods;
        mergeToFirst(importedTypes, Arrays.asList(types));
    }

    private static <T> List<T> merge(Collection<T> a, Collection<T> b) {
        LinkedHashSet<T> t = new LinkedHashSet(a);
        t.addAll(b);
        return new ArrayList<>(t);
    }

    private static <T> List<T> mergeToFirst(List<T> a, Collection<T> b) {
        LinkedHashSet<T> t = new LinkedHashSet(a);
        t.addAll(b);
        a.clear();
        a.addAll(t);
        return a;
    }

    public boolean isCheckEmptyArgMethods() {
        return checkEmptyArgMethods;
    }

    public UtilClassJResolver addImportType(JType type) {
        mergeToFirst(importedTypes, Arrays.asList(type));
        return this;
    }

    public UtilClassJResolver addImportFields(JType type) {
        mergeToFirst(importedFields, Arrays.asList(type));
        return this;
    }

    public UtilClassJResolver addImportMethods(JType type) {
        mergeToFirst(importedMethods, Arrays.asList(type));
        return this;
    }

    @Override
    public JVar resolveVariable(String name, JContext context) {
        LinkedHashSet<JType> t = new LinkedHashSet<>(importedTypes);
        t.addAll(importedFields);
        //check for fields
        for (JType type : t) {
            JField field = type.findDeclaredFieldOrNull(name);
            if (field != null) {
                return JeepUtilsFactory.createStaticFieldVar(field);
            }
        }
        if (checkEmptyArgMethods) {
            JFunction f = resolveFunction(name, new JTypePattern[0], context);
            if (f != null) {
                return new JFunctionJVar(f, context);
            }
        }
        return null;
    }

    @Override
    public JFunction resolveFunction(String name, JTypePattern[] argTypes, JContext context) {
        JFunction r = resolveFunctionDef0(name, argTypes, context);
        if (r == null) {
            return null;
        }
        JConverter[] conv = new JConverter[argTypes.length];
        boolean someConv = false;
        for (int i = 0; i < conv.length; i++) {
            JType to = r.getSignature().argType(i);
            JTypePattern from = argTypes[i];
            if (!to.isAssignableFrom(from.getType())) {
                conv[i] = JTypeUtils.createTypeImplicitConversions(from, JTypePattern.of(to));
                someConv = true;
            }
        }
        if (someConv) {
            return new JFunctionConverted3(r, conv, null, r.getReturnType());
        } else {
            return r;
        }
    }

    @Override
    public JFunction[] resolveFunctionsByName(String name, int argsCount, JContext context) {
        List<JFunction> all = new ArrayList<>();
        for (JType type : importedTypes) {
            all.addAll(
                    Arrays.stream(type.getDeclaredMethods(new String[]{name}, argsCount, true))
                            .filter(x -> x.isStatic() && x.isPublic())
                            .map(x -> new JFunctionFromStaticMethod(x))
                            .collect(Collectors.toList())
            );
        }
        for (JType type : importedMethods) {
            all.addAll(
                    Arrays.stream(type.getDeclaredMethods(new String[]{name}, argsCount, true))
                            .filter(x -> x.isStatic() && x.isPublic())
                            .map(x -> new JFunctionFromStaticMethod(x))
                            .collect(Collectors.toList())
            );
        }
        return all.toArray(new JFunction[0]);
    }

    protected JFunction resolveFunctionDef0(String name, JTypePattern[] args, JContext context0) {
        JType aBoolean = JTypeUtils.forBoolean(context0.types());
        switch (name) {
            case "": {
                return null;
            }
            case "+": {
                if (args.length == 1) {
                    //unary
                    return JeepUtilsFactory.createIdentFunction(name, args[0].getType());
                } else if (args.length == 2) {
                    return createBinaryOperatorFunction(name, args, "add", "reverseAdd", "add", context0);
                }
                break;
            }
            case "-": {
                if (args.length == 1) {
                    //unary
                    JFunction m = createUnaryOperatorFunction(name, args, "neg", "neg", context0);
                    if (m != null) {
                        return m;
                    }
                } else if (args.length == 2) {
                    return createBinaryOperatorFunction(name, args, "sub", "reverseSub", "sub", context0);
                }
                break;
            }
            case "*": {
                if (args.length == 2) {
                    return createBinaryOperatorFunction(name, args, "mul", "reverseMul", "mul", context0);
                }
                break;
            }
            case "/": {
                if (args.length == 2) {
                    return createBinaryOperatorFunction(name, args, "div", "reverseDiv", "div", context0);
                }
                break;
            }
            case "^^": {
                if (args.length == 2) {
                    return createBinaryOperatorFunction(name, args, "xor", "reverseXor", "xor", context0);
                }
                break;
            }
            case "^": {
                if (args.length == 2) {
                    return createBinaryOperatorFunction(name, args, "pow", "reversePow", "xor", context0);
                }
                break;
            }
            case "~": {
                if (args.length == 1) {
                    //unary
                    JFunction m = createUnaryOperatorFunction(name, args, "tilde", "tilde", context0);
                    if (m != null) {
                        return m;
                    }
                } else if (args.length == 2) {
                    return createBinaryOperatorFunction(name, args, "pow", "reversePow", "pow", context0);
                }
                break;
            }
            case "**": {
                if (args.length == 2) {
                    return createBinaryOperatorFunction(name, args, "scalarProduct", "reverseScalarProduct", "scalarProduct", context0);
                }
                break;
            }
            case "||": {
                if (args.length == 2) {
                    return createBinaryOperatorFunction(name, args, "or", "reverseOr", "or", context0);
                }
                break;
            }
            case "|": {
                if (args.length == 2) {
                    return createBinaryOperatorFunction(name, args, "binaryOr", "reverseBinaryOr", "binaryOr", context0);
                }
                break;
            }
            case "&&": {
                if (args.length == 2) {
                    return createBinaryOperatorFunction(name, args, "and", "reverseAnd", "and", context0);
                }
                break;
            }
            case "&": {
                if (args.length == 2) {
                    return createBinaryOperatorFunction(name, args, "binaryAnd", "reverseBinaryAnd", "binaryAnd", context0);
                }
                break;
            }
            case "->": {
                if (args.length == 2) {
                    return createBinaryOperatorFunction(name, args, "rightArrow", "reverseRightArrow", "rightArrow", context0);
                }
                break;
            }
            case "<": {
                if (args.length == 2) {
                    JFunction n = createBinaryOperatorFunction(name, args, "compareTo", "reverseCompareTo", "compare", context0);
                    if (n != null) {
                        return new JFunctionConverted(n, null, new AbstractJConverter(Integer.TYPE, Boolean.TYPE, 3, context0.types()) {
                            @Override
                            public Object convert(Object value, JInvokeContext context) {
                                Integer v = (Integer) value;
                                return v != null && v.intValue() < 0;
                            }
                        });
                    }
                    return n;
                }
                break;
            }
            case "<=": {
                if (args.length == 2) {
                    JFunction n = createBinaryOperatorFunction(name, args, "compareTo", "reverseCompareTo", "compare", context0);
                    if (n != null) {
                        return new JFunctionConverted(n, null, new AbstractJConverter(Integer.TYPE, Boolean.TYPE, 3, context0.types()) {
                            @Override
                            public Object convert(Object value, JInvokeContext context) {
                                Integer v = (Integer) value;
                                return v != null && v.intValue() <= 0;
                            }
                        });
                    }
                    return n;
                }
                break;
            }
            case ">": {
                if (args.length == 2) {
                    JFunction n = createBinaryOperatorFunction(name, args, "compareTo", "reverseCompareTo", "compare", context0);
                    if (n != null) {
                        return new JFunctionConverted(n, null, new AbstractJConverter(Integer.TYPE, Boolean.TYPE, 3, context0.types()) {
                            @Override
                            public Object convert(Object value, JInvokeContext context) {
                                Integer v = (Integer) value;
                                return v != null && v.intValue() > 0;
                            }
                        });
                    }
                    return n;
                }
                break;
            }
            case ">=": {
                if (args.length == 2) {
                    JFunction n = createBinaryOperatorFunction(name, args, "compareTo", "reverseCompareTo", "compare", context0);
                    if (n != null) {
                        return new JFunctionConverted(n, null, new AbstractJConverter(Integer.TYPE, Boolean.TYPE, 3, context0.types()) {
                            @Override
                            public Object convert(Object value, JInvokeContext context) {
                                Integer v = (Integer) value;
                                return v != null && v.intValue() >= 0;
                            }
                        });
                    }
                    return n;
                }
                break;
            }
            case "==": {
                if (args.length == 2) {
                    return createBinaryOperatorFunction(name, args, "equals", "equals", "equals", context0);
                }
                break;
            }
            case "!=": {
                if (args.length == 2) {
                    JFunction n = createBinaryOperatorFunction(name, args, "equals", "equals", "equals", context0);
                    if (n != null) {
                        return new JFunctionConverted(n, null, new AbstractJConverter(Integer.TYPE, Boolean.TYPE, 3, context0.types()) {
                            @Override
                            public Object convert(Object value, JInvokeContext context) {
                                Boolean v = (Boolean) value;
                                return v != null && !v.booleanValue();
                            }
                        });
                    }
                    return n;
                }
                break;
            }
            case "<>": {
                if (args.length == 2) {
                    JFunction n = createBinaryOperatorFunction(name, args, "equals", "equals", "equals", context0);
                    if (n != null) {
                        return new JFunctionConverted(n, null, new AbstractJConverter(Integer.TYPE, Boolean.TYPE, 3, context0.types()) {
                            @Override
                            public Object convert(Object value, JInvokeContext context) {
                                Boolean v = (Boolean) value;
                                return v != null && !v.booleanValue();
                            }
                        });
                    }
                    return n;
                }
                break;
            }
            default: {
                JTypePattern[] argTypes = null;
                argTypes = args;
                for (JType type : importedTypes) {
                    final JMethod m = JInvokeUtils.getMatchingMethod(type, name, argTypes);
                    if (m != null) {
                        if (m.getSignature().isVarArgs()) {
                            return new JFunctionFromJMethod(name, m, argTypes);
                        }
                        int[] indices = new int[argTypes.length];
                        for (int i = 0; i < argTypes.length; i++) {
                            indices[i] = i;
                        }
                        return createMethodInvocationFunction(name, m, -1, indices);
                    }
                }
            }
        }
        return null;
    }

    private JFunction createBinaryOperatorFunction(String name, JTypePattern[] args, String directName, String reverseName, String staticName, JContext context) {
        JTypePattern arg1 = null;
        JTypePattern arg2 = null;
        arg1 = args[0];
        arg2 = args[1];
        int[] order = !(importsFirst) ? new int[]{1, 2} : new int[]{2, 1};
        LinkedHashSet<JType> t = new LinkedHashSet<>(importedTypes);
        t.addAll(importedMethods);
        for (int i = 0; i < order.length; i++) {
            switch (order[i]) {
                case 1: {
                    if(arg1.isType()) {
                        JMethod m = JInvokeUtils.getMatchingMethod(arg1.getType(), directName, arg2);
                        if (m != null) {
                            return createMethodInvocationFunction(name, m, 0, 1);
                        }
                    }

                    if(arg2.isType()) {
                        JMethod rm = JInvokeUtils.getMatchingMethod(arg2.getType(), reverseName, arg1);
                        if (rm != null) {
                            return createMethodInvocationFunction(name, rm, 1, 0);
                        }
                    }
                    break;
                }
                case 2: {
                    for (JType type : t) {
                        JMethod sm = JInvokeUtils.getMatchingMethod(type, staticName, arg1, arg2);
                        if (sm != null) {
                            return createMethodInvocationFunction(name, sm, -1, 0, 1);
                        }
                    }
                    break;
                }
            }
        }


        return null;
    }

    private JFunction createUnaryOperatorFunction(String name, JTypePattern[] args, String directName, String staticName, JContext context) {
        JTypePattern arg1Type = args[0];
        int[] order = !(importsFirst) ? new int[]{1, 2} : new int[]{2, 1};
        LinkedHashSet<JType> t = new LinkedHashSet<>(importedTypes);
        t.addAll(importedMethods);

        for (int i = 0; i < order.length; i++) {
            switch (order[i]) {
                case 1: {
                    if(arg1Type.isType()) {
                        JMethod m = JInvokeUtils.getMatchingMethod(arg1Type.getType(), directName);
                        if (m != null) {
                            return createMethodInvocationFunction(name, m, 0);
                        }
                    }
                    break;

                }
                case 2: {
                    for (JType type : t) {
                        JMethod sm = JInvokeUtils.getMatchingMethod(type, staticName, arg1Type);
                        if (sm != null) {
                            return createMethodInvocationFunction(name, sm, -1, 0);
                        }
                    }
                    break;
                }
            }
        }

        return null;
    }

    public JFunction createMethodInvocationFunction(String name, JMethod m, int baseIndex, int... argIndices) {
        return new JMethodInvocationFunction(name, m, baseIndex, argIndices);
    }

    public JType[] getImportedTypes() {
        return importedTypes.toArray(new JType[0]);
    }

    public JType[] getImportedMethods() {
        return importedMethods.toArray(new JType[0]);
    }

    public JType[] getImportedFields() {
        return importedFields.toArray(new JType[0]);
    }

    public boolean isImportsFirst() {
        return importsFirst;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "importedTypes=" + Arrays.asList(getImportedTypes()) +
                ", importedMethods=" + Arrays.asList(getImportedMethods()) +
                ", importedFields=" + Arrays.asList(getImportedFields()) +
                ", importsFirst=" + isImportsFirst() +
                ", checkEmptyArgMethods=" + isCheckEmptyArgMethods() +
                '}';
    }

}
