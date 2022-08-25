package wtf.mizu.oshanraina.pipeline;

import com.google.common.collect.ImmutableList;
import com.squareup.javapoet.JavaFile;
import org.jetbrains.annotations.NotNull;
import wtf.mizu.oshanraina.api.Container;
import wtf.mizu.oshanraina.pipeline.intermediate.ProcessingIntermediate;
import wtf.mizu.oshanraina.pipeline.step.ProcessingStage;

import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Describes how a {@link Container} is processed at compile-time.
 *
 * @author Shyrogan
 * @since 0.0.1
 */
public class ProcessingPipeline {
    /**
     * Returns a new {@link ProcessingPipeline.Builder}.
     *
     * @param annotationMarker the annotation marker.
     * @return the newly created builder.
     */
    public static @NotNull Builder builder(
            final @NotNull Class<? extends Annotation> annotationMarker
    ) {
        return new Builder(annotationMarker);
    }

    private final Class<? extends Annotation> annotationMarker;

    private final ProcessingStage.Initialization initializationStage;

    private final ProcessingStage.Intermediary intermediaryStage;

    private final ProcessingStage.Writing writingStage;

    /**
     * @param annotationMarker    the annotation marker.
     * @param initializationStage the initializationStage stage.
     * @param intermediaryStage   the intermediaryStage stage.
     * @param writingStage        the writingStage stage.
     */
    public ProcessingPipeline(
            final @NotNull Class<? extends Annotation> annotationMarker,
            final @NotNull ProcessingStage.Initialization initializationStage,
            final @NotNull ProcessingStage.Intermediary intermediaryStage,
            final @NotNull ProcessingStage.Writing writingStage
    ) {
        this.annotationMarker = annotationMarker;
        this.initializationStage = initializationStage;
        this.intermediaryStage = intermediaryStage;
        this.writingStage = writingStage;
    }

    /**
     * TODO
     *
     * @param env     the processing environment.
     * @param element the target element.
     * @throws Exception if something went wrong while processing.
     */
    public void tryProcessing(
            final @NotNull ProcessingEnvironment env,
            final @NotNull Element element
    ) throws Exception {
        final ProcessingStage.Initialization.Result result =
                initializationStage.initialize(element);

        for (
                final ProcessingIntermediate intermediate
                : intermediaryStage.intermediates()
        ) {
            intermediate.process(
                    result.type,
                    element,
                    result.containerName,
                    env
            );
        }

        writingStage.write(result.containerName, result.type);
    }

    /**
     * TODO
     *
     * @param env      the processing environment.
     * @param elements the target elements.
     * @throws Exception if something went wrong while processing.
     */
    public void tryProcessing(
            final @NotNull ProcessingEnvironment env,
            final @NotNull Iterable<? extends Element> elements
    ) throws Exception {
        final List<Exception> errors = new ArrayList<>();

        for (final Element element : elements) {
            final ProcessingStage.Initialization.Result result =
                    initializationStage.initialize(element);

            for (
                    final ProcessingIntermediate intermediate
                    : intermediaryStage.intermediates()
            ) {
                intermediate.process(
                        result.type,
                        element,
                        result.containerName,
                        env
                );
            }

            final JavaFile file =
                    JavaFile.builder(
                            result.containerName.packageName(),
                            result.type.build()
                    ).build();

            writingStage.write(result.containerName, result.type);
        }
    }

    /**
     * TODO
     *
     * @param processingEnv the processing environment.
     * @param env           the round environment.
     * @throws Exception if something went wrong while processing.
     */
    public void tryProcessing(
            final @NotNull ProcessingEnvironment processingEnv,
            final @NotNull RoundEnvironment env
    ) throws Exception {
        tryProcessing(
                processingEnv,
                env.getElementsAnnotatedWith(annotationMarker)
        );
    }

    public static class Builder {
        private final @NotNull Class<? extends Annotation> annotationMarker;

        private ProcessingStage.Initialization initializationStage;

        private final ImmutableList.Builder<ProcessingIntermediate> intermediates
                = new ImmutableList.Builder<>();

        private ProcessingStage.Writing writingStage;

        private Builder(
                final @NotNull Class<? extends Annotation> annotationMarker
        ) {
            this.annotationMarker = annotationMarker;
        }

        /**
         * Sets the `initializationStage` field.
         *
         * @param initializationStage the new initialization stage.
         * @return this instance.
         */
        public @NotNull Builder initialization(
                final @NotNull ProcessingStage.Initialization initializationStage
        ) {
            this.initializationStage = initializationStage;
            return this;
        }

        /**
         * Adds a new {@link ProcessingIntermediate}.
         *
         * @param intermediate the new initialization stage.
         * @return this instance.
         */
        public @NotNull Builder then(
                final @NotNull ProcessingIntermediate intermediate
        ) {
            intermediates.add(intermediate);
            return this;
        }

        /**
         * Sets the `writingStage` field.
         *
         * @param writingStage the new writing stage.
         * @return this instance.
         */
        public @NotNull Builder writing(
                final @NotNull ProcessingStage.Writing writingStage
        ) {
            this.writingStage = writingStage;
            return this;
        }

        /**
         * Creates the {@link ProcessingPipeline} according to this
         * builder.
         *
         * @return the newly created {@link ProcessingPipeline}.
         */
        public @NotNull ProcessingPipeline create() {
            if (initializationStage == null) {
                throw new RuntimeException("Initialization stage was null.");
            }

            final ImmutableList<ProcessingIntermediate> intermediates =
                    this.intermediates.build();

            if (intermediates.isEmpty()) {
                throw new RuntimeException("No intermediate given.");
            }

            if (writingStage == null) {
                throw new RuntimeException("Writing stage was null.");
            }

            return new ProcessingPipeline(
                    annotationMarker,
                    initializationStage,
                    () -> intermediates,
                    writingStage
            );
        }
    }

}
