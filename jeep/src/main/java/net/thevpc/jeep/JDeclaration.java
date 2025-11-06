package net.thevpc.jeep;

import java.util.List;

public interface JDeclaration {
    //parent declaration
    JDeclaration getDeclaration();

    List<JModifier> getModifiers();

    List<JAnnotationInstance> getAnnotations();
    JTypes getTypes();

    String getSourceName();
}
