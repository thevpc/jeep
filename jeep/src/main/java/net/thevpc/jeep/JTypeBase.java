package net.thevpc.jeep;

import net.thevpc.jeep.impl.functions.JSignature;
import net.thevpc.jeep.util.JTypeUtils;

import java.util.*;

public abstract class JTypeBase implements JType {
    @Override
    public JMethod getDeclaredMethod(JSignature sig) {
        JMethod m = findDeclaredMethodOrNull(sig);
        if (m == null) {
            throw new JEvalException("Method " + getName() + "." + sig + " not found");
        }
        return m;
    }

    @Override
    public JConstructor findDeclaredConstructorOrNull(String sig) {
        return findDeclaredConstructorOrNull(JSignature.of(getTypes(), sig));
    }

    @Override
    public JConstructor getDeclaredConstructor(String sig) {
        return getDeclaredConstructor(JSignature.of(getTypes(), sig));
    }
    @Override
    public JMethod getDeclaredMethod(String sig) {
        return getDeclaredMethod(JSignature.of(getTypes(), sig));
    }

    @Override
    public JConstructor getDeclaredConstructor(JType... parameterTypes) {
        return getDeclaredConstructor(JSignature.of(getName(), parameterTypes));
    }


    @Override
    public JConstructor getDeclaredConstructor(JSignature sig) {
        JConstructor c = findDeclaredConstructorOrNull(sig);
        if (c == null) {
            throw new JEvalException("Constructor " + sig + " not found");
        }
        return c;
    }
    @Override
    public JConstructor[] getPublicConstructors() {
        List<JConstructor> m = new ArrayList<>();
        for (JConstructor value : getDeclaredConstructors()) {
            if (value.isPublic()) {
                m.add(value);
            }
        }
        return m.toArray(new JConstructor[0]);
    }

    @Override
    public JField findMatchedField(String fieldName) {
        JField f = findDeclaredFieldOrNull(fieldName);
        if (f != null) {
            return f;
        }
        JType s = getSuperType();
        if (s != null) {
            f = s.findMatchedField(fieldName);
            if (f != null) {
                return f;
            }
        }
        return null;
    }

    @Override
    public synchronized JField getDeclaredField(String fieldName) {
        JField f = findDeclaredFieldOrNull(fieldName);
        if (f == null) {
            throw new JEvalException("Field " + getName() + "." + fieldName + " not found");
        }
        return f;
    }

    @Override
    public JField getPublicField(String name) {
        JField f = findDeclaredFieldOrNull(name);
        if (f != null && f.isPublic()) {
            return f;
        }
        return null;
    }

    @Override
    public JConstructor getDefaultConstructor() {
        JConstructor d = findDefaultConstructorOrNull();
        if (d == null) {
            throw new JEvalException("Default Constructor not found for " + getName());
        }
        return d;
    }

    @Override
    public JConstructor findDefaultConstructorOrNull() {
        JConstructor defaultConstructor = getDeclaredConstructor(JSignature.of(getName(), new JType[0]));
        if (defaultConstructor.isPublic()) {
            return defaultConstructor;
        }
        return null;
    }

    @Override
    public JType getDeclaredInnerType(String name) {
        JType d = findDeclaredInnerTypeOrNull(name);
        if (d == null) {
            throw new JEvalException("inner type not found : " + getName() + "." + name);
        }
        return d;
    }

    @Override
    public JMethod[] getDeclaredMethods(boolean includeParents) {
        if (!includeParents) {
            return getDeclaredMethods();
        }
        LinkedHashMap<JSignature, JMethod> all = new LinkedHashMap<>();
        for (JMethod jMethod : getDeclaredMethods()) {
            JSignature sig = jMethod.getSignature();
            all.put(sig, jMethod);
        }
        if (includeParents) {
            for (JType parent : getParents()) {
                JMethod[] jMethods = parent.getDeclaredMethods(true);
                for (JMethod jMethod : jMethods) {
                    JSignature sig = jMethod.getSignature();
                    if (!all.containsKey(sig)) {
                        all.put(sig, jMethod);
                    }
                }
            }
        }
        return all.values().toArray(new JMethod[0]);
    }

    @Override
    public JMethod[] getDeclaredMethods(String[] names, int callArgumentsCount, boolean includeParents) {
//        System.out.println(getName()+".getDeclaredMethods("+Arrays.asList(names)+",...)");
        Set<String> namesSet = new HashSet<>(Arrays.asList(names));
        LinkedHashMap<JSignature, JMethod> all = new LinkedHashMap<>();
        for (JMethod jMethod : getDeclaredMethods()) {
            if (namesSet.contains(jMethod.getName())) {
                JSignature sig = jMethod.getSignature();
                if (sig.acceptArgsCount(callArgumentsCount)) {
                    all.put(sig, jMethod);
                }
            }
        }
        if (includeParents) {
            for (JType parent : getParents()) {
                JMethod[] jMethods = parent.getDeclaredMethods(names, callArgumentsCount, true);
                for (JMethod jMethod : jMethods) {
                    JSignature sig = jMethod.getSignature();
                    if (!all.containsKey(sig)) {
                        all.put(sig, jMethod);
                    }
                }
            }
        }
        return all.values().toArray(new JMethod[0]);
    }

    @Override
    public JMethod[] getPublicMethods() {
        List<JMethod> m = new ArrayList<>();
        for (JMethod value : getDeclaredMethods()) {
            if (value.isPublic()) {
                m.add(value);
            }
        }
        return m.toArray(new JMethod[0]);
    }

    @Override
    public JType toArray() {
        return toArray(1);
    }

    @Override
    public JType firstCommonSuperType(JType other) {
        return JTypeUtils.firstCommonSuperType(this, other, getTypes());
    }

    @Override
    public JMethod findDeclaredMethodOrNull(String sig) {
        return findDeclaredMethodOrNull(JSignature.of(getTypes(), sig));
    }


    @Override
    public JType[] getParents() {
        List<JType> parents = new ArrayList<>();
        JType s = getSuperType();
        if (s != null) {
            parents.add(s);
        }
        for (JType i : getInterfaces()) {
            parents.add(i);
        }
        return parents.toArray(new JType[0]);
    }

    @Override
    public JField[] getDeclaredFieldsWithParents() {
        LinkedHashSet<JField> fields = new LinkedHashSet<>();
        fields.addAll(Arrays.asList(getDeclaredFields()));
        for (JType parent : getParents()) {
            fields.addAll(Arrays.asList(parent.getDeclaredFieldsWithParents()));
        }
        return fields.toArray(new JField[0]);
    }

    @Override
    public JField findDeclaredFieldOrNull(String fieldName) {
        for (JField jField : getDeclaredFields()) {
            if (jField.name().equals(fieldName)) {
                return jField;
            }
        }
        return null;
    }


}
