package net.thevpc.jeep.impl.types;

import net.thevpc.jeep.JDeclaration;
import net.thevpc.jeep.JType;
import net.thevpc.jeep.JTypes;
import net.thevpc.jeep.*;

public class DefaultJTypeVariable extends AbstractJTypeVariable {
    private String name;
    private JType[] lowerBounds;
    private JType[] upperBounds;
    private JDeclaration declaration;
    private JAnnotationInstanceList annotations=new DefaultJAnnotationInstanceList();
    private JModifierList modifiers=new DefaultJModifierList();

    public DefaultJTypeVariable(String name, JType[] lowerBounds, JType[] upperBounds, JDeclaration declaration, JTypes types) {
        super(types);
        this.name = name;
        this.lowerBounds = lowerBounds;
        this.upperBounds = upperBounds;
        this.declaration = declaration;
    }

    @Override
    public JDeclaration getDeclaration() {
        return declaration;
    }

    @Override
    public JType[] upperBounds() {
        return upperBounds;
    }

    public JType[] lowerBounds() {
        return lowerBounds;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public JType replaceParameter(String name, JType param) {
        if(name.equals(this.name)){
            return param;
        }
        return this;
    }

    @Override
    public boolean isInterface() {
        return false;
    }

    @Override
    public JAnnotationInstanceList getAnnotations() {
        return annotations;
    }

    @Override
    public JModifierList getModifiers() {
        return modifiers;
    }

    @Override
    public String getSourceName() {
        return null;
    }
}
