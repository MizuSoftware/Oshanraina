package wtf.mizu.oshanraina.intermediate.defaults;

import com.squareup.javapoet.*;
import wtf.mizu.oshanraina.Container;
import wtf.mizu.oshanraina.intermediate.ContainerProcessingIntermediate;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import java.util.stream.Collectors;

import static javax.lang.model.element.ElementKind.CONSTRUCTOR;
import static javax.lang.model.element.Modifier.*;

/**
 * A {@link ContainerProcessingIntermediate intermediate} that implements the
 * {@link Container} interface.
 *
 * <p>
 *     If a wrapped element has a constructor, the {@link Container}
 *     generated will also inherit this constructor allowing them to have
 *     constructors.
 * </p>
 */
public class ContainerIntermediate implements ContainerProcessingIntermediate {

    @Override
    public void process(TypeSpec.Builder type, Element element,
                        ClassName containerName, ProcessingEnvironment env) throws Exception {
        type.addJavadoc("Generated from {@link $T} by the " +
                "annotation-processor", ClassName.get(element.asType()));
        type.addModifiers(PUBLIC, FINAL).addSuperinterface(ParameterizedTypeName.get(
                ClassName.get(Container.class),
                ClassName.get(element.asType()))
        );
        element.getEnclosedElements().stream()
                .filter(e -> e.getKind() == CONSTRUCTOR)
                .findFirst()
                .map(e -> (ExecutableElement)e)
                .filter(e -> !e.getParameters().isEmpty())
                .ifPresentOrElse(
                        e -> containerWithConstructor(type, element, e),
                        () -> containerWithoutConstructor(type, element)
                );

        // the obj() method
        type.addMethod(MethodSpec.methodBuilder("obj")
                .addModifiers(PUBLIC, FINAL)
                .addJavadoc("{@inheritDoc}")
                .returns(ClassName.get(element.asType()))
                .addStatement("return obj")
                .build());
    }

    public void containerWithConstructor(TypeSpec.Builder type,
                                         Element origin,
                                         ExecutableElement constructor) {
        final var fieldType = ClassName.get(origin.asType());
        type.addField(FieldSpec.builder(fieldType, "obj", PRIVATE, FINAL)
                .build());

        final var parameters = constructor.getParameters().stream()
                .map(e -> ParameterSpec.builder(ClassName.get(e.asType()), e.getSimpleName().toString())
                        .build())
                .collect(Collectors.toSet());
        type.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(PUBLIC)
                .addParameters(parameters)
                .addCode("this.obj = new $T($L);", fieldType,
                        parameters.stream()
                                .map(p -> p.name)
                                .collect(Collectors.joining(", "))
                ).build());
    }

    public void containerWithoutConstructor(TypeSpec.Builder type,
                                            Element origin) {
        final var fieldType = ClassName.get(origin.asType());
        type.addField(FieldSpec.builder(fieldType, "obj", PRIVATE, FINAL)
                .initializer("new $T()", fieldType)
                .build());
    }

}
