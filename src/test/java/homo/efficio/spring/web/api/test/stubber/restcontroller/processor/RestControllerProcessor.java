package homo.efficio.spring.web.api.test.stubber.restcontroller.processor;

import homo.efficio.spring.web.api.test.stubber.generator.SpringBootRestControllerTesterStubGenerator;
import homo.efficio.spring.web.api.test.stubber.restcontroller.extracted.RestControllerModel;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.processing.Completion;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author homo.efficio@gmail.com
 *         created on 2016-11-23.
 */
public class RestControllerProcessor extends AbstractStubberProcessor {

    @Override
    public Set<String> getSupportedAnnotationTypes() {

        Set<String> annotataions = new LinkedHashSet<String>();
        annotataions.add(org.springframework.web.bind.annotation.RestController.class.getCanonicalName());
        return annotataions;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        Set<? extends Element> elementsAnnotatedWithRestController = roundEnv.getElementsAnnotatedWith(RestController.class);
        for (Element restControllerAnnotatedEleemnt: elementsAnnotatedWithRestController) {
            if (restControllerAnnotatedEleemnt.getKind() != ElementKind.CLASS) {
                error(restControllerAnnotatedEleemnt, "@%s can be applied only to class",
                        RestController.class.getSimpleName());
                return true;
            }
            // @RequestMapping 붙은 클래스
            TypeElement restControllerTypeElement = (TypeElement) restControllerAnnotatedEleemnt;

            // 분석용 래퍼 클래스
            RestControllerModel restControllerModel = new RestControllerModel(restControllerTypeElement);

            generateStubFileFor(restControllerModel);
        }

        return false;
    }

    private void generateStubFileFor(RestControllerModel annotatedClass) {

        SpringBootRestControllerTesterStubGenerator springBootRestControllerTesterStubGenerator = new SpringBootRestControllerTesterStubGenerator(annotatedClass);

        try {
            springBootRestControllerTesterStubGenerator.generate();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public Iterable<? extends Completion> getCompletions(Element element, AnnotationMirror annotation, ExecutableElement member, String userText) {
        return Collections.emptyList();
    }
}
