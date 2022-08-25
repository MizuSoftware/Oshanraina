package wtf.mizu.oshanraina.pipeline.step.defaults;

import com.squareup.javapoet.*;

import org.jetbrains.annotations.NotNull;
import wtf.mizu.oshanraina.pipeline.step.ProcessingStage;

import javax.annotation.processing.ProcessingEnvironment;

/**
 * A {@link ProcessingStage.Writing} stage implementation that simply
 * uses <b>JavaPoet</b>'s
 * {@link JavaFile#writeTo(javax.annotation.processing.Filer)} method.
 *
 * @author Shyrogan
 * @since 0.0.1
 */
public class JavaPoetWritingStage implements ProcessingStage.Writing {
    private final @NotNull ProcessingEnvironment env;

    /**
     * @param env the processing environment.
     */
    public JavaPoetWritingStage(final @NotNull ProcessingEnvironment env) {
        this.env = env;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(
            final @NotNull ClassName containerName,
            final @NotNull TypeSpec.Builder type
    ) throws Exception {
        final JavaFile file = JavaFile.builder(
                containerName.packageName(),
                type.build()
        ).build();

        file.writeTo(env.getFiler());
    }
}
