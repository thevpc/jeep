package net.thevpc.jeep.util;

import net.thevpc.jeep.*;
import net.thevpc.jeep.*;
import net.thevpc.jeep.impl.JArgumentTypes;
import net.thevpc.jeep.impl.functions.JSignature;

import java.util.*;
import net.thevpc.jeep.impl.functions.ConvertedJMethod;
import net.thevpc.jeep.impl.functions.JFunctionConverted;

public class JInvokeUtils {

    static Map<String, JConverter> cached_createTypeImplicitConversions = new HashMap<>();
    static Map<GetMatchingMethodKey2, JMethod> cached_getMatchingMethod = new HashMap<>();

    public static JMethod getMatchingMethod(JType cls, String methodName, JTypePattern... parameterTypes) {
        return getMatchingMethod(true, cls, methodName, parameterTypes);
    }

    public static JMethod getMatchingMethod(boolean accessibleOnly, JType ctype, String methodName, JTypePattern... parameterTypes) {
        GetMatchingMethodKey2 k = new GetMatchingMethodKey2(accessibleOnly, ctype, methodName, parameterTypes);
        JMethod old = cached_getMatchingMethod.get(k);
        if (old == null) {
            if (cached_getMatchingMethod.containsKey(k)) {
                return null;
            }
        } else {
            return old;
        }
        if (methodName.isEmpty() || !Character.isJavaIdentifierStart(methodName.charAt(0))) {
            cached_getMatchingMethod.put(k, null);
            return null;
        }
//            System.out.println("\t DeclaredMethod? "+cls.getName()+"."+methodName);
        JType[] ttypes = JTypeUtils.typesOrNull(parameterTypes);
        if (ttypes == null) {
            return null;
        }
        JMethod method = ctype.findDeclaredMethodOrNull(JSignature.of(methodName, ttypes));
        if (method != null) {
            cached_getMatchingMethod.put(k, method);
            return method;
        }
        // search through all methods
        JMethod[] methods = accessibleOnly ? ctype.getPublicMethods() : ctype.getDeclaredMethods();//cls.getMethods();
        List<JMethodObject<JMethod>> m = new ArrayList<>();
        for (JMethod mm : methods) {
            if (mm.getName().equals(methodName)) {
                JType[] jTypes = mm.getSignature().argTypes();
                boolean canMatch = true;
                for (int j = 0; j < jTypes.length; j++) {
                    if (parameterTypes[j] == null) {
                        if (jTypes[j].isPrimitive()) {
                            canMatch = false;
                            break;
                        }
                    } else {
                        if (!jTypes[j].isAssignableFrom(ttypes[j])) {
                            canMatch = false;
                            break;
                        }
                    }
                }
                if (canMatch) {
                    m.add(new JMethodObject<>(
                            mm.getSignature(),
                            mm.getSignature(),
                            mm,
                            0
                    ));
                }
            }
        }
        JMethodObject<JMethod> rr = getMatchingMethod(m.toArray(new JMethodObject[0]), parameterTypes);
        if (rr == null) {
            cached_getMatchingMethod.put(k, null);
            return null;
        }
        cached_getMatchingMethod.put(k, rr.getMethod());
        return rr.getMethod();
    }

    public static <T> MCallCost2<T> getMatchingMethodCost(JMethodObject<T> current, JTypePattern... parameterTypes) {
        JArgumentTypes signature = current.getSignature();
        Comparable initialComparable = current.getCostObject();
        JType[] availableTypes = signature.argTypes();
        if (availableTypes.length == parameterTypes.length && !signature.isVarArgs()) {
            boolean ok = true;
            double[] cost = new double[availableTypes.length];
            for (int j = 0; j < availableTypes.length; j++) {
                JType c1 = availableTypes[j];
                JTypePattern c2 = parameterTypes[j];
                int cc2 = JTypeUtils.getAssignationCost(c1, c2);
                if (cc2 < 0) {
                    cost[j] = -1;
                    if (cost[j] < 0) {
                        ok = false;
                        break;
                    }
                } else {
                    cost[j] = cc2;
                }
            }
            if (ok) {
                //check each type
                MCallCost2 a = new MCallCost2(parameterTypes, current, cost, initialComparable);
                current.setCostObject(a);
                return a;
            }
        } else if ((availableTypes.length - 1) <= parameterTypes.length && signature.isVarArgs()) {
            boolean ok = true;
            JTypePattern[] r = new JTypePattern[availableTypes.length];
            double[] cost = new double[availableTypes.length];
            for (int j = 0; j < availableTypes.length - 1; j++) {
                JType c1 = availableTypes[j];
                JTypePattern c2 = parameterTypes[j];
                int cc2 = JTypeUtils.getAssignationCost(c1, c2);
                if (cc2 < 0) {
                    cost[j] = -1;
                    if (cost[j] < 0) {
                        ok = false;
                        break;
                    }
                } else {
                    cost[j] = cc2;
                }
            }
            if (ok) {
                JType availableType = availableTypes[availableTypes.length - 1];
                JType c3 = availableType.componentType();
                double y = 0;
                int count = 0;
                for (int j = availableTypes.length - 1; j < parameterTypes.length; j++) {
//                    JType c1 = availableTypes[j];
                    JTypePattern c2 = parameterTypes[j];
                    int cc1 = JTypeUtils.getAssignationCost(c3, c2);
//                    int cc2 = getAssignationCost(c1, c2);
                    if (cc1 < 0 /*|| cc2 < 0*/) {
                        y = -1;
                        ok = false;
                        break;
                    } else {
                        y += cc1 /*+ cc2*/;
                    }
                    count++;
                }
                if (ok) {
                    y += 0.1 + count * 0.1;
                    cost[availableTypes.length - 1] = y;
                    r[availableTypes.length - 1] = JTypePattern.of(availableTypes[availableTypes.length - 1]);
                }
            }
            if (ok) {
                MCallCost2 a = new MCallCost2(r, current, cost, initialComparable);
                current.setCostObject(a);
                return a;
            }
        }
        return null;
    }

    @Deprecated
    public static <T> JMethodObject<T> getMatchingMethod(JMethodObject<T>[] available, JTypePattern... parameterTypes) {
        MCallCost2<T> best = null;
        for (int i = 0; i < available.length; i++) {
            JMethodObject<T> a = available[i];
            MCallCost2<T> newCall = getMatchingMethodCost(a, parameterTypes);
            if (newCall != null) {
                if (best == null || best.compareTo(newCall) > 0) {
                    best = newCall;
                }
            }
        }
        return best == null ? null : best.av;
    }

    public static class MCallCost2<T> implements Comparable<MCallCost2> {

        JTypePattern[] input;
        JMethodObject<T> av;
        double[] cost;
        Comparable startingCost;

        public MCallCost2(JTypePattern[] input, JMethodObject<T> av, double[] cost, Comparable startingCost) {
            this.input = input;
            this.av = av;
            this.cost = cost;
            this.startingCost = startingCost;
        }

        private int compare(Comparable a, Comparable b) {
            if (a == b) {
                return 0;
            }
            if (a == null) {
                return 1;
            }
            if (b == null) {
                return -1;
            }
            return a.compareTo(b);
        }

        @Override
        public int compareTo(MCallCost2 o) {
            int x = compare(this.startingCost, o.startingCost);
            if (x != 0) {
                return x;
            }
            x = Integer.compare(cost.length, o.cost.length);
            if (x != 0) {
                return x;
            }
            for (int i = 0; i < cost.length; i++) {
                double val1 = cost[i];
                double val2 = o.cost[i];
                x = Double.compare(val1, val2);
                if (x != 0) {
                    return x;
                }
            }
            return 0;
        }

        @Override
        public String toString() {
            double a = 0;
            for (double v : cost) {
                a += v;
            }
            return "Cost{" + a + "=" + Arrays.toString(cost)
                    + (startingCost == null ? "" : ("+" + startingCost))
                    + "}";
        }
    }

    private static class GetMatchingMethodKey {

        boolean accessibleOnly;
        JType cls;
        String methodName;
        JType[] parameterTypes;

        public GetMatchingMethodKey(boolean accessibleOnly, JType cls, String methodName, JType[] parameterTypes) {
            this.accessibleOnly = accessibleOnly;
            this.cls = cls;
            this.methodName = methodName;
            this.parameterTypes = parameterTypes;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 73 * hash + (this.accessibleOnly ? 1 : 0);
            hash = 73 * hash + Objects.hashCode(this.cls);
            hash = 73 * hash + Objects.hashCode(this.methodName);
            hash = 73 * hash + Arrays.deepHashCode(this.parameterTypes);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final GetMatchingMethodKey other = (GetMatchingMethodKey) obj;
            if (this.accessibleOnly != other.accessibleOnly) {
                return false;
            }
            if (!Objects.equals(this.methodName, other.methodName)) {
                return false;
            }
            if (!Objects.equals(this.cls, other.cls)) {
                return false;
            }
            if (!Arrays.deepEquals(this.parameterTypes, other.parameterTypes)) {
                return false;
            }
            return true;
        }

    }

    private static class GetMatchingMethodKey2 {

        boolean accessibleOnly;
        JType cls;
        String methodName;
        JTypePattern[] parameterTypes;

        public GetMatchingMethodKey2(boolean accessibleOnly, JType cls, String methodName, JTypePattern[] parameterTypes) {
            this.accessibleOnly = accessibleOnly;
            this.cls = cls;
            this.methodName = methodName;
            this.parameterTypes = parameterTypes;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 73 * hash + (this.accessibleOnly ? 1 : 0);
            hash = 73 * hash + Objects.hashCode(this.cls);
            hash = 73 * hash + Objects.hashCode(this.methodName);
            hash = 73 * hash + Arrays.deepHashCode(this.parameterTypes);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final GetMatchingMethodKey2 other = (GetMatchingMethodKey2) obj;
            if (this.accessibleOnly != other.accessibleOnly) {
                return false;
            }
            if (!Objects.equals(this.methodName, other.methodName)) {
                return false;
            }
            if (!Objects.equals(this.cls, other.cls)) {
                return false;
            }
            if (!Arrays.deepEquals(this.parameterTypes, other.parameterTypes)) {
                return false;
            }
            return true;
        }

    }

    public static JInvokable convert(JInvokable invokable, JConverter[] args, JConverter ret) {
        boolean someConvertionNeeded = false;
        if (ret != null) {
            someConvertionNeeded = true;
        }
        if (!someConvertionNeeded) {
            if (args != null) {
                for (JConverter converter : args) {
                    if (converter != null) {
                        someConvertionNeeded = true;
                        break;
                    }
                }
            }
        }
        if (!someConvertionNeeded) {
            return invokable;
        }

        if (invokable instanceof JMethod) {
            return new ConvertedJMethod((JMethod) invokable, args, ret);
        } else if (invokable instanceof JFunction) {
            return new JFunctionConverted((JFunction) invokable, args, ret);
        } else {
            throw new JParseException("Unsupported invocable type : " + invokable.getClass().getSimpleName());
        }
    }
}
