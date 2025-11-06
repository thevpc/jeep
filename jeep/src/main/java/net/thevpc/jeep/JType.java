package net.thevpc.jeep;

import net.thevpc.jeep.core.JStaticObject;
import net.thevpc.jeep.impl.functions.JSignature;

public interface JType extends JDeclaration, JTypeOrVariable {
    JType[] getActualTypeArguments();
    /**
     * name with generic variables
     *
     * @return name with generic variables
     */
    String gname();

    boolean isRaw();

    JType parametrize(JType... parameters);

    void addAnnotation(JAnnotationInstance jAnnotationInstance);

    void setInterfaces(JType[] array);

    void setSuperType(JType tt);

    void addMethod(JMethod m);

    void addInterface(JType interfaceType);

    JConstructor addConstructor(JConstructor constructor, boolean redefine);


    JDeclaration getDeclaration();

    JTypeVariable[] getTypeParameters();

    JType getRawType();

    JAnnotationField[] getAnnotationFields();

    JAnnotationField getAnnotationField(String name);

    JStaticObject getStaticObject();

    String getRawName();
    String getSimpleRawName();

    String getName();

    String getVersion();

    JTypeName typeName();

    JTypes getTypes();

    String getSimpleName2();

    String getSimpleName();

    Object cast(Object o);

    JType boxed();

    boolean isPublic();

    boolean isStatic();

    boolean isNullable();

    boolean isPrimitive();

    boolean isAssignableFrom(JType other);

    boolean isAssignableFrom(JTypePattern other);

    boolean isInstance(Object instance);

    //<editor-fold desc="Array">

    boolean isArray();

    default JType toArray() {
        return toArray(1);
    }

    JType toArray(int count);

    //</editor-fold>

    JType getSuperType();

    JType firstCommonSuperType(JType other);

    JType toPrimitive();


    JField findMatchedField(String fieldName);


    //<editor-fold desc="Match">
//    @Deprecated
//    JMethod findMethodMatchOrNull(JSignature signature, JContext context);
//
//    @Deprecated
//    JConstructor findConstructorMatch(JSignature signature, JContext context);
//
//    @Deprecated
//    JConstructor findConstructorMatchOrNull(JSignature signature, JContext context);
//
//    @Deprecated
//    JMethod findMethodMatch(JSignature signature, JContext context);
    //</editor-fold>


    //<editor-fold desc="Declarations">

    JMethod getDeclaredMethod(String sig);

    JMethod getDeclaredMethod(JSignature sig);

    JMethod findDeclaredMethodOrNull(JSignature sig);

    //    JMethod declaredMethodOrNull(String methodName, JType... parameterTypes);
    JConstructor findDeclaredConstructorOrNull(String sig);

    JConstructor getDeclaredConstructor(String sig);

    JType[] getInterfaces();

    JConstructor getDeclaredConstructor(JSignature sig);

    JConstructor getDeclaredConstructor(JType... parameterTypes);

    JConstructor[] getPublicConstructors();

    JConstructor findDefaultConstructorOrNull();

    JConstructor getDefaultConstructor();

    JConstructor[] getDeclaredConstructors();

    JField getDeclaredField(String fieldName);

    JField[] getDeclaredFields();

    JField findDeclaredFieldOrNull(String fieldName);

    JField getPublicField(String name);


    JMethod[] getPublicMethods();

    JMethod[] getDeclaredMethods();


    JMethod[] getDeclaredMethods(String name);


    JField[] getDeclaredFieldsWithParents();

    JType[] getDeclaredInnerTypes();

    JType getDeclaredInnerType(String name);

    JType findDeclaredInnerTypeOrNull(String name);

    JMethod findDeclaredMethodOrNull(String sig);

    /**
     * all methods of the given names and that can be called with {@code callArgumentsCount}
     * arguments. This includes methods that has exactly {@code callArgumentsCount}
     * and methods that are vararg and who's arguments count is less or equals than
     * {@code callArgumentsCount}
     *
     * @param names              method names
     * @param callArgumentsCount arg count, when -1, all methods are returned!
     * @param includeParents     include super classes/interfaces
     * @return array of all applicable methods
     */
    JMethod[] getDeclaredMethods(String[] names, int callArgumentsCount, boolean includeParents);

    JMethod[] getDeclaredMethods(boolean includeParents);

    JType[] getParents();

    JConstructor findDeclaredConstructorOrNull(JSignature sig);
    //</editor-fold>

    Object getDefaultValue();

    JType getDeclaringType();

    String getPackageName();

    /**
     * exports are a list of "import" statement that should be processed each time
     * one imports this type with "**" end operator.
     * For instance
     * This code :
     * <pre>
     * class A{
     *     import java.util.*;
     *     import B.**;
     * }
     * class B{
     *     export java.reflect.*;
     * }
     * </pre>
     * is equivalent to
     * <pre>
     * class A{
     *     import java.util.*;
     *     import java.reflect.*;
     * }
     * class B{
     *     export java.reflect.*;
     * }
     * </pre>
     *
     * @return
     */
    String[] getExports();

    JTypeKind getKind();
    boolean isRawType();

    JType replaceParameter(String name, JType param);

    boolean isInterface();

    int arrayDimension();
    JType rootComponentType();
    JType componentType();
    JArray asArray(Object o);
    Object newArray(int... len);
    boolean isParametrizedType();
}
