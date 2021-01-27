package net.thevpc.jeep;

import net.thevpc.jeep.impl.functions.JSignature;

import java.util.Arrays;
import java.util.Objects;

public class JTypePattern {
    private String argName;
    private JType type;
    private JType[] lambdaArgTypes;
    private JType lambdaReturnType;

    public JTypePattern(JType type) {
        this.type = type;
        if (type == null) {
            throw new NullPointerException();
        }
    }

    public JTypePattern(JType[] lambdaArgTypes, JType lambdaReturnType) {
        this.lambdaArgTypes = lambdaArgTypes;
        this.lambdaReturnType = lambdaReturnType;
        if (lambdaArgTypes == null) {
            throw new NullPointerException();
        }
    }

    public static JTypePattern of(JType[] args, JType lambdaReturnTypes) {
        return new JTypePattern(args, lambdaReturnTypes);
    }

    public static JTypePattern ofLambdaOrNull(JType[] args, JType lambdaReturnTypes) {
        return args == null ? null : new JTypePattern(args,lambdaReturnTypes);
    }

    public static JTypePattern ofTypeOrNull(JType type) {
        return type == null ? null : new JTypePattern(type);
    }

    public static JTypePattern of(JType type) {
        return new JTypePattern(type);
    }

    public String getArgName() {
        return argName;
    }

    public JTypePattern setArgName(String argName) {
        this.argName = argName;
        return this;
    }

    public static String signatureStringNoPars(JTypePattern... all) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < all.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(all[i]);
        }
        return sb.toString();
    }

    public static String signatureString(JTypePattern... all) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        try {
            for (int i = 0; i < all.length; i++) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append(all[i].toString()); //force non null
            }
            sb.append(")");
        } catch (NullPointerException ex) {
            throw new JFixMeLaterException("Invalid null args for signatureString");
        }
        return sb.toString();
    }

    public JType getLambdaReturnType() {
        return lambdaReturnType;
    }
//    public static JType[] jTypes(JTypePattern... all){
//        JType[] a=new JType[all.length];
//        for (int i = 0; i < all.length; i++) {
//            a[i]=all[i].getType();
//        }
//        return a;
//    }
//
//    public static JTypePattern jTypeOrLambda(JNode node){
//        if(node instanceof HNLiteral && ((HNLiteral) node).getValue()==null){
//            return new JTypePattern((JType) null);
//        }else if(node instanceof HNLambdaExpression){
//            HNLambdaExpression lx=(HNLambdaExpression) node;
//            List<HNDeclareIdentifier> arguments = lx.getArguments();
//            JType[] lax=new JType[arguments.size()];
//            for (int j = 0; j < lax.length; j++) {
//                JTypeName tn = arguments.get(j).getIdentifierTypeName();
//                JType t = arguments.get(j).getIdentifierType();
//                if(t==null && tn!=null){
//                    return null;
//                }
//                lax[j]= t;
//            }
//            return new JTypePattern(lax);
//        }else {
//            JType t=node==null?null:node.getType();
//            if(t==null){
//                return null;
//            }
//            return new JTypePattern(t);
//        }
//    }
//
//    public static JTypePattern[] jTypeOrLambdas(JNode... all){
//        JTypePattern[] aa=new JTypePattern[all.length];
//        for (int i = 0; i < all.length; i++) {
//            aa[i]= jTypeOrLambda(all[i]);
//            if(aa[i]==null){
//                return null;
//            }
//        }
//        return aa;
//    }

    public boolean isType() {
        return lambdaArgTypes == null;
    }

    public boolean isLambda() {
        return lambdaArgTypes != null;
    }

    public JType getType() {
        if (!isType()) {
            throw new IllegalArgumentException("Not a Type");
        }
        return type;
    }

    public JType[] getLambdaArgTypes() {
        if (!isLambda()) {
            throw new IllegalArgumentException("Not a Lambda");
        }
        return lambdaArgTypes;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(type);
        result = 31 * result + Arrays.hashCode(lambdaArgTypes);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JTypePattern that = (JTypePattern) o;
        return Objects.equals(type, that.type) &&
                Arrays.equals(lambdaArgTypes, that.lambdaArgTypes);
    }

    @Override
    public String toString() {
        if (isType()) {
            return type == null ? "null" : type.getName();
        }
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < lambdaArgTypes.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(lambdaArgTypes[i] == null ? "?" : lambdaArgTypes[i].getName());
        }
        sb.append(")->...");
        return sb.toString();
    }

    public boolean matchesType(JType typeToMatchTo) {
        if (isType()) {
            return typeToMatchTo.isAssignableFrom(getType());
        }
        JMethod[] dm = Arrays.stream(typeToMatchTo.getDeclaredMethods()).filter(x -> x.isAbstract() && !x.isStatic())
                .toArray(JMethod[]::new);
        if (dm.length == 1) {
            JMethod cm = dm[0];
            JType[] lambdaArgTypes = getLambdaArgTypes();
            JSignature signature = cm.getSignature();
            if(signature.accept(lambdaArgTypes)){
                return true;
            }
        }
        return false;
    }
}
