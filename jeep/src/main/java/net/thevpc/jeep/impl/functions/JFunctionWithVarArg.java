package net.thevpc.jeep.impl.functions;

import net.thevpc.jeep.*;
import net.thevpc.jeep.*;
import net.thevpc.jeep.core.eval.JEvaluableValue;
import net.thevpc.jeep.impl.types.JModifierList;

import java.util.List;

public class JFunctionWithVarArg implements JFunction {
    private final JFunction fct;

    public JFunctionWithVarArg(JFunction fct) {
        this.fct = fct;
    }

    @Override
    public String getName() {
        return fct.getName();
    }

    @Override
    public JSignature getSignature() {
        return fct.getSignature();
    }

    @Override
    public JType getReturnType() {
        return fct.getReturnType();
    }

    public JFunction getFunction() {
        return fct;
    }

    @Override
    public Object invoke(JInvokeContext icontext) {
        JType[] mTypes = fct.getSignature().argTypes();
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
        return fct.invoke(
                icontext.builder()
                        .setInstance(null)
                        .setName(fct.getName())
                        .setArguments(all)
                        .build()
        );
    }

    @Override
    public String getSourceName() {
        return fct.getSourceName();
    }

    @Override
    public boolean isPublic() {
        return fct.isPublic();
    }

    @Override
    public JDeclaration getDeclaration() {
        return fct.getDeclaration();
    }

    @Override
    public List<JModifier> getModifiers() {
        return fct.getModifiers();
    }

    @Override
    public List<JAnnotationInstance> getAnnotations() {
        return fct.getAnnotations();
    }
    @Override
    public JTypes getTypes() {
        return fct.getTypes();
    }

}
