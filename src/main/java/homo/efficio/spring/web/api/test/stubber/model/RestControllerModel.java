package homo.efficio.spring.web.api.test.stubber.model;

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
public class RestControllerModel {

    private final TypeElement annotatedClassElement;
    private final String packageName;
    private final String simpleClassName;

    private final String[] reqMappedURLs;
    private final List<RequestMappingMethodModel> requestMappingMethodModels;

    public RestControllerModel(TypeElement annotatedClassElement) {
        this.annotatedClassElement = annotatedClassElement;

        this.reqMappedURLs = Optional.ofNullable(this.annotatedClassElement.getAnnotation(RequestMapping.class))
                                     .map(RequestMapping::value)
                                     .orElse(new String[] {});

        this.requestMappingMethodModels = getMethodsAnnotatatedWith(RequestMapping.class);

        String qualifiedClassName = annotatedClassElement.getQualifiedName().toString();
        int locationOfLastDot = qualifiedClassName.lastIndexOf('.');
        this.packageName = qualifiedClassName.substring(0, locationOfLastDot);
        this.simpleClassName = qualifiedClassName.substring(locationOfLastDot + 1);
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

    public List<RequestMappingMethodModel> getRequestMappingMethodModels() {
        return requestMappingMethodModels;
    }

    private List<RequestMappingMethodModel> getMethodsAnnotatatedWith(Class<? extends Annotation> annotation) {

        if (TypeElement.class.isAssignableFrom(Symbol.ClassSymbol.class)) {

            Symbol.ClassSymbol annotatedClassSymbol = ((Symbol.ClassSymbol) annotatedClassElement);
            Scope members = annotatedClassSymbol.members();
            Iterable<Symbol> membersElements = members.getElements();
            Stream<Symbol> memberStream = StreamSupport.stream(membersElements.spliterator(), false);

            return memberStream.filter(m -> ElementKind.METHOD.equals(m.getKind()))
                    .map(m -> (Symbol.MethodSymbol)m)
                    .filter(m -> Objects.nonNull(m.getAnnotation(annotation)))
                    .map(this::getRequestMappingMethodFrom)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private RequestMappingMethodModel getRequestMappingMethodFrom(Symbol.MethodSymbol i) {
        return new RequestMappingMethodModel(i);
    }
}
