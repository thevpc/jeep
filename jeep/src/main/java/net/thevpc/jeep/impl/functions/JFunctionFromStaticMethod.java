package net.thevpc.jeep.impl.functions;

import net.thevpc.jeep.*;
import net.thevpc.jeep.*;
import net.thevpc.jeep.impl.types.JModifierList;

import java.util.List;

public class JFunctionFromStaticMethod implements JFunction {
    private JMethod method;
    public JFunctionFromStaticMethod(JMethod method) {
        this.method=method;
    }

    public JMethod getMethod() {
        return method;
    }

    @Override
    public JType getReturnType() {
        return method.getReturnType();
    }

    @Override
    public String getName() {
        return method.getName();
    }

    @Override
    public Object invoke(JInvokeContext context) {
        return method.invoke(context);
    }

    @Override
    public JSignature getSignature() {
        return method.getSignature();
    }

    @Override
    public String getSourceName() {
        return method.getSourceName();
    }

    @Override
    public boolean isPublic() {
        return method.isPublic();
    }

    @Override
    public JDeclaration getDeclaration() {
        return method.getDeclaration();
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
    public JTypes getTypes() {
        return method.getTypes();
    }

}
