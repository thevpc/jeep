package net.thevpc.jeep;

import java.util.Arrays;
import java.util.Map;

public interface JMutableRawType extends JRawType{
    void addMethod(JMethod m);
    void addInterface(JType interfaceType);
    JConstructor addConstructor(JConstructor constructor, boolean redefine);

    default void setInterfaces(JType[] array){

    }

    default void setSuperType(JType tt){

    }

    JAnnotationField getAnnotationField(String name);
//    public void setSuperType(JType superclass) {
//        this.superclass = superclass;
//    }
//    public void addInterfaces(JType[] interfaces) {
//        this.interfaces.addAll(Arrays.asList(interfaces));
//    }
}
