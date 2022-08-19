package wtf.mizu.oshanraina.step;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;
import org.jetbrains.annotations.Unmodifiable;
import wtf.mizu.oshanraina.Container;
import wtf.mizu.oshanraina.step.defaults.JavaPoetContainerPrefixednitializationStage;
import wtf.mizu.oshanraina.step.defaults.JavaPoetContainerWritingStage;
import wtf.mizu.oshanraina.step.defaults.JavaPoetContainerSuffixedInitializationStage;
import wtf.mizu.oshanraina.intermediate.ContainerProcessingIntermediate;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import java.util.List;
import java.util.Optional;

/**
 * A {@link ContainerProcessingStage} represents a <b>single and unique</b> stage
 * of processing for any {@link Container}.
 *
 * <p>
 * There are 3 stages required to instantiate a {@link Container}:
 * <ol>
 *     <li>
 *         {@link Initialization} responsible of setting up common settings
 *         to ensure a correct container processing.
 *     </li>
 *     <li>
 *         {@link Intermediary} generally responsible of processing the
 *         data contained by a container.
 *     </li>
 *     <li>
 *         {@link Writing} responsible of writing the content of the
 *         processed container.
 *     </li>
 * </ol>
 *
 * <p>
 *     Unless all of these steps are provided, the {@link }
 * </p>
 */
public interface ContainerProcessingStage {

    interface Initialization extends ContainerProcessingStage {
        static Initialization withPrefix(String prefix) {
            return new JavaPoetContainerPrefixednitializationStage(prefix);
        }

        static Initialization withSuffix(String suffix) {
            return new JavaPoetContainerSuffixedInitializationStage(suffix);
        }

        /**
         * Initializes the processing of a future {@link Container} and
         * returns a {@link Result} that wraps the required data to continue.
         *
         * @param element the element
         * @return the {@link Result} that wraps the required data
         */
        Result initialize(Element element);

        /**
         * {@link Result} is a simplistic record that wraps all the data
         * required to process a given {@link Container}.
         *
         * @param type          the type spec
         * @param containerName the container's class name
         */
        record Result(
                TypeSpec.Builder type,
                ClassName containerName
        ) {
        }
    }

    @FunctionalInterface
    interface Intermediary extends ContainerProcessingStage {
        /**
         * Returns the {@link ContainerProcessingIntermediate processing
         * intermediates} used during this intermediate stage.
         *
         * @return the {@link ContainerProcessingIntermediate intermediates}
         * @see ContainerProcessingIntermediate
         */
        @Unmodifiable List<ContainerProcessingIntermediate> intermediates();
    }

    interface Writing {
        /**
         * Syntax sugar to the {@link JavaPoetContainerWritingStage}.
         *
         * @param processingEnvironment the processing environment
         * @return a new {@link JavaPoetContainerWritingStage}
         */
        static Writing usingJavaPoet(
                ProcessingEnvironment processingEnvironment
        ) {
            return new JavaPoetContainerWritingStage(processingEnvironment);
        }

        /**
         * Called once every {@link ContainerProcessingIntermediate} has been
         * applied to the future {@link Container}.
         *
         * <p>
         * This method is responsible of writing the file in the correct
         * directory and handling any exception.
         * </p>
         *
         * <p>
         * Unless anything went wrong, this method always returns an
         * empty {@link Optional}.
         * </p>
         *
         * @param containerName the container's name
         * @param type          the type
         * @throws Exception an exception, if one occurred
         */
        void write(ClassName containerName, TypeSpec.Builder type) throws Exception;
    }

}
