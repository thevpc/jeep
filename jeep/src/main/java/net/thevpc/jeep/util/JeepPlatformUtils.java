package net.thevpc.jeep.util;

import java.lang.reflect.AccessibleObject;
import java.util.*;

/**
 * @author thevpc
 */
public class JeepPlatformUtils {

    public final static Map<Class, Class> REF_TO_PRIMITIVE_TYPES = new HashMap<Class, Class>();
    final static Map<Class, Class> PRIMITIVE_TO_REF_TYPES = new HashMap<Class, Class>();

    static {
//        DEFAULT_VALUES_BY_TYPE.put(Short.TYPE, (short) 0);
//        DEFAULT_VALUES_BY_TYPE.put(Long.TYPE, 0L);
//        DEFAULT_VALUES_BY_TYPE.put(Integer.TYPE, 0);
//        DEFAULT_VALUES_BY_TYPE.put(Double.TYPE, 0.0);
//        DEFAULT_VALUES_BY_TYPE.put(Float.TYPE, 0.0f);
//        DEFAULT_VALUES_BY_TYPE.put(Byte.TYPE, (byte) 0);
//        DEFAULT_VALUES_BY_TYPE.put(Character.TYPE, (char) 0);
//        DEFAULT_VALUES_BY_TYPE.put(Boolean.TYPE, Boolean.FALSE);

        PRIMITIVE_TO_REF_TYPES.put(Short.TYPE, Short.class);
        PRIMITIVE_TO_REF_TYPES.put(Long.TYPE, Long.class);
        PRIMITIVE_TO_REF_TYPES.put(Integer.TYPE, Integer.class);
        PRIMITIVE_TO_REF_TYPES.put(Double.TYPE, Double.class);
        PRIMITIVE_TO_REF_TYPES.put(Float.TYPE, Float.class);
        PRIMITIVE_TO_REF_TYPES.put(Byte.TYPE, Byte.class);
        PRIMITIVE_TO_REF_TYPES.put(Character.TYPE, Character.class);
        PRIMITIVE_TO_REF_TYPES.put(Boolean.TYPE, Boolean.class);

        REF_TO_PRIMITIVE_TYPES.put(Short.class, Short.TYPE);
        REF_TO_PRIMITIVE_TYPES.put(Long.class, Long.TYPE);
        REF_TO_PRIMITIVE_TYPES.put(Integer.class, Integer.TYPE);
        REF_TO_PRIMITIVE_TYPES.put(Double.class, Double.TYPE);
        REF_TO_PRIMITIVE_TYPES.put(Float.class, Float.TYPE);
        REF_TO_PRIMITIVE_TYPES.put(Byte.class, Byte.TYPE);
        REF_TO_PRIMITIVE_TYPES.put(Character.class, Character.TYPE);
        REF_TO_PRIMITIVE_TYPES.put(Boolean.class, Boolean.TYPE);
    }

    public static Class toBoxingType(Class type) {
        Class t = PRIMITIVE_TO_REF_TYPES.get(type);
        if (t != null) {
            return t;
        }
        return type;
    }

    public static Class toPrimitiveTypeOrNull(Class type) {
        if (type.isPrimitive()) {
            return type;
        }
        Class t = REF_TO_PRIMITIVE_TYPES.get(type);
        if (t != null) {
            return t;
        }
        return null;
    }

    public static Class toPrimitiveType(Class type) {
        Class t = REF_TO_PRIMITIVE_TYPES.get(type);
        if (t != null) {
            return t;
        }
        return type;
    }

    public static boolean isAssignableFrom(Class parent, Class child) {
        if (parent.isAssignableFrom(child)) {
            return true;
        }
        if (toBoxingType(parent).isAssignableFrom(toBoxingType(child))) {
            return true;
        }
        Class c1 = toPrimitiveTypeOrNull(parent);
        Class c2 = toPrimitiveTypeOrNull(child);
        if (c1 != null && c2 != null) {
            if (Boolean.TYPE.equals(c1)) {
                return c2.equals(Boolean.TYPE);
            }
            if (Byte.TYPE.equals(c1)) {
                return c2.equals(Byte.TYPE);
            }
            if (Short.TYPE.equals(c1)) {
                return c2.equals(Byte.TYPE) || c2.equals(Short.TYPE);
            }
            if (Character.TYPE.equals(c1)) {
                return c2.equals(Character.TYPE);
            }
            if (Integer.TYPE.equals(c1)) {
                return c2.equals(Byte.TYPE) || c2.equals(Short.TYPE) || c2.equals(Character.TYPE) || c2.equals(Integer.TYPE);
            }
            if (Long.TYPE.equals(c1)) {
                return c2.equals(Byte.TYPE) || c2.equals(Short.TYPE) || c2.equals(Character.TYPE) || c2.equals(Integer.TYPE) || c2.equals(Long.TYPE);
            }
            if (Float.TYPE.equals(c1)) {
                return c2.equals(Byte.TYPE) || c2.equals(Short.TYPE) || c2.equals(Character.TYPE) || c2.equals(Integer.TYPE);
            }
            if (Double.TYPE.equals(c1)) {
                return c2.equals(Byte.TYPE) || c2.equals(Short.TYPE) || c2.equals(Character.TYPE) || c2.equals(Integer.TYPE) || c2.equals(Long.TYPE) || c2.equals(Float.TYPE) || c2.equals(Double.TYPE);
            }
        }
        return false;
    }

    public static void setAccessibleWorkaround(AccessibleObject o) {
        if (o == null || o.isAccessible()) {
            return;
        }
        try {
            o.setAccessible(true);
        } catch (SecurityException e) { // NOPMD
            // ignore in favor of subsequent IllegalAccessException
        }
    }

}
