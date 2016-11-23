package homo.efficio.spring.web.api.test.stubber.factory_example.processor;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @author homo.efficio@gmail.com
 *         created on 2016. 11. 23.
 */
public class FactoryProcessorTest extends AbstractProcessorTest {

    private static final String PACKAGE_PATH = "src/test/java/homo/efficio/spring/web/api/test/stubber/factory_example/annotated";

    private static final List<String> CLASSES = getFiles(PACKAGE_PATH);

    @Test
    public void process() throws IOException {
        System.out.println(CLASSES);
        File file = new File(PACKAGE_PATH, "CalzonePizza.java");
        process(FactoryProcessor.class, Collections.singletonList(file.getPath()),"test-stubber");
    }
}
