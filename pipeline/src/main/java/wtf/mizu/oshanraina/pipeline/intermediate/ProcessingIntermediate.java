package wtf.mizu.oshanraina.pipeline.intermediate;

import com.squareup.javapoet.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;

/**
 * TODO
 *
 * @author Shyrogan
 * @since 0.0.1
 */
public interface ProcessingIntermediate {
    /**
     * TODO
     *
     * @param type          the type.
     * @param element       the element.
     * @param containerName the name of the container.
     * @param env           the processing environment.
     * @throws Exception if something went wrong while processing.
     */
    void process(
            final @NotNull TypeSpec.Builder type,
            final @NotNull Element element,
            final @NotNull ClassName containerName,
            final @NotNull ProcessingEnvironment env
    ) throws Exception;
}
