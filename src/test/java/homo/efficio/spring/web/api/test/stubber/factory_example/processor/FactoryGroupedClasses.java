package homo.efficio.spring.web.api.test.stubber.factory_example.processor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author homo.efficio@gmail.com
 *         created on 2016. 11. 23.
 */
public class FactoryGroupedClasses {

    private String qualifiedClassName;

    private Map<String, FactoryAnnotatedClass> itemsMap =
            new LinkedHashMap<>();

    public FactoryGroupedClasses(String qualifiedClassName) {
        this.qualifiedClassName = qualifiedClassName;
    }

    public void add(FactoryAnnotatedClass toInsert) {

        FactoryAnnotatedClass existing = itemsMap.get(toInsert.getId());
        if (existing != null) {
            throw new IllegalArgumentException("Class already exists: " + existing);
        }

        itemsMap.put(toInsert.getId(), toInsert);
    }

//    public void generateCode(Elements elementUtils, Filer filer) throws IOException {
//	...
//    }
}
