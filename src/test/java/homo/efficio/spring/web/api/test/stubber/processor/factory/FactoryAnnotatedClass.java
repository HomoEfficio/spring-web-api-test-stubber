package homo.efficio.spring.web.api.test.stubber.processor.factory;

import homo.efficio.spring.web.api.test.stubber.annotation.factory.Factory;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;

/**
 * Created by homo.efficio@gmail.com on 2016. 11. 23..
 */
public class FactoryAnnotatedClass {

    private TypeElement annotatedClassElement;
    private String qualifiedSuperClassName;
    private String simpleTypeName;
    private String id;

    public FactoryAnnotatedClass(TypeElement annotatedClassElement) {
        this.annotatedClassElement = annotatedClassElement;

        Factory factoryAnnotation = annotatedClassElement.getAnnotation(Factory.class);
        this.id = factoryAnnotation.id();

        if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException(
                    String.format("id() in @%s for class %s is null or empty! that's not allowed",
                            Factory.class.getSimpleName(), annotatedClassElement.getQualifiedName().toString()));
        }

        try {
            Class<?> clazz = factoryAnnotation.type();
            qualifiedSuperClassName = clazz.getCanonicalName();
            simpleTypeName = clazz.getSimpleName();
        } catch (MirroredTypeException mte) {
            DeclaredType classTypeMirror = (DeclaredType) mte.getTypeMirror();
            TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
            qualifiedSuperClassName = classTypeElement.getQualifiedName().toString();
            simpleTypeName = classTypeElement.getSimpleName().toString();
        }
    }

    public TypeElement getAnnotatedClassElement() {
        return annotatedClassElement;
    }

    public String getQualifiedSuperClassName() {
        return qualifiedSuperClassName;
    }

    public String getSimpleTypeName() {
        return simpleTypeName;
    }

    public String getId() {
        return id;
    }
}
