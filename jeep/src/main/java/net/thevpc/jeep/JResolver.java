package net.thevpc.jeep;

public interface JResolver {

    default JVar resolveVariable(String name, JContext context){
        return null;
    }

    default JFunction resolveFunction(String name, JTypePattern[] argTypes, JContext context){
        return null;
    }
    default JFunction[] resolveFunctionsByName(String name, int argsCount, JContext context){
        return null;
    }

    default Object implicitConvertLiteral(Object literal, JContext context){
        return null;
    }


    default JConverter[] resolveImplicitConverters(JTypePattern type){
        return null;
    }

}
