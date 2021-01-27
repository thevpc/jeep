package net.thevpc.jeep.core;

import net.thevpc.jeep.impl.functions.JSignature;
import net.thevpc.jeep.util.JTypeUtils;
import net.thevpc.jeep.JFunction;
import net.thevpc.jeep.JType;
import net.thevpc.jeep.JTypes;

public abstract class JFunctionBase extends AbstractJFunction {

    private String name;
    private JType resultType;
    private JType[] argTypes;
    private boolean varArgs;
    private JSignature signature;
    private String sourceName;

    public JFunctionBase(String name, String returnType, String[] argTypes, JTypes types, String sourceName) {
        this(name, types.forName(returnType), types.forName(argTypes), false,sourceName);
    }

//    public JFunctionBase(String name, Class returnType, Class[] argTypes, JTypes types) {
//        this(name, types.forName(returnType), types.forName(argTypes), false);
//    }

    public JFunctionBase(String name, JType returnType, JType[] argTypes, String sourceName) {
        this(name, returnType, argTypes, false,sourceName);
    }

    public JFunctionBase(String name, JType returnType, JType[] argTypes, boolean varArgs, String sourceName) {
        super(returnType.getTypes());
        this.sourceName = sourceName;
        this.resultType = returnType;
        this.name = name;
        this.argTypes = argTypes;
        this.varArgs = varArgs;
        this.signature = new JSignature(name, argTypes, varArgs);
        if (varArgs && (this.argTypes.length == 0 || !this.argTypes[this.argTypes.length - 1].isArray())) {
            throw new IllegalArgumentException("invalid varargs");
        }
    }

    public JFunction getBase() {
        return null;
    }

    @Override
    public JType getReturnType() {
        return resultType;
    }


    public String getName() {
        return name;
    }

    @Override
    public JSignature getSignature() {
        return signature;
    }

    @Override
    public String toString() {
        String n = getName();
        StringBuilder sb = new StringBuilder(n).append("(");
        for (int i = 0; i < argTypes.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            String sargi = JTypeUtils.getSimpleClassName(argTypes[i]);
            sb.append(sargi);
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public JTypes getTypes() {
        return getReturnType().getTypes();
    }

    @Override
    public String getSourceName() {
        return sourceName;
    }
    
}
