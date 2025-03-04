package net.thevpc.jeep;

import java.util.Arrays;

public interface JMutableRawType extends JRawType{
    void addMethod(JMethod m);
    void addInterface(JType interfaceType);
    JConstructor addConstructor(JConstructor constructor, boolean redefine);


    public void setSuperType(JType superclass) {
        this.superclass = superclass;
    }
    public void addInterfaces(JType[] interfaces) {
        this.interfaces.addAll(Arrays.asList(interfaces));
    }
}
