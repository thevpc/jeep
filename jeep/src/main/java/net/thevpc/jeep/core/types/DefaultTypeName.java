package net.thevpc.jeep.core.types;

import net.thevpc.jeep.JTypeNameOrVariable;
import net.thevpc.jeep.JTypeName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultTypeName implements JTypeName {
    private String name;
    private JTypeNameOrVariable[] vars;
    private int array;
    private boolean varArg;

    public DefaultTypeName(String name) {
        this(name,null,0,false);
    }

    public DefaultTypeName(String name, JTypeNameOrVariable[] vars, int array, boolean varArg) {
        this.name = name;
        this.vars = vars == null ? new JTypeNameBounded[0] : Arrays.copyOf(vars, vars.length);
        this.array = array;
        this.varArg = varArg;
    }

    @Override
    public JTypeName rawType() {
        return new DefaultTypeName(name,null,array,varArg);
    }

    @Override
    public JTypeName addArguments(JTypeNameOrVariable[] z) {
        List<JTypeNameOrVariable> all=new ArrayList<>();
        all.addAll(Arrays.asList(vars));
        all.addAll(Arrays.asList(z));
        return new DefaultTypeName(name,all.toArray(new JTypeNameOrVariable[0]),array,varArg);
    }

    public static JTypeName of(String name) {
        return JTypeNameParser.parseType(name);
    }

    static String simpleNameOf(String name) {
        int r = name.lastIndexOf('.');
        if (r < 0) {
            return name;
        } else {
            return name.substring(r + 1);
        }
    }

    @Override
    public JTypeName withSimpleName() {
        String name = simpleNameOf(this.name);
        int array = this.array;
        boolean varArg = this.varArg;
        JTypeNameOrVariable[] vars = new JTypeNameOrVariable[this.vars.length];
        for (int i = 0; i < vars.length; i++) {
            vars[i] = this.vars[i].withSimpleName();
        }
        return new DefaultTypeName(name, vars, array, varArg);
    }

    @Override
    public int varsCount() {
        return vars.length;
    }

    @Override
    public JTypeNameOrVariable varAt(int i) {
        return vars[i];
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String simpleName() {
        return withSimpleName().name();
    }

    @Override
    public boolean isArray() {
        return array > 0;
    }

    @Override
    public int arrayDimension() {
        return array;
    }

    @Override
    public JTypeName toArray() {
        return new DefaultTypeName(name, vars, array + 1, varArg);
    }

    @Override
    public JTypeName componentType() {
        if (array > 0) {
            return new DefaultTypeName(name, vars, array - 1, varArg);
        }
        return null;
    }

    @Override
    public JTypeName rootComponentType() {
        if (array > 0) {
            return new DefaultTypeName(name, vars, 0, varArg);
        }
        return this;
    }

    @Override
    public String fullName() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        if (vars.length > 0) {
            sb.append('<');
            for (int i = 0; i < vars.length; i++) {
                if (i > 0) {
                    sb.append(',');
                }
                sb.append(vars[i]);
            }
            sb.append('>');
        }
        for (int i = 0; i < array; i++) {
            sb.append("[]");
        }
        if (varArg) {
            sb.append("...");
        }
        return sb.toString();
    }

    public JTypeNameOrVariable[] vars() {
        return Arrays.copyOf(vars, vars.length);
    }

    @Override
    public boolean isVarArg(){
        return varArg;
    }

    @Override
    public JTypeName replaceVarArg(){
        if(varArg){
            return new DefaultTypeName(
                    name,vars,
                    array+1,false
            );
        }
        return this;
    }

    @Override
    public String toString() {
        return fullName();
    }
}
