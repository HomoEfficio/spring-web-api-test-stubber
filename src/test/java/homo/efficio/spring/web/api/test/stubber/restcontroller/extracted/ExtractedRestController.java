package homo.efficio.spring.web.api.test.stubber.restcontroller.extracted;

import com.sun.tools.javac.code.Scope;
import com.sun.tools.javac.code.Symbol;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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

    private List<ExtractedMethod> mappedMethods;

    public ExtractedRestController(TypeElement annotatedClassElement) {
        this.annotatedClassElement = annotatedClassElement;

        RestController restControllerAnnotation = this.annotatedClassElement.getAnnotation(RestController.class);
        this.annotationValue = restControllerAnnotation.value();

        this.reqMappedURLs = Optional.ofNullable(this.annotatedClassElement.getAnnotation(RequestMapping.class))
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

    public List<Symbol.MethodSymbol> getMethodsAnnotatatedWith(Class<? extends Annotation> annotation) {

        if (TypeElement.class.isAssignableFrom(Symbol.ClassSymbol.class)) {

            Symbol.ClassSymbol annotatedClassSymbol = ((Symbol.ClassSymbol) annotatedClassElement);
            Scope members = annotatedClassSymbol.members();
            Iterable<Symbol> membersElements = members.getElements();
            Stream<Symbol> memberStream = StreamSupport.stream(membersElements.spliterator(), false);

            return memberStream.filter(m -> ElementKind.METHOD.equals(m.getKind()))
                    .map(m -> (Symbol.MethodSymbol)m)
                    .filter(m -> Objects.nonNull(m.getAnnotation(annotation)))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
