package wtf.mizu.oshanraina.pipeline.step.defaults;

import org.jetbrains.annotations.NotNull;
import wtf.mizu.oshanraina.pipeline.step.ProcessingStage;
import wtf.mizu.oshanraina.pipeline.util.JavaPoetUtil;

import javax.lang.model.element.Element;

/**
 * An {@link ProcessingStage.Initialization} stage that creates a
 * {@link com.squareup.javapoet.TypeSpec.Builder TypeSpec builder} with the
 * given suffix as a file name.
 *
 * @author Shyrogan
 * @since 0.0.1
 */
public class JavaPoetSuffixedInitializationStage implements ProcessingStage.Initialization {
    private final @NotNull String suffix;

    /**
     * @param suffix the suffix to be used as a filename.
     */
    public JavaPoetSuffixedInitializationStage(final @NotNull String suffix) {
        this.suffix = suffix;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Result initialize(final @NotNull Element element) {
        return JavaPoetUtil.initializeWithPrefixOrSuffix(
                element,
                suffix,
                false
        );
    }
}
