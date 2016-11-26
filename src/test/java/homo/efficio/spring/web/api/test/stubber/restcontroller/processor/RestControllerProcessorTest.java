package homo.efficio.spring.web.api.test.stubber.restcontroller.processor;

import homo.efficio.spring.web.api.test.stubber.processor.RestControllerProcessor;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.processing.AbstractProcessor;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author homo.efficio@gmail.com
 *         created on 2016. 11. 23.
 */
public class RestControllerProcessorTest {

    private final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    private Collection<String> getAPTOptions() {
        return Collections.emptyList();
    }

    private static final String SRC_PACKAGE_PATH = "src/test/java/homo/efficio/spring/web/api/test/stubber/restcontroller/annotated";

    @Test
    public void process() throws IOException {
        compile(RestControllerProcessor.class, getFileNamesIn(SRC_PACKAGE_PATH));
    }

    private List<String> getFileNamesIn(String path) {

        List<String> results = new ArrayList<>();
        File[] files = new File(path).listFiles();

        if (Objects.nonNull(files) && files.length > 0) {
            List<File> fileList = Arrays.asList(files);
            for (File f: fileList) {
                if (!f.isDirectory() && f.getName().toLowerCase().endsWith(".java"))
                    results.add(f.getPath());
                else if (f.isDirectory() && !f.getName().startsWith("."))
                    results.addAll(getFileNamesIn(f.getPath()));
            }
        }

        return results;
    }

    private void compile(Class<? extends AbstractProcessor> processorClass, List<String> classes) throws IOException {
        List<String> options = new ArrayList<String>();
//        Filer를 사용하지 않고 JavaPoet가 직접 파일을 생성하므로 아래 -s 옵션 불필요
//        options.add("-s");
//        options.add("src/test/java/" + target);
        options.add("-proc:only");
        options.add("-processor");
        options.add(processorClass.getName());
        options.add("-sourcepath");
        options.add("src/test/java");
        options.addAll(getAPTOptions());
        options.addAll(classes);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        int compilationResult = compiler.run(null, out, err, options.toArray(new String[options.size()]));

//        Processor.elementCache.clear();
        if (compilationResult != 0) {
            Assert.fail("Compilation Failed:\n " + new String(err.toByteArray(), "UTF-8"));
        }
    }

}
