package net.thevpc.jeep.impl;

import net.thevpc.jeep.util.JTypeUtils;
import net.thevpc.jeep.JTypeName;

import java.util.Arrays;
import java.util.Objects;

public class JArgumentTypeNames {

    private final JTypeName[] argTypes;
    private final boolean varArgs;

    public JArgumentTypeNames(JTypeName[] argTypes, boolean varArgs) {
        this.argTypes = Arrays.copyOf(argTypes,argTypes.length);
        for (int i = 0; i < argTypes.length; i++) {
            if(argTypes[i]==null){
                //throw new IllegalArgumentException("Invalid type #"+i);
            }
        }
        this.varArgs = varArgs;
        if (varArgs) {
            if (argTypes.length == 0) {
                throw new IllegalArgumentException("Expected Array");
            }
            if (argTypes[argTypes.length - 1]!=null && !argTypes[argTypes.length - 1].isArray()) {
                throw new IllegalArgumentException("Expected Array");
            }
        }
    }

    public JTypeName lastArgType() {
        int i = typesCount();
        return i==0?null: argType(i -1);
    }

    public int typesCount() {
        return argTypes.length;
    }

    public JTypeName argType(int index) {
        return argTypes[index];
    }

    public JTypeName[] argTypes() {
        return Arrays.copyOf(argTypes, argTypes.length);
    }

    public boolean isVarArgs() {
        return varArgs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JArgumentTypeNames that = (JArgumentTypeNames) o;
        return varArgs == that.varArgs &&
                Arrays.equals(argTypes, that.argTypes);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(varArgs);
        result = 31 * result + Arrays.hashCode(argTypes);
        return result;
    }

    public String toString(boolean withPars) {
        StringBuilder sb=new StringBuilder();
        if(withPars) {
            sb.append("(");
        }
        for (int i = 0; i < argTypes.length; i++) {
            if(i>0){
                sb.append(",");
            }
            if(i==argTypes.length-1 && varArgs){
                if(argTypes[i]==null){
                    sb.append("?");
                }else {
                    sb.append(JTypeUtils.getFullClassName(argTypes[i].componentType()));
                }
                sb.append("...");

            }else{
                if(argTypes[i]==null){
                    sb.append("?");
                }else {
                    sb.append(JTypeUtils.getFullClassName(argTypes[i]));
                }
            }
        }
        if(withPars) {
            sb.append(")");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return toString(true);
    }
}
