package net.thevpc.jeep.impl.types;

import net.thevpc.jeep.*;
import net.thevpc.jeep.impl.functions.JSignature;
import net.thevpc.jeep.impl.types.host.AbstractJType;
import net.thevpc.jeep.util.JTypeUtils;
import net.thevpc.jeep.*;
import net.thevpc.jeep.core.JStaticObject;
import net.thevpc.jeep.impl.JTypesSPI;

import java.util.LinkedHashMap;
import java.util.Map;

public class JParameterizedTypeImpl extends AbstractJType implements JParameterizedType{
    private JType rootRaw;
    private JType superType;
    private JType[] actualTypeArguments;

    private LinkedHashMap<String, JField> _fields;
    private LinkedHashMap<JSignature, JMethod> _methods;
    private LinkedHashMap<JSignature, JConstructor> _constructors;
    private String name;
    private String sname;
    private JType[] _interfaces;
    private JType declaringType;
    private LinkedHashMap<String, JType> innerTypes;
    public JParameterizedTypeImpl(JType rootRaw, JType[] actualTypeArguments, JType declaringType, JTypes types) {
        super(types);
        this.rootRaw=rootRaw;
        this.declaringType=declaringType;
        setActualTypeArguments(actualTypeArguments);
        JType sType = rootRaw.getSuperType();
        this.superType= sType ==null?null: JTypeUtils.buildParentType(sType,this);
    }

    @Override
    public JType[] getActualTypeArguments() {
        return actualTypeArguments;
    }

    public void setActualTypeArguments(JType[] actualTypeArguments) {
        this.actualTypeArguments = actualTypeArguments;
        
        StringBuilder pn=new StringBuilder();
        for (int i = 0; i < actualTypeArguments.length; i++) {
            if(i==0){
                pn.append("<");
            }else{
                pn.append(",");
            }
            pn.append(actualTypeArguments[i].getName());
            if(i==actualTypeArguments.length-1){
                pn.append(">");
            }
        }
        if(declaringType==null) {
            this.name = rootRaw.getName() + pn.toString();
        }else{
            this.name=declaringType.getName()+"."+rootRaw.simpleName()+pn;
        }
        this.sname=rootRaw.simpleName()+pn;
    }
    

    @Override
    public JStaticObject getStaticObject() {
        //shares static with root
        return rootRaw.getStaticObject();
    }

    @Override
    public String[] getExports() {
        return new String[0];
    }

    @Override
    public JType getSuperType() {
        return superType;
    }

    @Override
    public JType replaceParameter(String name, JType param) {
        JType[] jTypes = getActualTypeArguments();
        JType[] y = new JType[jTypes.length];
        boolean modified = false;
        for (int i = 0; i < y.length; i++) {
            y[i] = jTypes[i].replaceParameter(name, param);
            modified |= (!y[i].getName().equals(jTypes[i].getName()));
        }
        if (modified) {
            return types2().createParameterizedType0(getRawType(),y,this);
        } else {
            return this;
        }
    }

    @Override
    public String getPackageName() {
        return getRawType().getPackageName();
    }

    @Override
    public JType getDeclaringType() {
        return declaringType;
    }

    @Override
    public boolean isNullable() {
        return getRawType().isNullable();
    }

    @Override
    public JType getRawType() {
        return rootRaw;
    }

    @Override
    public Object getDefaultValue() {
        return getRawType().getDefaultValue();
    }

    @Override
    public JTypeVariable[] getTypeParameters() {
        return new JTypeVariable[0];
    }

    @Override
    public JDeclaration getDeclaration() {
        return getRawType().getDeclaringType();
    }

//    @Override
//    public JType parametrize(JType... parameters) {
//        if (parameters.length == 0) {
//            throw new IllegalArgumentException("Invalid");
//        }
//        JTypeVariable[] vars = typeParameters();
//        if (vars.length != parameters.length) {
//            throw new IllegalArgumentException("Invalid");
//        }
//        int dim = arrayDimension();
//        JType rootRaw = dim == 0 ? this : rootComponentType().rawType();
//        return DefaultJTypes.getRegisteredOrRegister(new JParameterizedTypeImpl(rootRaw, parameters,types()),types());
//    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String simpleName() {
        return sname;
    }

    @Override
    public JType[] getInterfaces() {
        if(_interfaces==null){
            _interfaces= JTypeUtils.buildParentType(getRawType().getInterfaces(),this);
        }
        return _interfaces;
    }
    private synchronized Map<String, JType> _innerTypes() {
        if (innerTypes == null) {
            innerTypes = new LinkedHashMap<>();
            for (JType item : rootRaw.getDeclaredInnerTypes()) {
                if(item.isStatic()){
                    innerTypes.put(item.simpleName(), item);
                }else {
                    JParameterizedType f = types2().createParameterizedType0(item,
                            new JType[0],
                            this);
                    innerTypes.put(f.simpleName(), f);
                }
            }
        }
        return innerTypes;
    }

    @Override
    public JMethod[] getDeclaredMethods() {
        if(_methods==null){
            LinkedHashMap<JSignature, JMethod> _methods=new LinkedHashMap<>();
            for (JMethod jMethod : getRawType().getDeclaredMethods()) {
                JParameterizedMethodImpl m=new JParameterizedMethodImpl(jMethod,new JType[0], this);
                _methods.put(m.getSignature(),m);
            }
            this._methods=_methods;
        }
        return this._methods.values().toArray(new JMethod[0]);
    }

    @Override
    public JConstructor[] getDeclaredConstructors() {
        if(_constructors==null){
            LinkedHashMap<JSignature, JConstructor> _constructors=new LinkedHashMap<>();
            for (JConstructor i : getRawType().getDeclaredConstructors()) {
                JParameterizedConstructorImpl m=new JParameterizedConstructorImpl(i,new JType[0], this);
                _constructors.put(m.getSignature(),m);
            }
            this._constructors=_constructors;
        }
        return this._constructors.values().toArray(new JConstructor[0]);
    }


    @Override
    public JField[] getDeclaredFields() {
        if(_fields==null){
            LinkedHashMap<String, JField> _fields=new LinkedHashMap<>();
            for (JField i : getRawType().getDeclaredFields()) {
                JParameterizedFieldImpl m=new JParameterizedFieldImpl((JRawField) i,this);
                _fields.put(m.name(),m);
            }
            this._fields=_fields;
        }
        return this._fields.values().toArray(new JField[0]);
    }

    @Override
    public JType[] getDeclaredInnerTypes() {
        return _innerTypes().values().toArray(new JType[0]);
    }

    @Override
    public JType findDeclaredInnerTypeOrNull(String name) {
        return _innerTypes().get(name);
    }

    @Override
    public boolean isPublic() {
        return rootRaw.isPublic();
    }

    @Override
    public boolean isStatic() {
        return rootRaw.isStatic();
    }

    @Override
    public JModifierList getModifiers() {
        return rootRaw.getModifiers();
    }

    @Override
    public JType toArray(int count) {
        return JTypesSPI.getRegisteredOrRegister(
                types2().createArrayType0(this,count), getTypes()
        );
    }

    @Override
    public boolean isInterface() {
        return rootRaw.isInterface();
    }

    @Override
    public JAnnotationInstanceList getAnnotations() {
        return rootRaw.getAnnotations();
    }

    @Override
    public String getSourceName() {
        return rootRaw.getSourceName();
    }
}
