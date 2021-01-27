/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.impl.vars;

import net.thevpc.jeep.*;
import net.thevpc.jeep.util.JTypeUtils;
import net.thevpc.jeep.util.JeepUtils;
import net.thevpc.jeep.*;
import net.thevpc.jeep.core.DefaultJVar;
//import net.thevpc.jeep.core.nodes.JNodeVarName;

import java.util.HashMap;
import java.util.Map;
import net.thevpc.jeep.impl.functions.DefaultJInvokeContext;

/**
 * @author thevpc
 */
public class JVarsImpl implements JVars {

    private final Map<String, JVar> variables = new HashMap<>();
    private final Map<String, String> variablesAliases = new HashMap<>();
    private JContext context;
    private Map<String, JVar> cacheVars = new HashMap<>();
    private boolean writable = true;
    private JVars parent;

    public JVarsImpl(JContext context, JVars parent) {
        this.context = context;
        this.parent = parent;
    }

    @Override
    public JVar declareConst(String name, Class type, Object value) {
        return declareConst(name, context.types().forName(type.getName()), value);
    }

    @Override
    public JVar declareVar(String name, Class type, Class undefinedType, Object value) {
        return declareVar(name, context.types().forName(type.getName()), context.types().forName(undefinedType.getName()), value);
    }

    @Override
    public JVar declareVar(String name, Class type, Object value) {
        return declareVar(name, context.types().forName(type.getName()), value);
    }

    public boolean isWritable() {
        return writable;
    }

    public void setWritable(boolean writable) {
        this.writable = writable;
    }

    private void chechWritable(String name) {
        if (!isWritable()) {
            throw new UnsupportedOperationException("Read only vars. Cannot update " + name);
        }
    }

//    public JNode getVariableNode(String var) {
//        JNode v = findNode(var);
//        if (v == null) {
//            throw new NoSuchVariableException(var);
//        }
//        return v;
//    }
    public boolean isReadOnlyVariable(String varName) {
        JVar ff = find(getCanonicalName(varName));
        return ff != null && ff.isReadOnly();
    }

    @Override
    public JVar declareConst(String name, JType type, Object value) {
        chechWritable(name);
        JeepUtils.validateFunctionName(name);
        JVar v = declareVar(name, type, type, value);
        v.setReadOnly(true);
        return v;
    }

    @Override
    public JVar declareConst(String name, Object value) {
        chechWritable(name);
        return declareConst(name, value == null
                ? JTypeUtils.forObject(context.types())
                : context.types().typeOf(value),
                 value);
    }

    @Override
    public JVar declareVar(String name, JType type, JType undefinedType, Object value) {
        chechWritable(name);
        JeepUtils.validateFunctionName(name);
        if (type == null) {
            if (value != null) {
                type = context.types().typeOf(value);
            }
        }
        if (type == null) {
            type = context.types().typeOf("Object");
        }
        if (undefinedType == null) {
            undefinedType = type;
        }
        JVar value2 = new DefaultJVar(name, type, undefinedType, value);
        declareVar(value2);
        return value2;
    }

    @Override
    public JVar declareVar(String name, JType type, Object value) {
        chechWritable(name);
        JeepUtils.validateFunctionName(name);
        return declareVar(name, type, type, value);
    }

    @Override
    public JVar declareVar(JVar def) {
        chechWritable(def.name());
        JeepUtils.validateFunctionName(def.name());
        String cn = getCanonicalName(def.name());
        JVar old = variables.get(cn);
        if (old != null) {
            throw new IllegalArgumentException("Variable already declared " + def.name());
        }
        variables.put(cn, def);
        invalidateVarCache(def.name());
        return def;
    }

    @Override
    public JVars declareAlias(String alias, String name) {
        chechWritable(alias);
        variablesAliases.put(getCanonicalName(alias), getCanonicalName(name));
        return this;
    }

    @Override
    public JVars undeclareVar(String name) {
        chechWritable(name);
        variables.remove(getCanonicalName(name));
        invalidateVarCache(name);
        return this;
    }

    @Override
    public JVars undeclareAlias(String alias) {
        chechWritable(alias);
        variablesAliases.remove(getCanonicalName(alias));
        return this;
    }

    @Override
    public JVar find(String var) {
        JVar p = cacheVars.get(var);
        if (p != null) {
            return p;
        }
        if (cacheVars.containsKey(var)) {
            return null;
        }
        String canonicalName = getCanonicalName(var);

        JVar ff = findVariableInternal(var);
        if (ff != null) {
            cacheVars.put(var, ff);
            return ff;
        }
        if (ff == null) {
            String ref = variablesAliases.get(canonicalName);
            if (ref != null) {
                ff = find(getCanonicalName(ref));
                if (ff != null) {
                    cacheVars.put(var, ff);
                    return ff;
                }
            }
        }
        for (JResolver resolver : context.resolvers().getResolvers()) {
            if (resolver != null) {
                ff = resolver.resolveVariable(var, context);
                if (ff != null) {
                    cacheVars.put(var, ff);
                    return ff;
                }
            }
        }
        cacheVars.put(var, null);
        return null;
    }

//    @Override
//    public JNode getName(String varName) {
//        JVar o = get(varName);
////        return new JNodeVarName(varName, o == null ? Object.class : o.getEffectiveType(this));
//        return new JNodeVarName(varName);
//    }
    @Override
    public JVar get(String var) {
        JVar ff = find(var);
        if (ff == null) {
            throw new NoSuchVariableException(var);
        }
        return ff;
    }

    @Override
    public void setValue(String varName, Object value, JInvokeContext invokeContext) {
        if (invokeContext == null) {
            invokeContext = new DefaultJInvokeContext(context, context.evaluators().newEvaluator(), null, new JEvaluable[0], null, JCallerInfo.NO_CALLER,null);
        }
        get(varName).setValue(value, invokeContext);
    }

    @Override
    public Object getValue(String varName, JInvokeContext invokeContext) {
        if (invokeContext == null) {
            invokeContext = new DefaultJInvokeContext(context, context.evaluators().newEvaluator(), null, new JEvaluable[0], null, JCallerInfo.NO_CALLER,null);
        }
        return get(varName).getValue(invokeContext);
    }

//    public JNode findNode(String var) {
//        JVar ff = find(var);
//        if (ff != null) {
//            if (ff.isUndefinedValue()) {
//                return new JNodeVarName(ff.name());
//            } else {
//                return new JNodeVarName(ff.name());
////                return new JNodeVarName(ff.getName(), ff.getEffectiveType(this));
//            }
//        }
//        return null;
//    }
    public void invalidateVarCache(String name) {
        cacheVars.remove(getCanonicalName(name));
    }

    public void invalidateVarCache() {
        cacheVars.clear();
    }

    public String getCanonicalName(String name) {
        if (!context.tokens().config().isCaseSensitive()) {
            return name.toUpperCase();
        }
        return name;
    }

    protected JVar findVariableInternal(String var) {
        JVar v = variables.get(getCanonicalName(var));
        if (v != null) {
            return v;
        }
        if (parent != null) {
            return parent.find(var);
        }
        return null;
    }

    protected JVar findParentVariable(String var) {
        return null;
    }

}
