package homo.efficio.spring.web.api.test.stubber.factory_example.annotated;

import homo.efficio.spring.web.api.test.stubber.factory_example.annotation.Factory;

/**
 * @author omwomw@sk.com
 *         created on 2016. 11. 21.
 */

@Factory(
        id = "Margherita",
        type = Meal.class
)
public class MargheritaPizza implements Meal {

    @Override
    public float getPrice() {
        return 6.0f;
    }
}
