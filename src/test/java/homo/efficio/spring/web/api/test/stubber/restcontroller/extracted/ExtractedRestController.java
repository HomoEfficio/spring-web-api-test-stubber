package homo.efficio.spring.web.api.test.stubber.restcontroller.extracted;

import com.sun.tools.javac.code.Scope;
import com.sun.tools.javac.code.Symbol;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
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

    private final TypeElement annotatedClassElement;
    private final String packageName;
    private final String simpleClassName;

    private final String[] reqMappedURLs;
    private final List<ExtractedRequestMappingMethod> requestMappingMethods;

    public ExtractedRestController(TypeElement annotatedClassElement) {
        this.annotatedClassElement = annotatedClassElement;

        this.reqMappedURLs = Optional.ofNullable(this.annotatedClassElement.getAnnotation(RequestMapping.class))
                                     .map(RequestMapping::value)
                                     .orElse(new String[] {});

        this.requestMappingMethods = getMethodsAnnotatatedWith(RequestMapping.class);

//        이건 컨트롤러 메서드에 method가 없는 것 필터링 할 때 응용하면 좋겠다
//        if (StringUtils.isEmpty(annotationValue)) {
//            throw new IllegalArgumentException(
//                    String.format("id() in @%s for class %s is null or empty! that's not allowed",
//                            RestController.class.getSimpleName(), annotatedClassElement.getQualifiedName().toString()));
//        }

//        if (StringUtils.isNoneEmpty(reqMappedURLs)) {
//            System.out.println("  reqMappedURLs: [" + String.join(", ", reqMappedURLs) + "]");
//        }


//        try {
//            Class<?> clazz = restControllerAnnotation.getClass();
//            qualifiedClassName = clazz.getCanonicalName();
//            simpleClassName = clazz.getSimpleName();
            String qualifiedClassName = annotatedClassElement.getQualifiedName().toString();
            int locationOfLastDot = qualifiedClassName.lastIndexOf('.');
            packageName = qualifiedClassName.substring(0, locationOfLastDot);
            simpleClassName = qualifiedClassName.substring(locationOfLastDot + 1);
//        } catch (MirroredTypeException mte) {
//            DeclaredType classTypeMirror = (DeclaredType) mte.getTypeMirror();
//            TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
//            qualifiedClassName = classTypeElement.getQualifiedName().toString();
//            simpleClassName = classTypeElement.getSimpleName().toString();
//        }
    }

    public String getSimpleClassName() {
        return simpleClassName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String[] getReqMappedURLs() {
        return reqMappedURLs;
    }

    public List<ExtractedRequestMappingMethod> getRequestMappingMethods() {
        return requestMappingMethods;
    }

    private List<ExtractedRequestMappingMethod> getMethodsAnnotatatedWith(Class<? extends Annotation> annotation) {

        if (TypeElement.class.isAssignableFrom(Symbol.ClassSymbol.class)) {

            Symbol.ClassSymbol annotatedClassSymbol = ((Symbol.ClassSymbol) annotatedClassElement);
            Scope members = annotatedClassSymbol.members();
            Iterable<Symbol> membersElements = members.getElements();
            Stream<Symbol> memberStream = StreamSupport.stream(membersElements.spliterator(), false);

            return memberStream.filter(m -> ElementKind.METHOD.equals(m.getKind()))
                    .map(m -> (Symbol.MethodSymbol)m)
                    .filter(m -> Objects.nonNull(m.getAnnotation(annotation)))
                    .map(this::getExtractedAnnotatedMethodWithAndFrom)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private ExtractedRequestMappingMethod getExtractedAnnotatedMethodWithAndFrom(Symbol.MethodSymbol i) {
        return new ExtractedRequestMappingMethod(i);
    }
}
