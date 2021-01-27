/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep;

/**
 * @author thevpc
 */
public interface JVars {

    JVar declareConst(String name, Class type, Object value);

    JVar declareConst(String name, JType type, Object value);

    JVar declareConst(String name, Object value);

    JVar declareVar(String name, Class type, Class undefinedType, Object value);

    JVar declareVar(String name, JType type, JType undefinedType, Object value);

    JVar declareVar(String name, Class type, Object value);

    JVar declareVar(String name, JType type, Object value);

    JVar declareVar(JVar def);

    JVars declareAlias(String alias, String name);

    JVars undeclareVar(String name);

    JVars undeclareAlias(String alias);

    JVar find(String var);

//    JNode getName(String varName);

    JVar get(String var);

    void setValue(String varName, Object value, JInvokeContext invokeContext);

    Object getValue(String varName, JInvokeContext invokeContext);

//    JNode findNode(String name);

}
