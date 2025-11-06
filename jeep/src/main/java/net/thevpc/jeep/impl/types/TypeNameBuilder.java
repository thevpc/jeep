package net.thevpc.jeep.impl.types;

import net.thevpc.jeep.JType;

public class TypeNameBuilder {
    public static final String actualTypeArguments(JType[] actualTypeArguments) {
        StringBuilder pn = new StringBuilder();
        for (int i = 0; i < actualTypeArguments.length; i++) {
            if (i == 0) {
                pn.append("<");
            } else {
                pn.append(",");
            }
            pn.append(actualTypeArguments[i].getName());
            if (i == actualTypeArguments.length - 1) {
                pn.append(">");
            }
        }
        return pn.toString();
    }
}
