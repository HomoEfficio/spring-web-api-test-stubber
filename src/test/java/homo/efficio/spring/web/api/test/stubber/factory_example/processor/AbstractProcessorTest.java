package homo.efficio.spring.web.api.test.stubber.factory_example.processor;

import com.sun.tools.javac.api.JavacTool;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;

import javax.annotation.processing.AbstractProcessor;
import javax.tools.JavaCompiler;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author homo.efficio@gmail.com
 *         created on 2016. 11. 23.
 */
public class AbstractProcessorTest {

    private final JavaCompiler compiler = JavacTool.create();

    protected static List<String> getFiles(String path) {
        List<String> classes = new ArrayList<String>();
        for (File file : new File(path).listFiles()) {
            if (file.getName().endsWith(".java")) {
                classes.add(file.getPath());
            } else if (file.isDirectory() && !file.getName().startsWith(".")) {
                classes.addAll(getFiles(file.getAbsolutePath()));
            }
        }
        return classes;
    }

    protected void process(Class<? extends AbstractProcessor> processorClass, List<String> classes, String target) throws IOException {
        File out = new File("build/target/" + target);
        FileUtils.deleteQuietly(out);
        if (!out.mkdirs()) {
            Assert.fail("Creation of " + out.getPath() + " failed");
        }
        compile(processorClass, classes, target);
    }

    protected void compile(Class<? extends AbstractProcessor> processorClass, List<String> classes, String target) throws IOException {
        List<String> options = new ArrayList<String>(classes.size() + 3);
        options.add("-s");
        options.add("build/target/" + target);
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
            System.err.println(compiler.getClass().getName());
            Assert.fail("Compilation Failed:\n " + new String(err.toByteArray(), "UTF-8"));
        }
    }

    protected Collection<String> getAPTOptions() {
        return Collections.emptyList();
    }
}
