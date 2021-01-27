/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep;

/**
 * @author thevpc
 */
public interface JResolvers {

    JResolvers importPlatform();

    JResolvers importType(String type);

    JResolvers importType(Class type);

    JResolvers importType(JType type);

    JResolvers importMethods(String type);

    JResolvers importMethods(Class type);

    JResolvers importMethods(JType type);

    JResolvers importFields(String type);

    JResolvers importFields(Class type);

    JResolvers importFields(JType type);

    JResolvers addResolver(JResolver resolver);

    JResolvers removeResolver(JResolver resolver);

    JResolver[] getResolvers();

    JConverter[] getConverters(JTypePattern operandType);

}
