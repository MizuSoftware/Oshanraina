package wtf.mizu.oshanraina.step.defaults;

import com.squareup.javapoet.ClassName;
import wtf.mizu.oshanraina.step.ContainerProcessingStage;

import javax.lang.model.element.Element;

import static com.squareup.javapoet.TypeSpec.classBuilder;
import static java.util.Arrays.copyOfRange;

/**
 * An {@link ContainerProcessingStage.Initialization Initialization} stage
 * that creates a {@link com.squareup.javapoet.TypeSpec.Builder TypeSpec}
 * with given suffix as a file name.
 */
public class JavaPoetContainerSuffixedInitializationStage implements ContainerProcessingStage.Initialization {

    private final String suffix;

    public JavaPoetContainerSuffixedInitializationStage(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public Result initialize(Element element) {
        final var originClassName = ClassName.get(element.asType());
        final var pkgPartsWithClass = originClassName.toString()
                .split("\\.");
        final var pkgParts = copyOfRange(pkgPartsWithClass, 0,
                pkgPartsWithClass.length - 1);
        final var pkg = String.join(".", pkgParts);
        final var containerClassName = ClassName.get(pkg,
                element.getSimpleName().toString() + suffix);

        final var typeBuilder = classBuilder(containerClassName);
        return new Result(
                typeBuilder,
                containerClassName
        );
    }
}
