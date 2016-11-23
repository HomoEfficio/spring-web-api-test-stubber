package homo.efficio.spring.web.api.test.stubber.restcontroller.processor;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.Set;

/**
 * @author homo.efficio@gmail.com
 *         created on 2016-11-23.
 */
public abstract class AbstractStubberProcessor extends AbstractProcessor {

    protected Types typeUtils;
    protected Elements elementUtils;
    protected Filer filer;
    protected Messager messager;

    public AbstractStubberProcessor() {
        super();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    @Override
    public abstract Set<String> getSupportedAnnotationTypes();

    @Override
    public Set<String> getSupportedOptions() {
        return super.getSupportedOptions();
    }
    @Override
    public abstract boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv);


    @Override
    public abstract Iterable<? extends Completion> getCompletions(Element element, AnnotationMirror annotation, ExecutableElement member, String userText);

    @Override
    protected synchronized boolean isInitialized() {
        return super.isInitialized();
    }

    protected void error(Element e, String s, String simpleName) {
        messager.printMessage(
                Diagnostic.Kind.ERROR,
                String.format(s, simpleName),
                e
        );
    }
}
