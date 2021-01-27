/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.impl.functions;

import net.thevpc.jeep.JType;
import net.thevpc.jeep.JTypeName;
import net.thevpc.jeep.JTypes;
import net.thevpc.jeep.*;
import net.thevpc.jeep.impl.JArgumentTypes;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author thevpc
 */
public class JSignature extends JArgumentTypes {

    private final String name;

    public static JSignature of(JTypes types, String signature) {
        return JMethodSignatureParser.parseSignature(signature,types);
    }
    public static JSignature of(JTypes types, JNameSignature signature) {
        return of(
                signature.name(),
                Arrays.stream(signature.argTypes())
                        .map(types::forName)
                        .toArray(JType[]::new),
                signature.isVarArgs()
        );
    }

    public JSignature(String name, JType[] operandTypes, boolean varArg) {
        super(operandTypes, varArg);
        this.name = name;
    }

    public static JSignature of(String name, JType... operandTypes) {
        return of(name,operandTypes,false);
    }

    public static JSignature of(String name, JType[] operandTypes, boolean varArg) {
        return new JSignature(name,operandTypes,varArg);
    }

//    public static JSignature of(JContext context, String name, JNode... nargs) {
//        JType[] ntypes = JNodeUtils.getTypes(nargs);
//        return JSignature.of(name, ntypes);
//    }

    public static JSignature of(JTypes types, String name, String... args) {
        String[] a2 = Arrays.copyOf(args, args.length);
        boolean varArg = false;
        if (a2.length > 0 && a2[a2.length - 1].endsWith("...")) {
            a2[a2.length - 1] = a2[a2.length - 1].substring(0, a2[a2.length - 1].length() - 3) + "[]";
            varArg = true;
        }
        JType[] operandTypes = types.forName(a2);
        for (int i = 0; i < operandTypes.length; i++) {
            if(operandTypes[i]==null){
                throw new IllegalArgumentException("type not found "+a2[i]);
            }
        }
        return new JSignature(name, operandTypes, varArg);
    }

    public JSignature toNoVarArgs() {
        if (isVarArgs()) {
            return new JSignature(name(), argTypes(), false);
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
        JSignature JSignature = (JSignature) o;
        return Objects.equals(name, JSignature.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }

    @Override
    public String toString() {
        return ((name == null || name.isEmpty()) ? "<anonymous>" : name) + super.toString();
    }

    public JSignature setName(String name) {
        return of(name,argTypes());
    }

    public JNameSignature nameSignature(){
        return JNameSignature.of(
                name(),
                Arrays.stream(argTypes())
                        .map(JType::typeName)
                        .toArray(JTypeName[]::new)
        );
    }
}
