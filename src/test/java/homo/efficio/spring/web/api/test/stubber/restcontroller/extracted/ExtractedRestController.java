package homo.efficio.spring.web.api.test.stubber.restcontroller.extracted;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import java.util.List;
import java.util.Optional;

/**
 * @author homo.efficio@gmail.com
 *         created on 2016-11-23.
 */
public class ExtractedRestController {

    private TypeElement annotatedClassElement;
    private String qualifiedClassName;
    private String simpleClassName;
    private String annotationValue;
    private String[] reqMappedURLs;

    public ExtractedRestController(TypeElement annotatedClassElement) {
        this.annotatedClassElement = annotatedClassElement;

        RestController restControllerAnnotation = annotatedClassElement.getAnnotation(RestController.class);
        this.annotationValue = restControllerAnnotation.value();

        this.reqMappedURLs = Optional.ofNullable(annotatedClassElement.getAnnotation(RequestMapping.class))
                                     .map(RequestMapping::value)
                                     .orElse(new String[] {});

//        if (StringUtils.isEmpty(annotationValue)) {
//            throw new IllegalArgumentException(
//                    String.format("id() in @%s for class %s is null or empty! that's not allowed",
//                            RestController.class.getSimpleName(), annotatedClassElement.getQualifiedName().toString()));
//        }

        if (StringUtils.isNoneEmpty(reqMappedURLs)) {
            System.out.println("  reqMappedURLs: [" + String.join(", ", reqMappedURLs) + "]");
        }


        try {
            Class<?> clazz = restControllerAnnotation.getClass();
            qualifiedClassName = clazz.getCanonicalName();
            simpleClassName = clazz.getSimpleName();
        } catch (MirroredTypeException mte) {
            DeclaredType classTypeMirror = (DeclaredType) mte.getTypeMirror();
            TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
            qualifiedClassName = classTypeElement.getQualifiedName().toString();
            simpleClassName = classTypeElement.getSimpleName().toString();
        }
    }

    public TypeElement getAnnotatedClassElement() {
        return annotatedClassElement;
    }

    public String[] getReqMappedURLs() {
        return reqMappedURLs;
    }
}
