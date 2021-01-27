package net.thevpc.jeep;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public interface JTypesResolver {
    /**
     *
     * Effective signature (or null if not changed) of methods with
     * erasure equivalent arguments :
     * <pre>
     * method(List&lt;Integer&gt;);
     * method(List&lt;String&gt;);
     * </pre>
     * This would be implemented as :
     * <pre>
     * {@literal @}Signature("method(List&lt;Integer&gt;)")
     * method$1(List&lt;Integer&gt;);
     * {@literal @}Signature("method(List&lt;String&gt;)")
     * method$2(List&lt;String&gt;);
     * </pre>
     * though $1 and $2 suffixes are not mandatory it is a good practice
     * to help read well and have a general expectation of method signature.
     * @param method java method
     * @return effective signature, may rename method or parameters
     */
    default String resolveMethodSignature(Method method){
        return null;
    }

    default String resolveConstructorSignature(Constructor method){
        return null;
    }

    /**
     * type exports is simple way to export a list of import statements.
     * The following code will expand every "import MyClass" to
     * "import MyClass ; import mypackage.*" .
     *  <pre>
     *      {@literal @}Exports({"mypackage.*"})
     *      public class MyClass{
     *
     *      }
     *  </pre>
     * @param clazz clazz to resolve from
     * @return export list
     */
    default String[] resolveTypeExports(Class clazz){
        return null;
    }
}
