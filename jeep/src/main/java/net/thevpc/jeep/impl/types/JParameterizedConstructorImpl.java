package net.thevpc.jeep.impl.types;

import net.thevpc.jeep.*;
import net.thevpc.jeep.impl.functions.JSignature;
import net.thevpc.jeep.util.JTypeUtils;

import java.util.List;

public class JParameterizedConstructorImpl extends AbstractJConstructor implements JParameterizedConstructor {
    private JConstructor rawConstructor;
    private JType declaringType;
    private JType[] actualTypes;
    private JSignature sig;

    public JParameterizedConstructorImpl(JConstructor rawConstructor, JType[] actualTypes, JType declaringType) {
        this.rawConstructor = rawConstructor;
        this.declaringType = declaringType;
        this.actualTypes = actualTypes;
        if (rawConstructor instanceof JRawConstructor) {
            JRawConstructor rawMethod1 = (JRawConstructor) rawConstructor;
            JType[] jeepParameterTypes = JTypeUtils.buildActualType(rawMethod1.getGenericSignature().argTypes(), this);
            this.sig = JSignature.of(rawConstructor.getName(), jeepParameterTypes);
        } else {
            JType[] jeepParameterTypes = JTypeUtils.buildActualType(rawConstructor.getSignature().argTypes(), this);
            this.sig = JSignature.of(rawConstructor.getName(), jeepParameterTypes);
        }
    }

    @Override
    public JType[] getArgTypes() {
        return sig.argTypes();
    }

    @Override
    public String[] getArgNames() {
        return rawConstructor.getArgNames();
    }
    @Override
    public JConstructor getRawConstructor() {
        return rawConstructor;
    }

    @Override
    public JType[] getActualParameters() {
        return actualTypes;
    }

    @Override
    public JTypeVariable[] getTypeParameters() {
        return new JTypeVariable[0];
    }

    @Override
    public JType getDeclaringType() {
        return declaringType;
    }

    @Override
    public boolean isPublic() {
        return rawConstructor.isPublic();
    }

    @Override
    public List<JModifier> getModifiers() {
        return rawConstructor.getModifiers();
    }

    @Override
    public List<JAnnotationInstance> getAnnotations() {
        return rawConstructor.getAnnotations();
    }

    @Override
    public JTypes getTypes() {
        return rawConstructor.getTypes();
    }

    @Override
    public Object invoke(JInvokeContext context) {
        return rawConstructor.invoke(context);
    }

    @Override
    public JSignature getSignature() {
        return sig;
    }

    @Override
    public JType getReturnType() {
        return declaringType;
    }

    @Override
    public String getName() {
        return declaringType.getRawType().getSimpleName();
    }

    @Override
    public JDeclaration getDeclaration() {
        return declaringType;
    }

    @Override
    public String getSourceName() {
        return rawConstructor.getSourceName();
    }
}
