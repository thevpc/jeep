package net.thevpc.jeep.impl.functions;

import net.thevpc.jeep.JDeclaration;
import net.thevpc.jeep.JInvokable;
import net.thevpc.jeep.impl.types.DefaultJAnnotationInstanceList;
import net.thevpc.jeep.impl.types.DefaultJModifierList;
import net.thevpc.jeep.impl.types.JAnnotationInstanceList;
import net.thevpc.jeep.impl.types.JModifierList;

public abstract class AbstractJInvokable implements JInvokable {
    private JModifierList modifiers = new DefaultJModifierList();
    private JAnnotationInstanceList annotations = new DefaultJAnnotationInstanceList();

    @Override
    public boolean isPublic() {
        return true;
    }

    @Override
    public JDeclaration getDeclaration() {
        return null;
    }

    @Override
    public JModifierList getModifiers() {
        return modifiers;
    }

    @Override
    public JAnnotationInstanceList getAnnotations() {
        return annotations;
    }

    public String getName() {
        return getSignature().name();
    }
}
