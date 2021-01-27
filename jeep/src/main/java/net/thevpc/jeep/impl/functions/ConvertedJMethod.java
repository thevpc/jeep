package net.thevpc.jeep.impl.functions;

import net.thevpc.jeep.*;
import net.thevpc.jeep.util.JTypeUtils;
import net.thevpc.jeep.*;
import net.thevpc.jeep.impl.eval.JEvaluableConverter;
import net.thevpc.jeep.impl.types.AbstractJMethod;
import net.thevpc.jeep.impl.types.JAnnotationInstanceList;
import net.thevpc.jeep.impl.types.JModifierList;

import java.util.Arrays;
import java.util.Objects;

public class ConvertedJMethod extends AbstractJMethod {
    private JConverter[] argConverters;
    private JConverter resultConverter;
    private JMethod other;
    private JType[] newTypes;
    private JSignature signature;

    //return type was Object.class, fix it!
    public ConvertedJMethod(JMethod other, JConverter[] argConverters, JConverter resultConverter) {
        newTypes = convArgTypes(other.getSignature().argTypes(), argConverters);
        this.other = other;
        this.argConverters = argConverters;
        this.resultConverter = resultConverter;
        this.signature = JSignature.of(other.getName() + System.identityHashCode(this), newTypes);
    }

    private static JType[] convArgTypes(JType[] argTypes, JConverter[] converters) {
        JType[] newArgTypes = new JType[argTypes.length];
        for (int i = 0; i < newArgTypes.length; i++) {
            if (converters != null && converters[i] != null) {
                newArgTypes[i] = converters[i].originalType().getType();
                if (newArgTypes[i] == null) {
                    newArgTypes[i] = argTypes[i];
                }
            } else {
                newArgTypes[i] = argTypes[i];
            }
        }
        return newArgTypes;
    }

    @Override
    public JType[] getArgTypes() {
        return newTypes;
    }

    @Override
    public String[] getArgNames() {
        return other.getArgNames();
    }

    @Override
    public boolean isAbstract() {
        return other.isAbstract();
    }

    @Override
    public Object invoke(JInvokeContext context) {
        JEvaluable[] initialArgs = context.getArguments();
        JEvaluable[] convertedArgs = new JEvaluable[initialArgs.length];
        JType[] initialTypes = context.getArgumentTypes();
        JType[] convertedTypes = new JType[initialArgs.length];
        for (int i = 0; i < initialArgs.length; i++) {
            if (argConverters != null && argConverters[i] != null) {
                JConverter argConverter = argConverters[i];
                JEvaluable value = initialArgs[i];
                convertedArgs[i] = new JEvaluableConverter(argConverter, value);
                convertedTypes[i] = argConverter.targetType().getType();
            } else {
                convertedArgs[i] = initialArgs[i];
                convertedTypes[i] = initialTypes[i];
            }
        }
        JInvokeContext c2 = context.builder()
                .setArguments(convertedArgs)
                .build();
        Object v = other.invoke(c2);
        if (resultConverter != null) {
            v = resultConverter.convert(v, context);
        }
        return v;
    }

    @Override
    public JType getDeclaringType() {
        return other.getDeclaringType();
    }

    @Override
    public JType getReturnType() {
        if (resultConverter == null) {
            return other.getReturnType();
        }
        return resultConverter.targetType().getType();
    }

    @Override
    public JSignature getSignature() {
        return signature;
    }

    @Override
    public boolean isStatic() {
        return other.isStatic();
    }

    @Override
    public boolean isPublic() {
        return other.isPublic();
    }

    @Override
    public JModifierList getModifiers() {
        return other.getModifiers();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(other.getName());
        sb.append("(");
        for (int i = 0; i < argConverters.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            if (argConverters[i] == null) {
                sb.append(other.getSignature().nameSignature().argType(i).simpleName());
            }
        }
        sb.append(")");
        if (resultConverter != null) {
            sb.append(":");
            sb.append(JTypeUtils.str(resultConverter.originalType()));
            sb.append("->");
            sb.append(JTypeUtils.str(resultConverter.targetType()));
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConvertedJMethod that = (ConvertedJMethod) o;
        return Objects.equals(other, that.other) &&
                Arrays.equals(newTypes, that.newTypes);
    }

//    @Override
//    public int hashCode() {
//        int result = Objects.hash(other);
//        result = 31 * result + Arrays.hashCode(newTypes);
//        return result;
//    }


    @Override
    public JTypeVariable[] getTypeParameters() {
        return other.getTypeParameters();
    }

    @Override
    public JMethod parametrize(JType... parameters) {
        throw new JFixMeLaterException();
    }

    @Override
    public boolean isDefault() {
        return other.isDefault();
    }

    @Override
    public String getSourceName() {
        return other.getSourceName();
    }

    @Override
    public JTypes getTypes() {
        return other.getTypes();
    }

    @Override
    public JAnnotationInstanceList getAnnotations() {
        return other.getAnnotations();
    }

    @Override
    public Object getDefaultValue() {
        return other.getDefaultValue();
    }
}
