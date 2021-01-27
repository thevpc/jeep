package net.thevpc.jeep.impl.functions;

import net.thevpc.jeep.*;
import net.thevpc.jeep.util.JTypeUtils;
import net.thevpc.jeep.core.JFunctionBase;
import net.thevpc.jeep.core.eval.JEvaluableValue;

public class JFunctionFromJMethod extends JFunctionBase {
    private final JMethod method;

    public JFunctionFromJMethod(String name, JMethod method, JTypePattern[] argTypes) {
        //TODO fix me later
        super(name, method.getReturnType(), JTypeUtils.typesOrError(argTypes), method.getSignature().isVarArgs(), method.getSourceName());
        this.method = method;
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
        return method.invoke(
                icontext.builder()
                        .setInstance(null)
                        .setName(method.getName())
                        .setArguments(all)
                        .build()
        );
    }

    @Override
    public JTypes getTypes() {
        return method.getTypes();
    }
}
