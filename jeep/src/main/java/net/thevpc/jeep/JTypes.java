package net.thevpc.jeep;

//import java.lang.reflect.Type;

public interface JTypes {

    ClassLoader hostClassLoader();

    JTypesResolver[] resolvers();

    void addResolver(JTypesResolver r);

    JTypesLoader[] loaders();

    void addLoader(JTypesLoader loader);

    void removeLoader(JTypesLoader loader);

    JTypeName parseName(String name);

    JType[] forName(String[] name);

    JType forName(String name, JDeclaration enclosingDeclaration);

    JType forName(String name);

    JType forNameOrNull(String name);

    JType forNameOrNull(String name, JDeclaration enclosingDeclaration);

//    JType[] forName(Type[] clazz);
//
//    JType[] forName(Type[] names, JDeclaration declaration);

//    JType[] forNameOrVar(Type[] clazz, JDeclaration declaration);


//    JType forName(Type clazz);

//    JType forName(Type clazz, JDeclaration declaration);

//    JType forNameOrVar(Type clazz, JDeclaration declaration);

    boolean isNull(Object object);

    JType typeOf(Object object);

    String typenameOf(Object object);

    JType declareType(String fullName, JTypeKind kind, boolean redefine);

    JType forName(JTypeName name);
    JType forNameOrNull(JTypeName name);

    void addAlias(String name, JType type);

    JTypes parent();

    boolean isAbstractType(JType c);
}
