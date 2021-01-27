/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep;

import net.thevpc.jeep.impl.functions.JMultipleInvokableMatchFound;
import net.thevpc.jeep.impl.functions.JNameSignature;
import net.thevpc.jeep.impl.functions.JSignature;

import java.util.function.Function;

/**
 * @author thevpc
 */
public interface JFunctions {

    void declare(JFunction JFunction);

    void declare(String name, String[] args, String returnType, JInvoke function);

    JFunction declare(JSignature signature, JType returnType, JInvoke function);

    JFunction declare(String name, JType[] args, JType returnType, boolean varArgs, JInvoke function);

    //    @Override
    Object evaluate(JCallerInfo callerInfo, String name, JEvaluable... args);

    void declareAlias(String alias, String referenceOp, boolean varArgs, JType... operands);

    void removeFunction(JNameSignature signature);

    void removeFunction(JSignature signature);

    JFunction[] findFunctions(String name, int callArgumentsCount);

    JFunction findFunctionMatchOrNull(JSignature sig, JCallerInfo callerInfo);

    JFunction findFunctionMatch(JSignature sig, JCallerInfo callerInfo);

    void undeclareAlias(String alias, String[] operands);

    void undeclareAlias(String alias);

    JFunction findFunctionMatchOrNull(JNameSignature signature, JCallerInfo callerInfo);

    JFunction findFunctionMatchOrNull(String name, JCallerInfo callerInfo);

    JFunction findFunctionExact(JNameSignature signature);

    JFunction findFunctionExact(JSignature signature);

    JFunction findFunctionExactOrNull(JSignature signature);

    JFunction[] findFunctionsByName(String name);


    JInvokable resolveBestMatch(JCallerInfo callerInfo, JInvokable[] invokables, Function<JTypePattern, JConverter[]> convertersSupplier, JTypePattern[] argTypes, JTypePattern returnType) throws JMultipleInvokableMatchFound;

    JInvokableCost[] resolveMatches(boolean bestMatchOnly, JInvokable[] invokables, Function<JTypePattern, JConverter[]> convertersSupplier, JTypePattern[] argTypes, JTypePattern returnType);
}
