package homo.efficio.spring.web.api.test.stubber.factory_example.annotated;

import homo.efficio.spring.web.api.test.stubber.factory_example.annotation.Factory;

/**
 * @author homo.efficio@gmail.com
 *         created on 2016. 11. 23.
 */

@Factory(
        id = "Calzone",
        type = Meal.class
)
public class CalzonePizza implements Meal {

    @Override
    public float getPrice() {
        return 8.5f;
    }
}
