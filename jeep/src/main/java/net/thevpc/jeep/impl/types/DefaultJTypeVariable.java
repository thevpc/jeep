package net.thevpc.jeep.impl.types;

import net.thevpc.jeep.JDeclaration;
import net.thevpc.jeep.JType;
import net.thevpc.jeep.JTypes;
import net.thevpc.jeep.*;
import net.thevpc.jeep.util.ImplicitValue;

import java.util.ArrayList;
import java.util.List;

public class DefaultJTypeVariable extends AbstractJTypeVariable {
    private String name;
    private JType[] lowerBounds;
    private JType[] upperBounds;
    private JDeclaration declaration;
    public ImplicitValue.MapForListImplicitValue<String, JAnnotationInstance> annotations = new ImplicitValue.MapForListImplicitValue<>(x -> x.getName());
    public ImplicitValue.MapForListImplicitValue<String, JModifier> modifiers = new ImplicitValue.MapForListImplicitValue<>(x -> x.name());

    public DefaultJTypeVariable(String name, JType[] lowerBounds, JType[] upperBounds, JDeclaration declaration, JTypes types) {
        super(types);
        this.name = name;
        this.lowerBounds = lowerBounds;
        this.upperBounds = upperBounds;
        this.declaration = declaration;
    }

    public DefaultJTypeVariable setLowerBounds(JType[] lowerBounds) {
        this.lowerBounds = lowerBounds;
        return this;
    }

    public DefaultJTypeVariable setUpperBounds(JType[] upperBounds) {
        this.upperBounds = upperBounds;
        return this;
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
    public List<JAnnotationInstance> getAnnotations() {
        return annotations.values();
    }

    @Override
    public List<JModifier> getModifiers() {
        return modifiers.values();
    }

    @Override
    public String getSourceName() {
        return null;
    }

    @Override
    public void addAnnotation(JAnnotationInstance jAnnotationInstance) {
        annotations.add(jAnnotationInstance);
    }
}
