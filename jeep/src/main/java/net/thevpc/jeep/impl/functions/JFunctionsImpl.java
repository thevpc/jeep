/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.impl.functions;

import net.thevpc.jeep.*;
import net.thevpc.jeep.util.JInvokeUtils;
import net.thevpc.jeep.util.JMethodObject;
import net.thevpc.jeep.util.JTypeUtils;
import net.thevpc.jeep.util.JeepUtils;
import net.thevpc.jeep.impl.ArgsPossibility;

import java.util.*;
import java.util.function.Function;

/**
 * @author thevpc
 */
public class JFunctionsImpl implements JFunctions {

    private final Map<JSignature, JFunction> functionsBySig = new HashMap<>();
    private final Map<JSignature, JFunction> functionsBySigCache = new HashMap<>();
    private final Map<Integer, List<JSignature>> sigsByOpCount = new HashMap<>();
    private final Map<String, String> canonicalToAbsolute = new HashMap<>();
    private final Map<String, String> aliases = new HashMap<>();
    private JContext context;
    private JFunctions parent;

    public JFunctionsImpl(JContext context, JFunctions parent) {
        this.context = context;
        this.parent = parent;
    }

//    static JType[] convertArgumentTypesByIndex(JType[] argumentTypes, JConverter1 currentConverter, int index) {
//        JType[] argumentTypes2 = new JType[argumentTypes.length];
//        for (int j = 0; j < argumentTypes2.length; j++) {
//            if (index == j) {
//                argumentTypes2[j] = currentConverter.targetType();
//            } else {
//                argumentTypes2[j] = argumentTypes[j];
//            }
//        }
//        return argumentTypes2;
//    }
    public JContext getContext() {
        return context;
    }

    public void declare(JFunction fct) {
        JeepUtils.validateFunctionName(fct.getName());
        JSignature fsig = fct.getSignature();
        String canonicalName = getCanonicalName(fct.getName());
        JSignature sig = new JSignature(canonicalName, fsig.argTypes(), fsig.isVarArgs());
        functionsBySigCache.clear();
        functionsBySig.put(sig, fct);
        List<JSignature> list = sigsByOpCount.get(fct.getSignature().argsCount());
        if (list == null) {
            list = new ArrayList<>();
            sigsByOpCount.put(fct.getSignature().argsCount(), list);
        }
        list.add(sig);
        invalidateFunctionsCache(sig);
    }

    @Override
    public void declare(String name, String[] argTypes, String returnType, JInvoke function) {
        JSignature sig = JSignature.of(context.types(), name, argTypes);
        declare(sig.name(), sig.argTypes(), context.types().forName(returnType), sig.isVarArgs(), function);
    }

    @Override
    public JFunction declare(JSignature signature, JType returnType, JInvoke function) {
        return declare(signature.name(), signature.argTypes(), returnType, signature.isVarArgs(), function);
    }

    public JFunction declare(String name, JType[] args, JType returnType, boolean varArgs, JInvoke function) {
        JFunctionFromInvoke fct = new JFunctionFromInvoke(name, returnType, args, varArgs, function);
        declare(fct);
        return fct;
    }

    public Object evaluate(JCallerInfo callerInfo, String name, JEvaluable... args) {
        JType[] argVals = new JType[args.length];
        for (int i = 0; i < args.length; i++) {
            argVals[i] = args[i].type();
        }
        JSignature sig = JSignature.of(name, argVals);
        JFunction f = findFunctionMatchOrNull(sig, callerInfo);
        if (f != null) {
            //a=args.map(x->EvaluableNode(x))
            return f.invoke(new DefaultJInvokeContext(
                    context,
                    context.evaluators().newEvaluator(),
                    null, args,
                    name,callerInfo,null));
        }
        if (name == null || name.isEmpty()) {
            name = "<IMPLICIT>";
        }
        throw new NoSuchElementException(sig + " failed to execute" + ". JFunction " + name + " nor found for " + Arrays.asList(args));
    }

    public void declareAlias(String alias, String referenceOp, boolean varArgs, JType... argTypes) {
        String canonicalName = getCanonicalName(referenceOp);
        JSignature sig = new JSignature(canonicalName, argTypes, false);
        String aliasCanonicalName = getCanonicalName(alias);
//        JSignature aliasSig = new JSignature(aliasCanonicalName, argTypes);
        JeepUtils.validateFunctionName(alias);
        JFunction u = functionsBySig.get(sig);
        if (u == null
                && aliases.containsKey(canonicalName)) {
            throw new JParseException("Function not found : " + referenceOp);
        }
        if (aliases.containsKey(aliasCanonicalName)) {
            // TODO
        } else {
            aliases.put(aliasCanonicalName, canonicalName);
            invalidateFunctionsCache();
        }
        invalidateFunctionsCache(new JSignature(alias, argTypes, false));
    }

    @Override
    public void removeFunction(JNameSignature signature) {
        removeFunction(JSignature.of(context.types(), signature));

    }

    public void removeFunction(JSignature signature) {
        functionsBySigCache.clear();
        String canonicalName = getCanonicalName(signature.name());
        functionsBySig.remove(signature);
        List<JSignature> list = sigsByOpCount.get(signature.argsCount());
        if (list != null) {
            list.remove(signature);
        }
        for (Map.Entry<String, String> entry : new HashMap<String, String>(aliases).entrySet()) {
            if (entry.getValue().equals(signature.name())) {
                undeclareAlias(entry.getKey());
            }
        }
        invalidateFunctionsCache(signature.name());
    }

    @Override
    public JFunction[] findFunctions(String name, int callArgumentsCount) {
        Set<JFunction> applicables = new LinkedHashSet<>();
        if (callArgumentsCount == 0) {
            JVar v = getContext().vars().find(name);
            if (v != null) {
                applicables.add(new JFunctionFromVariable(v,getContext().types()));
            }
        }
        for (JFunction f : functionsBySigCache.values()) {
            JSignature sig = f.getSignature();
            if (sig.acceptArgsCount(callArgumentsCount)) {
                applicables.add(f);
            }
        }
        for (JResolver resolver : context.resolvers().getResolvers()) {
            JFunction[] jFunctions = resolver.resolveFunctionsByName(name, callArgumentsCount, context);
            if (jFunctions != null) {
                for (JFunction jFunction : jFunctions) {
                    if (jFunction != null) {
                        applicables.add(jFunction);
                    }
                }
            }
        }
        if (parent != null) {
            applicables.addAll(Arrays.asList(parent.findFunctions(name, callArgumentsCount)));
        }
        return applicables.toArray(new JFunction[0]);
    }

    public JFunction findFunctionMatchOrNull(JSignature signature, JCallerInfo callerInfo) {
        JFunction[] functions = findFunctions(signature.name(), signature.argsCount());
        JType[] tt = signature.argTypes();
        JTypePattern[] aa = new JTypePattern[tt.length];
        for (int i = 0; i < aa.length; i++) {
            aa[i] = JTypePattern.of(tt[i]);
        }
        return (JFunction) resolveBestMatch(callerInfo, functions, null, aa, null);
    }

    public JFunction findFunctionMatch(JSignature signature, JCallerInfo callerInfo) {
        JFunction f = findFunctionMatchOrNull(signature, callerInfo);
        if (f == null) {
            if (signature.name().isEmpty()) {
                throw new JParseException("Implicit Function not found " + signature);
            }
            throw new JParseException("Function not found " + signature);
        }
        return f;
    }

    @Override
    public void undeclareAlias(String alias, String[] argTypes) {
        JType[] jargTypes = context.types().forName(argTypes);
        undeclareAlias(alias);
    }

    public void undeclareAlias(String alias) {
        String canonicalName = getCanonicalName(alias);
        aliases.remove(canonicalName);
        invalidateFunctionsCache();
    }

    @Override
    public JFunction findFunctionMatchOrNull(JNameSignature signature, JCallerInfo callerInfo) {
        return findFunctionMatchOrNull(JSignature.of(context.types(), signature), callerInfo);
    }

    @Override
    public JFunction findFunctionMatchOrNull(String name, JCallerInfo callerInfo) {
        return findFunctionMatchOrNull(JSignature.of(name, new JType[0]), callerInfo);
    }

    @Override
    public JFunction findFunctionExact(JNameSignature signature) {
        return findFunctionExact(JSignature.of(context.types(), signature));
    }

    @Override
    public JFunction findFunctionExact(JSignature signature) {
        JFunction f = findFunctionExactOrNull(signature);
        if (f != null) {
            return f;
        }
        throw new JParseException("Function not found " + signature);
    }

    @Override
    public JFunction findFunctionExactOrNull(JSignature signature) {
        JFunction[] functions = findFunctions(signature.name(), signature.argsCount());
        List<JFunction> ok = new ArrayList<>();
        for (JFunction function : functions) {
            if (function.getSignature().equals(signature)) {
                ok.add(function);
            }
        }
        if (ok.isEmpty()) {
            return null;
        }
        if (ok.size() > 1) {
            throw new JMultipleInvokableMatchFound(signature.toString(), functions);
        }
        return ok.get(0);
    }

    public JFunction[] findFunctionsByName(String name) {
        return findFunctions(name, -1);
    }

    @Override
    public JInvokable resolveBestMatch(JCallerInfo callerInfo, JInvokable[] invokables, Function<JTypePattern, JConverter[]> convertersSupplier, JTypePattern[] argTypes, JTypePattern returnType) {
        JInvokableCost[] result = resolveMatches(true, invokables, convertersSupplier, argTypes, returnType);
        if (result.length == 0) {
            return null;
        }
        if (result.length > 1) {
            JInvokable[] error = new JInvokable[result.length];
            for (int i = 0; i < error.length; i++) {
                error[i] = result[i].getInvokable();
            }
            throw new JMultipleInvokableMatchFound(
                    JTypeUtils.sig(invokables[0].getSignature().name(), argTypes, false, true),
                    error);
        }
        return result[0].getInvokable();
    }

//    @Override
    @Override
    public JInvokableCost[] resolveMatches(boolean bestMatchOnly, JInvokable[] invokables, Function<JTypePattern, JConverter[]> convertersSupplier, JTypePattern[] argTypes, JTypePattern returnType) {
        if (invokables.length == 0) {
            return new JInvokableCost[0];
        }
        ArgsPossibility[] argsPossibilities = ArgsPossibility.allOf(argTypes, new MergedConvertersSupplier(context, convertersSupplier));
        class ArgsPossibilityApplication {

            ArgsPossibility possibility;
            JInvokable invocation;

            public ArgsPossibilityApplication(ArgsPossibility possibility, JInvokable invocation) {
                this.possibility = possibility;
                this.invocation = invocation;
            }

            @Override
            public String toString() {
                return invocation + " __AS__ " + possibility;
            }
        }

        Set<JInvokable> noDuplicatesInvokables = new LinkedHashSet<JInvokable>(Arrays.asList(invokables));
        List<JMethodObject<ArgsPossibilityApplication>> possibilities = new ArrayList<>();
        for (ArgsPossibility argsPossibility : argsPossibilities) {
            for (JInvokable possibleMethod : noDuplicatesInvokables) {
                ArgsPossibilityApplication a = new ArgsPossibilityApplication(argsPossibility, possibleMethod);
                JMethodObject<ArgsPossibilityApplication> mm = new JMethodObject<ArgsPossibilityApplication>(
                        possibleMethod.getSignature(),
                        null,
                        a,
                        a.possibility.weight()
                );
                JInvokeUtils.MCallCost2<ArgsPossibilityApplication> cost = JInvokeUtils.getMatchingMethodCost(mm, a.possibility.getConverted());
                if (cost != null) {
                    mm.setCostObject(cost);
                    possibilities.add(mm);
                }
            }
        }
        JMethodObject<ArgsPossibilityApplication>[] matchingMethods;
        if (bestMatchOnly) {
            matchingMethods = _getBestMatchingMethodsWithoutSort(possibilities);
        } else {
            possibilities.sort(Comparator.comparing(JMethodObject::getCostObject));
            matchingMethods = possibilities.toArray(new JMethodObject[0]);
        }
        List<JInvokableCost> all = new ArrayList<>();
        for (JMethodObject<ArgsPossibilityApplication> matchingMethod : matchingMethods) {
            JInvokable invocation = matchingMethod.getMethod().invocation;
            if (invocation.getSignature().argsCount() != argTypes.length) {
                //this is the case where we are using ellipse vararg (...)  arguments
                if (invocation.getSignature().acceptArgsCount(argTypes.length)) {
                    if (invocation instanceof JMethod) {
                        invocation = new JMethodWithVarArg((JMethod) invocation);
                    } else if (invocation instanceof JFunction) {
                        invocation = new JFunctionWithVarArg((JFunction) invocation);
                    } else if (invocation instanceof JConstructor) {
                        invocation = new JConstructorWithVarArg((JConstructor) invocation);
                    } else {
                        throw new JParseException("BUG :: Unexpected Behaviour....");
                    }
                } else {
                    throw new JParseException("BUG :: Unexpected Behaviour....");
                }
            }
            JInvokable invokable = invocation;
            all.add(new JInvokableCostImpl(
                    JInvokeUtils.convert(invokable,
                            matchingMethod.getMethod().possibility == null ? null
                            : matchingMethod.getMethod().possibility.getConverters(),
                            null),
                    matchingMethod.getCostObject()
            ));
        }
        return all.toArray(new JInvokableCost[0]);
    }

    private static <T> JMethodObject<T>[] _getBestMatchingMethodsWithoutSort(List<JMethodObject<T>> available) {
        List<JMethodObject<T>> allBests = new ArrayList<>();
        for (JMethodObject<T> a : available) {
            Comparable newCall = a.getCostObject();
            if (newCall != null) {
                a.setCostObject(newCall);
                if (allBests.isEmpty()) {
                    allBests.add(a);
                } else {
                    int cc = allBests.get(0).getCostObject().compareTo(newCall);
                    if (cc == 0) {
                        allBests.add(a);
                    } else if (cc > 0) {
                        allBests.clear();
                        allBests.add(a);
                    }
                }
            }
        }
        return (JMethodObject<T>[]) allBests.toArray(new JMethodObject<?>[0]);
    }

    public String getCanonicalName(String name) {
        if (!context.tokens().config().isCaseSensitive()) {
            String canonical = name.toUpperCase();
            String old = canonicalToAbsolute.get(canonical);
            if (old == null) {
                canonicalToAbsolute.put(canonical, name);
                return canonical;
            } else if (old.equals(name)) {
                //okkay
                return canonical;
            } else {
                throw new JParseException("Clash of names " + name + " <> " + old);
            }
        }
        return name;
    }

    public String getOriginalName(String name) {
        if (!context.tokens().config().isCaseSensitive()) {
            String o = canonicalToAbsolute.get(name);
            if (o != null) {
                return o;
            }
        }
        return name;
    }

    private Map<String, Set<String>> buildCache() {
        Map<String, Set<String>> cache = new HashMap<>();
        for (Map.Entry<String, String> entry : aliases.entrySet()) {
            String a = entry.getKey();
            Set<String> g1 = cache.get(a);
            String b = entry.getValue();
            Set<String> g2 = cache.get(b);
            LinkedHashSet<String> g = new LinkedHashSet<>();
            g.add(getOriginalName(a));
            g.add(getOriginalName(b));
            if (g1 != null) {
                g.addAll(g1);
            }
            if (g2 != null) {
                g.addAll(g2);
            }
            for (String s : g) {
                cache.put(s, g);
            }
        }
        return cache;
    }

    public void invalidateFunctionsCache(String name) {

    }

    public void invalidateFunctionsCache(JSignature signature) {
        if (signature.isVarArgs()) {
            invalidateFunctionsCache(signature.name());
        } else {
//            cacheFunctions.remove(signature);
        }
    }

    public void invalidateFunctionsCache() {
//        cacheFunctions.clear();
    }

    //    protected JMethod matchedMethodExact(JType type, String name, JType[] argTypes) {
//        Stack<JType> all = new Stack<>();
//        all.add(type);
//        Set<String> visited = new HashSet<>();
//        while (!all.isEmpty()) {
//            JType o = all.pop();
//            if (!visited.contains(o.name())) {
//                visited.add(o.name());
//                JMethod m = type.declaredMethodOrNull(JSignature.of(name, argTypes));
//                if (m != null) {
//                    return m;
//                }
//                JType s = o.superclass();
//                if (s != null) {
//                    all.push(s);
//                }
//            }
//        }
//        return null;
//    }
//    protected JMethod[] allMethodsByName(JType type, String name) {
//        Map<JSignature, JMethod> ret = new LinkedHashMap<>();
//        Stack<JType> all = new Stack<>();
//        all.add(type);
//        Set<String> visited = new HashSet<>();
//        while (!all.isEmpty()) {
//            JType o = all.pop();
//            if (!visited.contains(o.name())) {
//                visited.add(o.name());
//                for (JMethod jMethod : type.declaredMethods(name)) {
//                    JSignature sig = jMethod.signature().toNoVarArgs();
//                    if (!ret.containsKey(sig)) {
//                        ret.put(sig, jMethod);
//                    }
//                }
//                JType s = o.superclass();
//                if (s != null) {
//                    all.push(s);
//                }
//            }
//        }
//        return ret.values().toArray(new JMethod[0]);
//    }
}
