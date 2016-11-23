package homo.efficio.spring.web.api.test.stubber.annotation.factory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author homo.efficio@gmail.com
 *         created on 2016. 11. 23.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Factory {

    /**
     * The name of the factory
     */
    Class type();

    /**
     * The ID for determining which item should be instantiated
     */
    String id();
}
