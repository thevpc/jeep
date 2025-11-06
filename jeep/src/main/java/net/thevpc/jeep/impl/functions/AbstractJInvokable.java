package net.thevpc.jeep.impl.functions;

import net.thevpc.jeep.JAnnotationInstance;
import net.thevpc.jeep.JDeclaration;
import net.thevpc.jeep.JInvokable;
import net.thevpc.jeep.JModifier;
import net.thevpc.jeep.util.ImplicitValue;

import java.util.List;

public abstract class AbstractJInvokable implements JInvokable {
    public ImplicitValue.MapForListImplicitValue<String, JAnnotationInstance> annotations = new ImplicitValue.MapForListImplicitValue<>(x -> x.getName());
    public ImplicitValue.MapForListImplicitValue<String, JModifier> modifiers = new ImplicitValue.MapForListImplicitValue<>(x -> x.name());

    @Override
    public boolean isPublic() {
        return true;
    }

    @Override
    public JDeclaration getDeclaration() {
        return null;
    }

    @Override
    public List<JModifier> getModifiers() {
        return modifiers.values();
    }

    @Override
    public List<JAnnotationInstance> getAnnotations() {
        return annotations.values();
    }

    public String getName() {
        return getSignature().name();
    }
}
