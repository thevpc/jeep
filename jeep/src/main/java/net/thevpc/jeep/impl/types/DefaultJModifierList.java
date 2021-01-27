package net.thevpc.jeep.impl.types;

import net.thevpc.jeep.JModifier;
import net.thevpc.jeep.JPrimitiveModifier;

import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.util.*;

public class DefaultJModifierList implements JModifierList{
    public static final JPrimitiveModifier PRIVATE = new JPrimitiveModifier("private");
    public static final JPrimitiveModifier PROTECTED = new JPrimitiveModifier("protected");
    public static final JPrimitiveModifier PUBLIC = new JPrimitiveModifier("public");
    public static final JPrimitiveModifier SYNTHETIC = new JPrimitiveModifier("synthetic");
    public static final JPrimitiveModifier STATIC = new JPrimitiveModifier("static");
    public static final JPrimitiveModifier SYNCHRONIZED = new JPrimitiveModifier("synchronized");
    public static final JPrimitiveModifier ABSTRACT = new JPrimitiveModifier("abstract");
    public static final JPrimitiveModifier FINAL = new JPrimitiveModifier("final");
    public static final JPrimitiveModifier INTERFACE = new JPrimitiveModifier("interface");
    public static final JPrimitiveModifier NATIVE = new JPrimitiveModifier("native");
    public static final JPrimitiveModifier VOLATILE = new JPrimitiveModifier("volatile");
    public static final JPrimitiveModifier TRANSIENT = new JPrimitiveModifier("transient");
    public static final JPrimitiveModifier STRICTFP = new JPrimitiveModifier("strictfp");
    public static final JPrimitiveModifier BRIDGE = new JPrimitiveModifier("bridge");
    public static final JPrimitiveModifier ENUM = new JPrimitiveModifier("enum");
    public static final JPrimitiveModifier ANNOTATION = new JPrimitiveModifier("annotation");
    public static final JPrimitiveModifier MANDATED = new JPrimitiveModifier("mandated");
    private static final JPrimitiveModifier[] _ALL= {PRIVATE,PROTECTED,PUBLIC,SYNTHETIC,STATIC,SYNCHRONIZED
            ,ABSTRACT
            ,FINAL
            ,INTERFACE
            ,NATIVE
            ,VOLATILE
            ,TRANSIENT
            ,STRICTFP
            ,BRIDGE
            ,ENUM
            ,ANNOTATION
            ,MANDATED
    };

    private static final Map<String,JPrimitiveModifier> _DEFAULT_MODIFIERS=new HashMap<>();
    static {
        for (JPrimitiveModifier jPrimitiveModifier : _ALL){
            _DEFAULT_MODIFIERS.put(jPrimitiveModifier.name(),jPrimitiveModifier);
        }
    }

    public static JPrimitiveModifier getDefault(String name){
        return _DEFAULT_MODIFIERS.get(name);
    }

    private List<JModifier> list=new ArrayList<>(); //with duplicates
    private Set<JModifier> set=new HashSet<>(); //without duplicates

    public void addAll(JModifier... Modifiers){
        if(Modifiers!=null) {
            for (JModifier a : Modifiers) {
                add(a);
            }
        }
    }

    public void add(JModifier a){
        list.add(a);
    }

    public void remove(JModifier a){
        list.remove(a);
        set.clear();
        set.addAll(list);
    }

    @Override
    public JModifier[] toArray() {
        return list.toArray(new JModifier[0]);
    }

    @Override
    public <T> T[] toArray(Class<T> cls) {
        return list.toArray((T[]) Array.newInstance(cls,0));
    }

    @Override
    public List<JModifier> toList() {
        return list;
    }

    @Override
    public boolean contains(JModifier a) {
        return set.contains(a);
    }

    static final int J_BRIDGE    = 0x00000040;
    static final int J_VARARGS   = 0x00000080;
    static final int J_SYNTHETIC = 0x00001000;
    static final int J_ANNOTATION  = 0x00002000;
    static final int J_ENUM      = 0x00004000;
    static final int J_MANDATED  = 0x00008000;

    public void addJavaModifiers(int modifiers){
        int[] modifierRef=new int[]{modifiers};
        while(modifierRef[0]!=0){
            if(consumeModifier(modifierRef, Modifier.ABSTRACT)){
                add(ABSTRACT);
            }else if(consumeModifier(modifierRef, Modifier.FINAL)){
                add(FINAL);
            }else if(consumeModifier(modifierRef, Modifier.INTERFACE)){
                add(INTERFACE);
            }else if(consumeModifier(modifierRef, Modifier.NATIVE)){
                add(NATIVE);
            }else if(consumeModifier(modifierRef, Modifier.PRIVATE)){
                add(PRIVATE);
            }else if(consumeModifier(modifierRef, Modifier.PROTECTED)){
                add(PROTECTED);
            }else if(consumeModifier(modifierRef, Modifier.PUBLIC)){
                add(PUBLIC);
            }else if(consumeModifier(modifierRef, Modifier.STATIC)){
                add(STATIC);
            }else if(consumeModifier(modifierRef, Modifier.SYNCHRONIZED)){
                add(SYNCHRONIZED);
            }else if(consumeModifier(modifierRef, Modifier.VOLATILE)){
                add(VOLATILE);
            }else if(consumeModifier(modifierRef, Modifier.TRANSIENT)){
                add(TRANSIENT);
            }else if(consumeModifier(modifierRef, Modifier.STRICT)){
                add(STRICTFP);
            }else if(consumeModifier(modifierRef, J_SYNTHETIC)){
                add(SYNTHETIC);
            }else if(consumeModifier(modifierRef, J_BRIDGE)){
                add(BRIDGE);
            }else if(consumeModifier(modifierRef, J_ANNOTATION)){
                add(ANNOTATION);
            }else if(consumeModifier(modifierRef, J_ENUM)){
                add(ENUM);
            }else if(consumeModifier(modifierRef, J_MANDATED)){
                add(MANDATED);
            }else{
                throw new IllegalArgumentException("Unsupported");
            }
        }
    }

    private static boolean consumeModifier(int[] modifierRef,int mod){
        if((modifierRef[0]&mod)!=0){
            modifierRef[0]=modifierRef[0] & (~mod);
            return true;
        }
        return false;
    }
}
