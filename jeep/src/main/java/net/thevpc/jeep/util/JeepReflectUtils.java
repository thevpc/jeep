package net.thevpc.jeep.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class JeepReflectUtils {

    public static Object getInstanceFieldValue(Object instance, Field field){
        try {
            JeepPlatformUtils.setAccessibleWorkaround(field);
            return field.get(instance);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Field not found "+field.getName());
        }
    }

    public static Object getInstanceFieldValue(Object instance, String name){
        if(instance!=null){
            Field fieldForClass = getFieldForClass(instance.getClass(), name);
            if(fieldForClass!=null){
                try {
                    return fieldForClass.get(instance);
                } catch (IllegalAccessException e) {
                    throw new IllegalArgumentException("Field not accessible "+instance.getClass().getName()+"."+name);
                }
            }
            throw new IllegalArgumentException("Field not found for type "+instance.getClass().getName()+"."+name);
        }
        throw new IllegalArgumentException("Field not found for type null."+name);
    }

    public static void setInstanceFieldValue(Object instance, Field field,Object value){
        try {
            JeepPlatformUtils.setAccessibleWorkaround(field);
            field.set(instance,value);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Field not found "+field.getName());
        }
    }

    public static void setInstanceFieldValue(Object instance, String name,Object value){
        if(instance!=null){
            Field fieldForClass = getFieldForClass(instance.getClass(), name);
            if(fieldForClass!=null){
                try {
                    fieldForClass.set(instance,value);
                } catch (IllegalAccessException e) {
                    throw new IllegalArgumentException("Field not accessible "+instance.getClass().getName()+"."+name);
                }
            }
            throw new IllegalArgumentException("Field not found "+instance.getClass().getName()+"."+name);
        }else{
            throw new IllegalArgumentException("Field not found null."+name);
        }
    }



    public static Field getFieldForClass(Class cls, String name){
        Set<Class> visited=new HashSet<>();
        try {
            Field f = cls.getDeclaredField(name);
            JeepPlatformUtils.setAccessibleWorkaround(f);
            return f;
        }catch (Exception ex){
            //
        }
        visited.add(cls);
        Stack<Class> stack=new Stack<>();
        if(cls.getSuperclass()!=null){
            stack.push(cls.getSuperclass());
            for (Class ii : cls.getInterfaces()) {
                stack.push(ii);
            }
        }
        stack.push(cls);
        while(!stack.isEmpty()){
            Class c = stack.pop();
            if(!visited.contains(c)){
                visited.add(c);
                try {
                    Field f = c.getDeclaredField(name);
                    int m = f.getModifiers();
                    if(!Modifier.isPrivate(m)){
                        JeepPlatformUtils.setAccessibleWorkaround(f);
                        return f;
                    }
                }catch (Exception ex){
                    //
                }
            }
        }
        return null;
    }


}
