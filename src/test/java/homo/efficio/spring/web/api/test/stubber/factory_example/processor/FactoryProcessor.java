package homo.efficio.spring.web.api.test.stubber.factory_example.processor;

import com.google.auto.service.AutoService;
import homo.efficio.spring.web.api.test.stubber.factory_example.annotation.Factory;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author homo.efficio@gmail.com
 *         created on 2016. 11. 21.
 */
@AutoService(Process.class)
public class FactoryProcessor extends AbstractProcessor {

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;
    private Map<String, FactoryGroupedClasses> factoryClasses;

    public FactoryProcessor() {
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        factoryClasses = new LinkedHashMap<>();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotataions = new LinkedHashSet<String>();
        annotataions.add(Factory.class.getCanonicalName());
        return annotataions;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for (Element annotatedElement: roundEnv.getElementsAnnotatedWith(Factory.class)) {

            if (annotatedElement.getKind() != ElementKind.CLASS) {
                error(annotatedElement, "Only classes can be annotated with @%s",
                        Factory.class.getSimpleName());
                return true;
            }

            TypeElement typeElement = (TypeElement) annotatedElement;
        }

        return true;
    }

    private void error(Element e, String s, String simpleName) {
        messager.printMessage(
                Diagnostic.Kind.ERROR,
                String.format(s, simpleName),
                e
        );
    }
}
