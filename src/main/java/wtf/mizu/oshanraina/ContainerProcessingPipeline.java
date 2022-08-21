package wtf.mizu.oshanraina;

import com.google.common.collect.ImmutableList;
import com.squareup.javapoet.JavaFile;
import wtf.mizu.oshanraina.intermediate.defaults.ContainerIntermediate;
import wtf.mizu.oshanraina.step.ContainerProcessingStage;
import wtf.mizu.oshanraina.intermediate.ContainerProcessingIntermediate;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.ArrayList;

/**
 * {@link ContainerProcessingPipeline} describes how a {@link Container} is
 * processed at compile-time.
 */
public class ContainerProcessingPipeline {

    /**
     * Returns a new {@link Builder} to build a custom pipeline.
     *
     * @param marker the annotation marker
     * @return the builder
     */
    public static Builder builder(Class<? extends Annotation> marker) {
        return new Builder(marker);
    }

    private final Class<? extends Annotation> marker;
    private final ContainerProcessingStage.Initialization initialization;
    private final ContainerProcessingStage.Intermediary intermediary;
    private final ContainerProcessingStage.Writing writing;

    public ContainerProcessingPipeline(
            Class<? extends Annotation> marker,
            ContainerProcessingStage.Initialization initialization,
            ContainerProcessingStage.Intermediary intermediary,
            ContainerProcessingStage.Writing writing
    ) {
        this.marker = marker;
        this.initialization = initialization;
        this.intermediary = intermediary;
        this.writing = writing;
    }

    public void tryProcessing(ProcessingEnvironment env, Element e) throws Exception {
        final var result = initialization.initialize(e);
        for (final var intermediate : intermediary.intermediates()) {
            intermediate.process(result.type(), e, result.containerName(),
                    env);
        }
        writing.write(result.containerName(), result.type());
    }

    public void tryProcessing(ProcessingEnvironment env,
                              Iterable<? extends Element> elements) throws Exception {
        final var errors = new ArrayList<Exception>();
        for (final var e : elements) {
            final var result = initialization.initialize(e);
            for (final var intermediate : intermediary.intermediates()) {
                intermediate.process(result.type(), e, result.containerName()
                        , env);
            }
            final var file = JavaFile.builder(
                    result.containerName().packageName(),
                    result.type().build()
            ).build();
            writing.write(result.containerName(), result.type());
        }
    }

    public void tryProcessing(ProcessingEnvironment processingEnv,
                              RoundEnvironment env) throws Exception {
        tryProcessing(processingEnv, env.getElementsAnnotatedWith(marker));
    }

    public static class Builder {
        private final Class<? extends Annotation> marker;
        private ContainerProcessingStage.Initialization initialization;
        private final ImmutableList.Builder<ContainerProcessingIntermediate> intermediates
                = new ImmutableList.Builder<ContainerProcessingIntermediate>();
        private ContainerProcessingStage.Writing writing;

        Builder(Class<? extends Annotation> marker) {
            this.marker = marker;
        }

        public Builder initialization(ContainerProcessingStage.Initialization initialization) {
            this.initialization = initialization;
            return this;
        }

        public Builder then(ContainerProcessingIntermediate intermediate) {
            intermediates.add(intermediate);
            return this;
        }

        public Builder writing(ContainerProcessingStage.Writing writing) {
            this.writing = writing;
            return this;
        }

        public ContainerProcessingPipeline create() {
            final var intermediates = this.intermediates.build();
            return new ContainerProcessingPipeline(
                    marker, initialization, () -> intermediates, writing
            );
        }
    }

}
