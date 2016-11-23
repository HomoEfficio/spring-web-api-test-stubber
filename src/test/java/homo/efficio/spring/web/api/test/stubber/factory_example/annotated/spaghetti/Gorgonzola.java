package homo.efficio.spring.web.api.test.stubber.factory_example.annotated.spaghetti;

import homo.efficio.spring.web.api.test.stubber.factory_example.annotated.Meal;
import homo.efficio.spring.web.api.test.stubber.factory_example.annotation.Factory;

/**
 * @author homo.efficio@gmail.com
 *         created on 2016. 11. 23.
 */
public class Gorgonzola implements Meal {

    @Override
    public float getPrice() {
        return 7.5f;
    }
}
