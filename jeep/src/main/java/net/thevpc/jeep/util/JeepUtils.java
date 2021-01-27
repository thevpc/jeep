/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.util;

import net.thevpc.jeep.JOperatorPrecedences;

import java.io.*;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author thevpc
 */
public class JeepUtils {
    public static final String TAB = "\t";
    public static final String NEWLINE = "\n";

    public static <T> T coalesce(T... a) {
        for (T t : a) {
            if (t != null) {
                return t;
            }
        }
        return null;
    }

    public static boolean isDefaultOp(String c) {
        return (c.length() > 0 && JOperatorPrecedences.OPERATORS.indexOf(c.charAt(0)) >= 0);
    }


    public static Object joinArraysAsType(Class componentType, Object[] arrays) {
        if (arrays.length == 0) {
            throw new IllegalArgumentException("No Array to join");
        }
        int lenSum = 0;
        for (int i = 0; i < arrays.length; i++) {
            Class c = arrays[i].getClass().getComponentType();
            if (!componentType.isAssignableFrom(c)) {
                throw new IllegalArgumentException("Array expected");
            }
            lenSum += Array.getLength(arrays[i]);
        }
        int pos = 0;
        Object newObj = Array.newInstance(componentType, lenSum);
        for (int i = 0; i < arrays.length; i++) {
            int len = Array.getLength(arrays[i]);
            System.arraycopy(arrays[i], 0, newObj, pos, len);
            pos += len;
        }
        return newObj;
    }


    public static boolean convertToBoolean(Object u) {
        if (u == null) {
            return false;
        }
//        if (u instanceof JNodeLiteral) {
//            u = ((JNodeLiteral) u).getValue();
//        }
        if (u == null) {
            return false;
        }
        if (u instanceof Boolean) {
            return ((Boolean) u).booleanValue();
        }
        if (u instanceof Number) {
            double d = ((Number) u).doubleValue();
            return Double.isNaN(d) && d != 0;
        }
        String s = (String) u.toString();
        if ("true".equalsIgnoreCase(s)) {
            return true;
        }
        try {
            double d = Double.parseDouble(s);
            return Double.isNaN(d) && d != 0;
        } catch (Exception ex) {
            //
        }
        return false;
    }

    public static int getArrayDim(Class arrayType) {
        if (arrayType.isArray()) {
            return 1 + getArrayDim(arrayType.getComponentType());
        }
        return 0;
    }

    public static Class toArrType(Class arrayType) {
        return Array.newInstance(arrayType, 0).getClass();
    }

    public static Class toArrType(Class arrayType, int count) {
        Class t = arrayType;
        for (int i = 0; i < count; i++) {
            t = Array.newInstance(t, 0).getClass();

        }
        return t;
    }

    public static Class getArrayRootComponentType(Class arrayType) {
        if (arrayType.isArray()) {
            return getArrayRootComponentType(arrayType.getComponentType());
        }
        return arrayType;
    }

    public static String getSimpleClassName(Class[] cls) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cls.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(getSimpleClassName(cls[i]));
        }
        return sb.toString();
    }

    public static String getSimpleClassName(Class cls) {
        if (cls.isArray()) {
            return getSimpleClassName(cls.getComponentType()) + "[]";
        }
        return cls.getSimpleName();
    }

    public static void validateFunctionName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Empty operator");
        }
        for (char c : name.toCharArray()) {
            if (Character.isWhitespace(c)) {
                throw new IllegalArgumentException("operator could not contain white characters : " + name);
            }
        }
    }

    public static String indent(String str) {
        return indent("  ", str);
    }

    public static String indent(String prefix, String str) {
        String[] s = str.split("\n");
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String s1 : s) {
            if (first) {
                first = false;
            } else {
                sb.append("\n");
            }
            sb.append(prefix);
            sb.append(s1);
        }
        return sb.toString();
    }

    public static <T> T[] arrayAppend(Class arrayElementType, T[] one, T other) {
        T[] two = (T[]) Array.newInstance(arrayElementType, 1);
        two[0] = other;
        return arrayConcat(arrayElementType, one, two);
    }

    public static <T> T[] arrayAppend(Class arrayElementType, T one, T[] otherArray, T two) {
        T[] oneArray = arraySingleton(arrayElementType != null ? arrayElementType : otherArray.getClass(), one);
        T[] twoArray = arraySingleton(arrayElementType != null ? arrayElementType : otherArray.getClass(), two);
        T[] t2 = arrayConcat(arrayElementType, oneArray, otherArray);
        return arrayConcat(arrayElementType, t2, twoArray);
    }

    private static <T> T[] arraySingleton(Class arrayElementType, T one) {
        T[] a = (T[]) Array.newInstance(arrayElementType, 1);
        a[0] = one;
        return a;
    }

    public static <T> T[] arrayAppend(Class arrayElementType, T one, T[] otherArray) {
        T[] oneArray = arraySingleton(arrayElementType != null ? arrayElementType : otherArray.getClass(), one);
        return arrayConcat(arrayElementType, oneArray, otherArray);
    }

    public static <T> T[] arrayConcat(Class arrayElementType, T[]... all) {
        Class<? extends Object[]> arrayType = arrayElementType != null ? arrayElementType : all[0].getClass();
        List<T> a = new ArrayList<>();
        for (T[] ts : all) {
            a.addAll(Arrays.asList(ts));
        }
        return a.toArray((T[]) Array.newInstance(arrayType, 0));
    }

    public static <T> T[] arrayConcatNonNull(Class arrayElementType, T[]... all) {
        List<T> a = new ArrayList<>();
        for (T[] ts : all) {
            if (ts != null) {
                a.addAll(Arrays.asList(ts));
            }
        }
        return a.toArray((T[]) Array.newInstance(arrayElementType, 0));
    }

    public static char[] urlToCharArray(URL r) {
        try (InputStream in = r.openStream()) {
            return inputStreamToCharArray(in);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static char[] fileToCharArray(File r) {
        try (InputStream in = new FileInputStream(r)) {
            return inputStreamToCharArray(in);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static char[] inputStreamToCharArray(InputStream r) {
        return readerToCharArray(new InputStreamReader(r));
    }

    public static char[] readerToCharArray(Reader r) {
        CharArrayWriter w = new CharArrayWriter();
        char[] cc = new char[1024];
        int len;
        while (true) {
            try {
                if (!((len = r.read(cc)) > 0)) break;
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            w.write(cc, 0, len);
        }
        return w.toCharArray();
    }

    public static String threadIndent() {
        StackTraceElement[] s = new Throwable().getStackTrace();
        StringBuilder sb = new StringBuilder(s.length);
        for (int i = 0; i < s.length - 1; i++) {
            sb.append(' ');
        }
        return sb.toString();
    }

    public static <T> T first(Set<T> s) {
        if (s == null) {
            return null;
        }
        for (T t : s) {
            return t;
        }
        return null;
    }


    public static boolean consumeModifier(int[] modifierRef, int mod) {
        if ((modifierRef[0] & mod) != 0) {
            modifierRef[0] = modifierRef[0] & (~mod);
            return true;
        }
        return false;
    }

    public static String propertyToGetter(String property, boolean boolProperty) {
        String prefix = boolProperty ? "is" : "get";
        return prefix + JStringUtils.capitalize(property);
    }

    public static String propertyToSetter(String property) {
        String prefix = "set";
        return prefix + JStringUtils.capitalize(property);
    }
}
