package net.thevpc.jeep;

import net.thevpc.jeep.impl.types.JAnnotationInstanceList;
import net.thevpc.jeep.impl.types.JModifierList;

public interface JDeclaration {
    //parent declaration
    JDeclaration getDeclaration();

    JModifierList getModifiers();

    JAnnotationInstanceList getAnnotations();
    JTypes getTypes();

    String getSourceName();
}
