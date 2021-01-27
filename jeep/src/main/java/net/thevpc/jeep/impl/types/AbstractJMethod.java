package net.thevpc.jeep.impl.types;

import net.thevpc.jeep.JDeclaration;
import net.thevpc.jeep.JMethod;
import net.thevpc.jeep.impl.JTypesSPI;

public abstract class AbstractJMethod implements JMethod {
    @Override
    public String getName() {
        return getSignature().name();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(isPublic()){
            sb.append("public ");
        }
        if(isStatic()){
            sb.append("static ");
        }
        //type if not anonymous!
        if (getReturnType() != null) {
            sb.append(getReturnType().getName());
            sb.append(" ");
        }
        if(getDeclaringType()!=null) {
            sb.append(getDeclaringType().getName());
            sb.append(".");
        }
        sb.append(getSignature());
        return sb.toString();

    }

    @Override
    public boolean isAbstract() {
        return ((JTypesSPI)getTypes()).isAbstractMethod(this);
    }

    @Override
    public boolean isStatic() {
        return ((JTypesSPI)getTypes()).isStaticMethod(this);
    }

    @Override
    public boolean isPublic() {
        return ((JTypesSPI)getTypes()).isPublicMethod(this);
    }

    public boolean isSynthetic() {
        return ((JTypesSPI)getTypes()).isSyntheticMethod(this);
    }

    @Override
    public JDeclaration getDeclaration() {
        return getDeclaringType();
    }

    @Override
    public int hashCode() {
        return getSignature().hashCode();
    }
}
