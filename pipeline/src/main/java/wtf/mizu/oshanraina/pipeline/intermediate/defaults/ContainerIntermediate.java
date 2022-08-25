package wtf.mizu.oshanraina.pipeline.intermediate.defaults;

import com.squareup.javapoet.*;

import org.jetbrains.annotations.NotNull;
import wtf.mizu.oshanraina.api.Container;
import wtf.mizu.oshanraina.pipeline.intermediate.ProcessingIntermediate;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import java.util.Set;
import java.util.stream.Collectors;

import static javax.lang.model.element.ElementKind.CONSTRUCTOR;
import static javax.lang.model.element.Modifier.*;

/**
 * A {@link ProcessingIntermediate} implementation for the {@link Container}
 * interface.
 * <p>
 * If a wrapped element has a constructor, the generated {@link Container} will
 * also inherit this constructor.
 *
 * @author Shyrogan
 * @since 0.0.1
 */
public class ContainerIntermediate implements ProcessingIntermediate {
    /**
     * {@inheritDoc}
     */
    @Override
    public void process(
            final @NotNull TypeSpec.Builder type,
            final @NotNull Element element,
            final @NotNull ClassName containerName,
            final @NotNull ProcessingEnvironment env
    ) throws Exception {
        type.addJavadoc(
                "Generated from {@link $T} by the annotation processor",
                ClassName.get(element.asType())
        );

        type.addModifiers(PUBLIC, FINAL)
                .addSuperinterface(
                        ParameterizedTypeName.get(
                                ClassName.get(Container.class),
                                ClassName.get(element.asType())
                        )
                );

        element.getEnclosedElements().stream()
                .filter(e -> e.getKind() == CONSTRUCTOR
                        && !((ExecutableElement) e).getParameters().isEmpty()
                )
                .findFirst()
                .<Runnable>map(
                        e -> () -> containerWithConstructor(
                                type,
                                element,
                                (ExecutableElement) e
                        )
                )
                .orElse(
                        () -> containerWithoutConstructor(type, element)
                )
                .run();

        // the obj() method
        type.addMethod(
                MethodSpec.methodBuilder("obj")
                        .addModifiers(PUBLIC, FINAL)
                        .addJavadoc("{@inheritDoc}")
                        .returns(ClassName.get(element.asType()))
                        .addStatement("return obj")
                        .build()
        );
    }

    /**
     * TODO
     *
     * @param type        the type.
     * @param origin      the origin element.
     * @param constructor the constructor.
     */
    public void containerWithConstructor(
            final @NotNull TypeSpec.Builder type,
            final @NotNull Element origin,
            final @NotNull ExecutableElement constructor
    ) {
        final TypeName fieldType = ClassName.get(origin.asType());
        type.addField(
                FieldSpec.builder(fieldType, "obj")
                        .addModifiers(PRIVATE, FINAL)
                        .build()
        );

        final Set<ParameterSpec> parameters =
                constructor.getParameters().stream()
                        .map(e ->
                                ParameterSpec.builder(
                                        ClassName.get(e.asType()),
                                        e.getSimpleName().toString()
                                ).build()
                        )
                        .collect(Collectors.toSet());

        type.addMethod(
                MethodSpec.constructorBuilder()
                        .addModifiers(PUBLIC)
                        .addParameters(parameters)
                        .addCode("this.obj = new $T($L);", fieldType,
                                parameters.stream()
                                        .map(p -> p.name)
                                        .collect(Collectors.joining(", "))
                        )
                        .build()
        );
    }

    /**
     * TODO
     *
     * @param type   the type.
     * @param origin the origin element.
     */
    public void containerWithoutConstructor(
            final @NotNull TypeSpec.Builder type,
            final @NotNull Element origin
    ) {
        final TypeName fieldType = ClassName.get(origin.asType());

        type.addField(
                FieldSpec.builder(fieldType, "obj")
                        .addModifiers(PRIVATE, FINAL)
                        .initializer("new $T()", fieldType)
                        .build()
        );
    }
}
