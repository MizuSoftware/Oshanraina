package wtf.mizu.oshanraina.pipeline.step.defaults;

import org.jetbrains.annotations.NotNull;
import wtf.mizu.oshanraina.pipeline.step.ProcessingStage;
import wtf.mizu.oshanraina.pipeline.util.JavaPoetUtil;

import javax.lang.model.element.Element;

/**
 * An {@link ProcessingStage.Initialization} stage that creates a
 * {@link com.squareup.javapoet.TypeSpec.Builder TypeSpec builder} with the
 * given prefix as a file name.
 *
 * @author Shyrogan
 * @since 0.0.1
 */
public class JavaPoetPrefixedInitializationStage
        implements ProcessingStage.Initialization {
    private final @NotNull String prefix;

    /**
     * @param prefix the prefix to be used as a filename.
     */
    public JavaPoetPrefixedInitializationStage(final @NotNull String prefix) {
        this.prefix = prefix;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Result initialize(final @NotNull Element element) {
        return JavaPoetUtil.initializeWithPrefixOrSuffix(
                element,
                prefix,
                true
        );
    }
}
