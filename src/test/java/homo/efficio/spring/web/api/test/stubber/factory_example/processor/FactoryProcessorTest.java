package homo.efficio.spring.web.api.test.stubber.factory_example.processor;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author homo.efficio@gmail.com
 *         created on 2016. 11. 23.
 */
public class FactoryProcessorTest extends AbstractProcessorTest {

    private static final String PACKAGE_PATH = "src/test/java/homo/efficio/spring/web/api/test/stubber/factory_example/annotated";

    private int count;

    @Test
    public void process() throws IOException {
        System.out.println("==== " + count++ + " ====");
        System.out.println(getFileNamesIn(PACKAGE_PATH));

        process(FactoryProcessor.class,
                getFileNamesIn(PACKAGE_PATH),
                "test-stbber"
        );
//        File file = new File(PACKAGE_PATH, "CalzonePizza.java");
//        process(FactoryProcessor.class, Collections.singletonList(file.getPath()), "test-stubber");
    }

    private List<String> getFileNamesIn(String path) {

        List<String> results = new ArrayList<>();

        List<File> files = Arrays.asList(new File(path).listFiles());

        for (File f: files) {
            if (!f.isDirectory() && f.getName().toLowerCase().endsWith(".java"))
                results.add(f.getPath());
            else if (f.isDirectory() && !f.getName().startsWith("."))
                results.addAll(getFileNamesIn(f.getPath()));
        }

        return results;
    }
}
