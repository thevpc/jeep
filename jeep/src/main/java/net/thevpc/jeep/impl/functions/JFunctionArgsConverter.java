//package net.thevpc.jeep.impl.functions;
//
//import net.thevpc.jeep.*;
//import net.thevpc.jeep.impl.eval.JEvaluableConverter;
//import net.thevpc.jeep.impl.eval.JEvaluableConverter;
//
//import java.util.Arrays;
//
//class JFunctionArgsConverter implements JFunction {
//
//    private final JFunction oo;
//    private final String name;
//    private final JType[] argTypes;
//    private final int index;
//    private final JConverter1 currentConverter;
//    private final JSignature signature;
//
//    public JFunctionArgsConverter(String name, JFunction oo, JType[] argTypes, int index, JConverter1 currentConverter) {
//        this.oo = oo;
//        this.name = name;
//        this.index = index;
//        this.argTypes = JFunctionsImpl.convertArgumentTypesByIndex(argTypes, currentConverter, index);
//        this.currentConverter = currentConverter;
//        this.signature = new JSignature(name, argTypes,oo.signature().isVarArgs());
//    }
//
//    @Override
//    public JType returnType() {
//        return oo.returnType();
//    }
//
//
//    @Override
//    public String name() {
//        return name;
//    }
//
//    @Override
//    public Object invoke(JInvokeContext context) {
//        JEvaluable[] initArgs = context.arguments();
//        JEvaluable[] newArgs = new JEvaluable[initArgs.length];
//        JType[] oldTypes = context.argumentTypes();
//        JType[] newTypes = Arrays.copyOf(oldTypes,oldTypes.length);
//        for (int j = 0; j < signature().argsCount(); j++) {
//            final JEvaluable joperand = initArgs[j];
//            if (index == j) {
//                newArgs[j] = new JEvaluableConverter(currentConverter,joperand);
//                newTypes[j]=currentConverter.targetType();
//            } else {
//                newArgs[j] = joperand;
//            }
//        }
//        return oo.invoke(
//                context.builder()
//                        .arguments(newArgs)
//                        .build()
//        );
//    }
//
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        String n = oo.name();
//        if (n == null || n.isEmpty() || n.trim().isEmpty()) {
//            n = "<IMPLICIT>";
//        }
//        sb.append(n).append("(");
//        for (int j = 0; j < signature().argsCount(); j++) {
//            if (j > 0) {
//                sb.append(",");
//            }
//            if (index == j) {
//                sb.append(currentConverter.originalType().simpleName()).append("->").append(currentConverter.targetType().simpleName());
//            } else {
//                sb.append(signature().argType(j).simpleName());
//            }
//        }
//        sb.append("){");
//        sb.append(oo);
//        sb.append("}");
//        return sb.toString();
//    }
//
//    @Override
//    public JSignature signature() {
//        return signature;
//    }
//}
