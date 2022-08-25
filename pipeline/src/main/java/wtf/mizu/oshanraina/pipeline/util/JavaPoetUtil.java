package wtf.mizu.oshanraina.pipeline.util;

import com.squareup.javapoet.ClassName;
import org.jetbrains.annotations.NotNull;
import wtf.mizu.oshanraina.pipeline.step.ProcessingStage;

import javax.lang.model.element.Element;

import static com.squareup.javapoet.TypeSpec.classBuilder;
import static java.util.Arrays.copyOfRange;

/**
 * @author lambdagg
 * @see wtf.mizu.oshanraina.pipeline.step.defaults.JavaPoetPrefixedInitializationStage
 * @see wtf.mizu.oshanraina.pipeline.step.defaults.JavaPoetSuffixedInitializationStage
 * @since 0.0.1
 */
public class JavaPoetUtil {
    private JavaPoetUtil() {
    }

    /**
     * Utility method used by JavaPoet default initialization stages.
     *
     * @param element        the element.
     * @param prefixOrSuffix the prefix or suffix.
     * @param prefix         whether to prefix or suffix the container class name.
     * @return the initialization result.
     * @see wtf.mizu.oshanraina.pipeline.step.defaults.JavaPoetPrefixedInitializationStage
     * @see wtf.mizu.oshanraina.pipeline.step.defaults.JavaPoetSuffixedInitializationStage
     */
    public static @NotNull ProcessingStage.Initialization.Result initializeWithPrefixOrSuffix(
            final @NotNull Element element,
            final @NotNull String prefixOrSuffix,
            final boolean prefix
    ) {
        final String[] pkgPartsWithClass = ClassName.get(element.asType())
                .toString()
                .split("\\.");

        final ClassName containerClassName = ClassName.get(
                String.join(  // the package
                        ".",
                        copyOfRange(
                                pkgPartsWithClass,
                                0,
                                pkgPartsWithClass.length - 1
                        )
                ),
                (prefix ? prefixOrSuffix : "")
                        + element.getSimpleName().toString()
                        + (!prefix ? prefixOrSuffix : "")
        );

        return new ProcessingStage.Initialization.Result(
                classBuilder(containerClassName),  // the type builder
                containerClassName
        );
    }
}
