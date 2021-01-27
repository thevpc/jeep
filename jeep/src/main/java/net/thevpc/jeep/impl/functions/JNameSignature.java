/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.impl.functions;

import net.thevpc.jeep.JTypeName;
import net.thevpc.jeep.*;
import net.thevpc.jeep.core.types.DefaultTypeName;
import net.thevpc.jeep.impl.JArgumentTypeNames;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author thevpc
 */
public class JNameSignature extends JArgumentTypeNames {

    private final String name;

    public static JNameSignature of(String signature) {
        return JMethodSignatureParser.parseNameSignature(signature);
    }

    public JNameSignature(String name, JTypeName[] operandTypes, boolean varArg) {
        super(operandTypes, varArg);
        this.name = name;
    }

    public static JNameSignature of(String name, JTypeName... operandTypes) {
        JTypeName[] aa=Arrays.copyOf(operandTypes,operandTypes.length);
        boolean isVarArg=false;
        if(aa.length>0 && aa[aa.length-1]!=null && aa[aa.length-1].isVarArg()){
            aa[aa.length-1]=aa[aa.length-1].replaceVarArg();
            isVarArg=true;
        }
        return of(name,aa,isVarArg);
    }

    public static JNameSignature of(String name, JTypeName[] operandTypes, boolean varArg) {
        return new JNameSignature(name,operandTypes,varArg);
    }


    public static JNameSignature of(String name, String... args) {
        String[] a2 = Arrays.copyOf(args, args.length);
        for (int i = 0; i < a2.length; i++) {
            if(a2[i]==null){
                throw new IllegalArgumentException("type not found "+a2[i]);
            }
        }
        boolean varArg = false;
        if (a2.length > 0 && a2[a2.length - 1].endsWith("...")) {
            a2[a2.length - 1] = a2[a2.length - 1].substring(0, a2[a2.length - 1].length() - 3) + "[]";
            varArg = true;
        }
        JTypeName[] operandTypes = new JTypeName[a2.length];
        for (int i = 0; i < a2.length; i++) {
            operandTypes[i]= DefaultTypeName.of(a2[i]);
            if(operandTypes[i]==null){
                throw new IllegalArgumentException("type not found "+a2[i]);
            }
        }
        return new JNameSignature(name, operandTypes, varArg);
    }

    public JNameSignature toNoVarArgs() {
        if (isVarArgs()) {
            return new JNameSignature(name(), argTypes(), false);
        }
        return this;
    }

    public String name() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        JNameSignature JSignature = (JNameSignature) o;
        return Objects.equals(name, JSignature.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }

    public String toString() {
        return ((name == null || name.isEmpty()) ? "<anonymous>" : name) + super.toString();
    }

    public JNameSignature setName(String name) {
        return of(name,argTypes());
    }
}
