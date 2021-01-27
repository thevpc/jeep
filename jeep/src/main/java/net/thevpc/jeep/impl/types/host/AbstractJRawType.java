package net.thevpc.jeep.impl.types.host;

import net.thevpc.jeep.JType;
import net.thevpc.jeep.JTypeVariable;
import net.thevpc.jeep.JTypes;
import net.thevpc.jeep.impl.JTypesSPI;
import net.thevpc.jeep.JRawType;

public abstract class AbstractJRawType extends AbstractJType implements JRawType {
    public AbstractJRawType(JTypes types) {
        super(types);
    }

    @Override
    public JType toArray(int count) {
        return JTypesSPI.getRegisteredOrRegister(
                types2().createArrayType0(this,count)
                , getTypes());
    }

    @Override
    public JType parametrize(JType... parameters) {
        if (parameters.length == 0) {
            throw new IllegalArgumentException("Invalid zero parameters count");
        }
        JTypeVariable[] vars = getTypeParameters();
        if (vars.length != parameters.length) {
            throw new IllegalArgumentException("Invalid parameters count. expected "+vars.length+" but got "+parameters.length);
        }
        return JTypesSPI.getRegisteredOrRegister(
                types2().createParameterizedType0(this, parameters,
                getDeclaringType()), getTypes());
    }

    @Override
    public boolean isPublic() {
        return ((JTypesSPI)getTypes()).isPublicType(this);
    }

    @Override
    public boolean isStatic() {
        return ((JTypesSPI)getTypes()).isStaticType(this);
    }

    public boolean isInterface() {
        return ((JTypesSPI)getTypes()).isInterfaceType(this);
    }

}
