package net.thevpc.jeep.core;

import net.thevpc.jeep.JShouldNeverHappenException;

public class JExpressionOptions<T extends JExpressionOptions> implements Cloneable{
    private JExpressionUnaryOptions unary;
    private JExpressionBinaryOptions binary;

    public JExpressionOptions() {
    }

//    public JExpressionOptions(JExpressionUnaryOptions unary, JExpressionBinaryOptions binary) {
//        this.setUnary(unary);
//        this.setBinary(binary);
//    }

    public T copy(){
        try {
            T a=(T) clone();
            if(a.getUnary() !=null){
                a.setUnary(getUnary().copy());
            }
            if(a.getBinary() !=null){
                a.setBinary(getBinary().copy());
            }
            return a;
        } catch (CloneNotSupportedException e) {
            throw new JShouldNeverHappenException();
        }
    }

    public JExpressionUnaryOptions getUnary() {
        return unary;
    }

    public T setUnary(JExpressionUnaryOptions unary) {
        this.unary = unary;
        return (T) this;
    }

    public JExpressionBinaryOptions getBinary() {
        return binary;
    }

    public T setBinary(JExpressionBinaryOptions binary) {
        this.binary = binary;
        return (T) this;
    }
}
