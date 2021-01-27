package net.thevpc.jeep.util;

import net.thevpc.jeep.impl.JArgumentTypes;

public class JMethodObject<T> {
    private JArgumentTypes signature;
    private JArgumentTypes effectiveSignature;
    private T method;
    private Comparable costObject;

    public JMethodObject(JArgumentTypes signature, JArgumentTypes effectiveSignature,T method,Comparable costObject) {
        this.signature = signature;
        this.method = method;
        this.effectiveSignature = effectiveSignature;
        this.costObject = costObject;
    }

    public JArgumentTypes getEffectiveSignature() {
        return effectiveSignature;
    }

    public Comparable getCostObject() {
        return costObject;
    }

    public JMethodObject<T> setCostObject(Comparable costObject) {
        this.costObject = costObject;
        return this;
    }

    public JArgumentTypes getSignature() {
        return signature;
    }

    public T getMethod() {
        return method;
    }

    @Override
    public String toString() {
        return "JMethodObject{" +
                "signature=" + signature +
                ", method=" + method +
                ", costObject=" + costObject +
                '}';
    }
}
