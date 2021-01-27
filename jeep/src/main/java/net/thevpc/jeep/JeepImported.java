package net.thevpc.jeep;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is an annotation that helps finding classes that are marked for
 * Import in Resolvers of Jeep.
 * IT IS NOT REQUIRED to add this annotation to import. Its just a hint for developers.
 *
 */
@Target({ElementType.TYPE ,ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.SOURCE)
public @interface JeepImported {
    ElementType[] value();
}
