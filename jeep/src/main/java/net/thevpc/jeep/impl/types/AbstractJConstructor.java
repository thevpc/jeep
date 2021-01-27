package net.thevpc.jeep.impl.types;

import net.thevpc.jeep.JConstructor;
import net.thevpc.jeep.impl.JArgumentTypes;
import net.thevpc.jeep.impl.JTypesSPI;

public abstract class AbstractJConstructor implements JConstructor {
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(isPublic()){
            sb.append("public ");
        }
        sb.append(getDeclaringType().getName());
        sb.append(new JArgumentTypes(getSignature().argTypes(), getSignature().isVarArgs()));
        return sb.toString();
    }


    public boolean isPublic() {
        return ((JTypesSPI)getTypes()).isPublicConstructor(this);
    }
}
