package net.thevpc.jeep.impl.types;

import net.thevpc.jeep.*;
import net.thevpc.jeep.impl.types.host.AbstractJType;
import net.thevpc.jeep.impl.types.host.HostJArray;
import net.thevpc.jeep.impl.types.host.HostJRawType;
import net.thevpc.jeep.util.JTypeUtils;
import net.thevpc.jeep.*;
import net.thevpc.jeep.core.JStaticObject;
import net.thevpc.jeep.impl.JTypesSPI;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.Arrays;

public class DefaultJArrayType extends AbstractJType implements JArrayType {
    private JType root;
    private int dim;
    private String name;
    private String sname;
    private ArrFieldLength lengthField;
    private JType componentType;
    private JAnnotationInstanceList annotations = new DefaultJAnnotationInstanceList();
    private JModifierList modifiers = new DefaultJModifierList();

    public DefaultJArrayType(JType root, int dim) {
        super(root.getTypes());
        this.root = root;
        this.dim = dim;
        if (root.isArray() || dim == 0) {
            throw new IllegalStateException("Invalid Array with dimension ==0");
        }
        StringBuilder fb = new StringBuilder(root.getName().length() + 2 * dim);
        StringBuilder sb = new StringBuilder(root.simpleName().length() + 2 * dim);
        fb.append(root.getName());
        sb.append(root.simpleName());
        for (int i = 0; i < dim; i++) {
            fb.append("[]");
            sb.append("[]");
        }
        this.name = fb.toString();
        this.sname = sb.toString();
        this.lengthField = new ArrFieldLength(this, getTypes());
        this.componentType = dim == 1 ? root :
                JTypesSPI.getRegisteredOrRegister(types2().createArrayType0(root, dim - 1),
                        getTypes()
                );
    }


    @Override
    public JDeclaration getDeclaration() {
        return null;
    }

//    @Override
//    public JType[] actualTypeArguments() {
//        return new JType[0];
//    }

    @Override
    public JTypeVariable[] getTypeParameters() {
        return new JTypeVariable[0];
    }

    @Override
    public JType getRawType() {
        return root.isRawType() ? this : root.getRawType().toArray(arrayDimension());
    }

    @Override
    public JStaticObject getStaticObject() {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String simpleName() {
        return sname;
    }

    @Override
    public boolean isNullable() {
        return true;
    }

    @Override
    public JType toArray(int count) {
        return JTypesSPI.getRegisteredOrRegister(
                types2().createArrayType0(rootComponentType(), arrayDimension() + count),
                getTypes()
        );
    }

    @Override
    public JType getSuperType() {
        return JTypeUtils.forObject(getTypes());
    }

//    @Override
//    public JType parametrize(JType... parameters) {
//        return ((JRawType)rootComponentType()).parametrize(parameters).toArray(arrayDimension());
//    }

    public JType componentType() {
        return componentType;
    }

    @Override
    public JType[] getInterfaces() {
        return new JType[0];
    }

    @Override
    public JConstructor findDefaultConstructorOrNull() {
        return null;
    }

    @Override
    public JConstructor[] getDeclaredConstructors() {
        return new JConstructor[0];
    }

    @Override
    public JField[] getDeclaredFields() {
        return new JField[]{lengthField};
    }

    @Override
    public JMethod[] getDeclaredMethods() {
        return new JMethod[0];
    }

    @Override
    public JType[] getDeclaredInnerTypes() {
        return new JType[0];
    }

    @Override
    public Object getDefaultValue() {
        return null;
    }

    @Override
    public JType getDeclaringType() {
        return null;
    }

    @Override
    public boolean isPublic() {
        return true;
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    @Override
    public String getPackageName() {
        return null;
    }

    @Override
    public String[] getExports() {
        return new String[0];
    }

    public int arrayDimension() {
        return dim;
    }

    @Override
    public JType rootComponentType() {
        return root;
    }

    @Override
    public Object newArray(int... len) {
        if (rootComponentType() instanceof HostJRawType) {
            HostJRawType hjt = (HostJRawType) rootComponentType().getRawType();
            Type ht = hjt.getHostType();
            return Array.newInstance((Class<?>) ht, len);
        } else {
            int len0 = len[0];
            JType jType = componentType();
            DefaultJArray aa = new DefaultJArray(new Object[len0], jType);
            if (len.length > 1) {
                JArrayType jTypea = (JArrayType) jType;
                int[] len2 = Arrays.copyOfRange(len, 0, len.length - 1);
                for (int i = 0; i < len0; i++) {
                    aa.set(i, jTypea.newArray(len2));
                }
            }
            return aa;
        }
    }

    @Override
    public JArray asArray(Object o) {
        if (o instanceof JArray) {
            return (JArray) o;
        }
        return new HostJArray(o, componentType());
    }

    @Override
    public JType replaceParameter(String name, JType param) {
        JType r = rootComponentType().replaceParameter(name, param);
        if (r.getName().equals(rootComponentType().getName())) {
            return this;
        }
        return types2().createArrayType0(r, arrayDimension());
    }

    @Override
    public boolean isArray() {
        return true;
    }


    @Override
    public boolean isInterface() {
        return false;
    }

    public boolean isAssignableFrom(JType other) {
        if(other.isArray()){
            JArrayType otherArray=(JArrayType) other;
            if(arrayDimension()==otherArray.arrayDimension()){
                return rootComponentType().isAssignableFrom(otherArray.rootComponentType());
            }
        }
        return false;
    }

    @Override
    public JAnnotationInstanceList getAnnotations() {
        return annotations;
    }

    @Override
    public JModifierList getModifiers() {
        return modifiers;
    }

    @Override
    public String getSourceName() {
        return null;
    }
}
