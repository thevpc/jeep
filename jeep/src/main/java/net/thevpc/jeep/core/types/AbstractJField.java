package net.thevpc.jeep.core.types;

import net.thevpc.jeep.JField;
import net.thevpc.jeep.JType;

public abstract class AbstractJField implements JField {
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(isPublic()){
            sb.append("public ");
        }
        if(isStatic()){
            sb.append("static ");
        }
        JType type = type();
        sb.append(type==null?"?":(type.getName()));
        sb.append(" ");
        sb.append(getDeclaringType().getName());
        sb.append(".");
        sb.append(name());
        return sb.toString();
    }
}
