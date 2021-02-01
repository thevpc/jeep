package net.thevpc.jeep.impl.types;

import net.thevpc.jeep.*;
import net.thevpc.jeep.impl.functions.JSignature;
import net.thevpc.jeep.util.JTypeUtils;
import net.thevpc.jeep.*;

public class JParameterizedMethodImpl extends AbstractJMethod implements JParameterizedMethod {
    private JMethod rawMethod;
    private JType declaringType;
    private JType[] actualParameters;
    private JType returnType;
    private JSignature sig;

    public JParameterizedMethodImpl(JMethod rawMethod, JType[] actualParameters,JType declaringType) {
        this.rawMethod=rawMethod;
        this.declaringType = declaringType;
        this.actualParameters=actualParameters;
        if(rawMethod instanceof JRawMethod) {
            JRawMethod rawMethod1 = (JRawMethod) rawMethod;
            JType[] jeepParameterTypes = JTypeUtils.buildActualType(rawMethod1.getGenericSignature().argTypes(), this);
            this.returnType = JTypeUtils.buildActualType(rawMethod1.getGenericReturnType(), this);
            this.sig=JSignature.of(rawMethod.getName(),jeepParameterTypes);
        }else{
            JType[] jeepParameterTypes = JTypeUtils.buildActualType(rawMethod.getSignature().argTypes(), this);
            this.returnType = JTypeUtils.buildActualType(rawMethod.getReturnType(), this);
            this.sig=JSignature.of(rawMethod.getName(),jeepParameterTypes);
        }
    }

    @Override
    public JType[] getArgTypes() {
        return sig.argTypes();
    }

    @Override
    public String[] getArgNames() {
        return rawMethod.getArgNames();
    }

    @Override
    public JType[] getActualParameters() {
        return actualParameters;
    }

    @Override
    public JMethod getRawMethod() {
        return rawMethod;
    }

    @Override
    public Object invoke(JInvokeContext context) {
        return rawMethod.invoke(context);
    }

    @Override
    public boolean isAbstract() {
        return rawMethod.isAbstract();
    }

    @Override
    public boolean isStatic() {
        return rawMethod.isStatic();
    }

    @Override
    public boolean isPublic() {
        return rawMethod.isPublic();
    }

    @Override
    public JModifierList getModifiers() {
        return rawMethod.getModifiers();
    }

    @Override
    public JDeclaration getDeclaration() {
        return declaringType;//rawMethod.getDeclaration();
    }

    @Override
    public JAnnotationInstanceList getAnnotations() {
        return rawMethod.getAnnotations();
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
    public JType getReturnType() {
        return returnType;
    }

    @Override
    public JSignature getSignature() {
        return sig;
    }

    @Override
    public JMethod parametrize(JType... parameters) {
        return new JParameterizedMethodImpl(
                this,parameters, getDeclaringType()
        );
    }

    @Override
    public boolean isDefault() {
        return rawMethod.isDefault();
    }

    @Override
    public String getSourceName() {
        return rawMethod.getSourceName();
    }

    @Override
    public JTypes getTypes() {
        return rawMethod.getTypes();
    }

    @Override
    public Object getDefaultValue() {
        return rawMethod.getDefaultValue();
    }
}
