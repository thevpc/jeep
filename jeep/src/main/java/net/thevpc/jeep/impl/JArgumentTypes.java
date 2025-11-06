package net.thevpc.jeep.impl;

import net.thevpc.jeep.util.JTypeUtils;
import net.thevpc.jeep.JType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class JArgumentTypes {

    private final JType[] argTypes;
    private final boolean varArgs;

    public JArgumentTypes(JType[] argTypes, boolean varArgs) {
        this.argTypes = Arrays.copyOf(argTypes, argTypes.length);
        for (int i = 0; i < argTypes.length; i++) {
            if (argTypes[i] == null) {
                throw new IllegalArgumentException("Invalid type #" + i);
            }
        }
        this.varArgs = varArgs;
        if (varArgs) {
            if (argTypes.length == 0) {
                throw new IllegalArgumentException("Expected Array");
            }
            if (!(argTypes[argTypes.length - 1] instanceof JType) || !((JType) argTypes[argTypes.length - 1]).isArray()) {
                throw new IllegalArgumentException("Expected Array");
            }
        }
    }

    public boolean accept(JType[] other) {
        return acceptAndExpand(other)!=null;
    }

    public JType[] acceptAndExpand(JType[] other) {
        return acceptAndExpand(other,argTypes,varArgs);
    }

    public JType lastArgType() {
        int i = argsCount();
        return i == 0 ? null : argType(i - 1);
    }

    public boolean acceptArgsCount(int callArgumentsCount) {
        if (callArgumentsCount < 0) {
            return true;
        }
        int ac = argsCount();
        return (ac == callArgumentsCount)
                || (ac > 0 && isVarArgs() && callArgumentsCount >= (ac - 1));
    }

    public int argsCount() {
        return argTypes.length;
    }

    public JType argType(int index) {
        if (index >= argTypes.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return argTypes[index];
    }

    public JType[] argTypes() {
        return Arrays.copyOf(argTypes, argTypes.length);
    }

    public boolean isVarArgs() {
        return varArgs;
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(varArgs);
        result = 31 * result + Arrays.hashCode(argTypes);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JArgumentTypes that = (JArgumentTypes) o;
        return varArgs == that.varArgs &&
                Arrays.equals(argTypes, that.argTypes);
    }

    @Override
    public String toString() {
        return toString(true);
    }

    public String toString(boolean withPars) {
        StringBuilder sb = new StringBuilder();
        if (withPars) {
            sb.append("(");
        }
        for (int i = 0; i < argTypes.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            if (i == argTypes.length - 1 && varArgs) {
                JType argType = argTypes[i];
                sb.append(JTypeUtils.getFullClassName(argType.componentType()));
                sb.append("...");
            } else {
                sb.append(JTypeUtils.getFullClassName(argTypes[i]));
            }
        }
        if (withPars) {
            sb.append(")");
        }
        return sb.toString();
    }


    public static JType[] acceptAndExpand(JType[] actualArgTypes,JType[] expectedArgTypes,boolean expectedArgVarTypes) {
        if (expectedArgVarTypes) {
            if (expectedArgTypes.length - 1 >= actualArgTypes.length) {
                List<JType> t2=new ArrayList<>();
                for (int i = 0; i < expectedArgTypes.length - 1; i++) {
                    if (actualArgTypes[i]!=null && !expectedArgTypes[i].isAssignableFrom(actualArgTypes[i])) {
                        return null;
                    }
                    t2.add(expectedArgTypes[i]);
                }
                int r= actualArgTypes.length-(expectedArgTypes.length - 1);
                JType lastArgType = expectedArgTypes[expectedArgTypes.length - 1];
                JType componentType = lastArgType.componentType();
                if(r==0) {
                    //ok
                }else if(r==1){
                    if(actualArgTypes[expectedArgTypes.length-1]==null) {
                        t2.add(lastArgType);
                    }else if(actualArgTypes[expectedArgTypes.length-1].isArray() &&
                            lastArgType.isAssignableFrom(actualArgTypes[expectedArgTypes.length-1])){
                        t2.add(lastArgType);
                    }else if(componentType.isAssignableFrom(actualArgTypes[expectedArgTypes.length-1])){
                        t2.add(componentType);
                    }else {
                        return null;
                    }
                }else{
                    for (int i = expectedArgTypes.length-1; i < actualArgTypes.length; i++) {
                        if(actualArgTypes[expectedArgTypes.length-1]!=null && !componentType.isAssignableFrom(actualArgTypes[expectedArgTypes.length-1])){
                            return null;
                        }
                        t2.add(componentType);
                    }
                }
                return t2.toArray(new JType[0]);
            }
            return null;
        } else {
            if (expectedArgTypes.length == actualArgTypes.length) {
                for (int i = 0; i < actualArgTypes.length; i++) {
                    if (actualArgTypes[i]!=null && !expectedArgTypes[i].isAssignableFrom(actualArgTypes[i])) {
                        return null;
                    }
                }
                return expectedArgTypes;
            }
            return null;
        }
    }
}
