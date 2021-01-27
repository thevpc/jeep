package net.thevpc.jeep.impl.functions;

import net.thevpc.jeep.*;
import net.thevpc.jeep.*;
import net.thevpc.jeep.core.eval.JEvaluableValue;
import net.thevpc.jeep.impl.types.AbstractJConstructor;
import net.thevpc.jeep.impl.types.JAnnotationInstanceList;
import net.thevpc.jeep.impl.types.JModifierList;

public class JConstructorWithVarArg extends AbstractJConstructor {
    private final JConstructor ctr;

    public JConstructorWithVarArg(JConstructor ctr) {
        this.ctr = ctr;
    }

    @Override
    public String getName() {
        return ctr.getName();
    }

    @Override
    public JType getDeclaringType() {
        return ctr.getDeclaringType();
    }

    @Override
    public boolean isPublic() {
        return ctr.isPublic();
    }

    @Override
    public JSignature getSignature() {
        return ctr.getSignature();
    }

    @Override
    public JType getReturnType() {
        return ctr.getReturnType();
    }

    public JConstructor getCtr() {
        return ctr;
    }

    @Override
    public JType[] getArgTypes() {
        return ctr.getArgTypes();
    }

    @Override
    public JModifierList getModifiers() {
        return ctr.getModifiers();
    }

    @Override
    public String[] getArgNames() {
        return ctr.getArgNames();
    }

    @Override
    public Object invoke(JInvokeContext icontext) {
        JType[] mTypes = ctr.getSignature().argTypes();
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
                JArrayType jType = (JArrayType) ((JArrayType)last).componentType();
                Object anArray0 = jType.newArray(varArgCount);
                JArray anArray = ((JArrayType)jType.toArray(varArgCount)).asArray(anArray0);
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
        return ctr.invoke(
                icontext.builder()
                        .setName(ctr.getName())
                        .setArguments(all)
                        .build()
        );
    }

    @Override
    public JDeclaration getDeclaration() {
        return getDeclaringType();
    }

    @Override
    public JTypeVariable[] getTypeParameters() {
        return ctr.getTypeParameters();
    }

    @Override
    public String getSourceName() {
        return ctr.getSourceName();
    }

    @Override
    public JTypes getTypes() {
        return ctr.getTypes();
    }

    @Override
    public JAnnotationInstanceList getAnnotations() {
        return ctr.getAnnotations();
    }
}
