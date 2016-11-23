package homo.efficio.spring.web.api.test.stubber.factory_example.generator;

import homo.efficio.spring.web.api.test.stubber.factory_example.processor.FactoryAnnotatedClass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hanmomhanda on 2016-11-23.
 */
public class SourceItemMap {

    private Map<String, List<FactoryAnnotatedClass>> map;

    public SourceItemMap() {
        map = new HashMap<>();
    }

    public List<FactoryAnnotatedClass> getAnnotatedClasses(String pkgName) {
        return map.get(pkgName);
    }
}
