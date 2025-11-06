package net.thevpc.jeep.impl.functions;

import net.thevpc.jeep.*;
import net.thevpc.jeep.*;
import net.thevpc.jeep.core.eval.JEvaluableValue;

import java.util.List;
import java.util.Objects;

public class JMethodWithVarArg implements JMethod {
    private final JMethod method;

    public JMethodWithVarArg(JMethod method) {
        this.method = method;
    }

    @Override
    public String getName() {
        return method.getName();
    }

    @Override
    public JType getDeclaringType() {
        return method.getDeclaringType();
    }

    @Override
    public boolean isStatic() {
        return method.isStatic();
    }

    @Override
    public boolean isPublic() {
        return method.isPublic();
    }

    @Override
    public boolean isAbstract() {
        return method.isPublic();
    }

    @Override
    public JSignature getSignature() {
        return method.getSignature();
    }

    @Override
    public JType getReturnType() {
        return method.getReturnType();
    }

    @Override
    public List<JModifier> getModifiers() {
        return method.getModifiers();
    }

    @Override
    public List<JAnnotationInstance> getAnnotations() {
        return method.getAnnotations();
    }

    @Override
    public JType[] getArgTypes() {
        return method.getArgTypes();
    }

    @Override
    public String[] getArgNames() {
        return method.getArgNames();
    }

    public JMethod getMethod() {
        return method;
    }

    @Override
    public Object invoke(JInvokeContext icontext) {
        JType[] mTypes = method.getSignature().argTypes();
        JEvaluable[] all = new JEvaluable[mTypes.length];
        JEvaluable[] args = icontext.getArguments();
        for (int i = 0; i < all.length - 1; i++) {
            if (icontext.getContext().types().forName(JEvaluable.class.getName()).isAssignableFrom(mTypes[i])) {
                all[i] = new JEvaluableValue((args[i]),mTypes[i]);
            } else {
                all[i] = args[i];
            }
        }
        JType last = mTypes[all.length - 1];
        int varArgCount = args.length - (all.length - 1);
        all[all.length - 1] = new JEvaluable() {
            @Override
            public JType type() {
                return icontext.getArgumentTypes()[icontext.getArgumentTypes().length-1];
            }

            @Override
            public Object evaluate(JInvokeContext context) {
                JType jType = last.componentType();
                Object anArray0 = jType.newArray(varArgCount);
                JArray anArray = jType.toArray(varArgCount).asArray(anArray0);
                anArray.value();
                for (int i = 0; i < varArgCount; i++) {
                    JEvaluable aaa = args[all.length - 1 + i];
                    if (icontext.getContext().types().forName(JEvaluable.class.getName()).isAssignableFrom(jType)) {
                        anArray.set(i, aaa);
                    } else {
                        anArray.set(i, icontext.evaluate(aaa));
                    }
                }
                return anArray.value();
            }
        };
        return method.invoke(
                icontext.builder()
                        .setName(method.getName())
                        .setArguments(all)
                        .build()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JMethodWithVarArg that = (JMethodWithVarArg) o;
        return Objects.equals(method, that.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method);
    }

    @Override
    public JDeclaration getDeclaration() {
        return getDeclaringType();
    }

    @Override
    public JTypeVariable[] getTypeParameters() {
        return method.getTypeParameters();
    }

    @Override
    public JMethod parametrize(JType... parameters) {
        return new JMethodWithVarArg(method.parametrize(parameters));
    }

    @Override
    public boolean isDefault() {
        return method.isDefault();
    }

    @Override
    public String getSourceName() {
        return method.getSourceName();
    }

    @Override
    public JTypes getTypes() {
        return method.getTypes();
    }

    @Override
    public boolean isSynthetic() {
        return method.isSynthetic();
    }

    @Override
    public Object getDefaultValue() {
        return method.getDefaultValue();
    }
}
