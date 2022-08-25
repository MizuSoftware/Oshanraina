package wtf.mizu.oshanraina.pipeline.step;

import com.squareup.javapoet.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import wtf.mizu.oshanraina.api.Container;
import wtf.mizu.oshanraina.pipeline.intermediate.ProcessingIntermediate;
import wtf.mizu.oshanraina.pipeline.step.defaults.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import java.util.List;
import java.util.Optional;

/**
 * Represents a <b>single and unique</b> {@link Container} processing stage.
 * <p>
 * In order to instantiate a {@link Container}, 3 stages are required:
 * <ol>
 *     <li>{@link Initialization}.</li>
 *     <li>{@link Intermediary}.</li>
 *     <li>{@link Writing}.</li>
 * </ol>
 * <p>
 * Unless all of these steps are provided, the {@link }
 *
 * @author Shyrogan
 * @since 0.0.1
 */
public interface ProcessingStage {
    /**
     * Stage responsible for setting up common settings to ensure a correct
     * container processing.
     */
    interface Initialization extends ProcessingStage {
        /**
         * Syntactic sugar to the {@link JavaPoetPrefixedInitializationStage}.
         *
         * @param prefix the prefix.
         * @return the newly created {@link JavaPoetPrefixedInitializationStage}.
         */
        static Initialization withPrefix(String prefix) {
            return new JavaPoetPrefixedInitializationStage(prefix);
        }

        /**
         * Syntactic sugar to the {@link JavaPoetSuffixedInitializationStage}.
         *
         * @param suffix the suffix.
         * @return the newly created {@link JavaPoetSuffixedInitializationStage}.
         */
        static Initialization withSuffix(String suffix) {
            return new JavaPoetSuffixedInitializationStage(suffix);
        }

        /**
         * Initializes the processing of a future {@link Container} and
         * returns a {@link Result} that wraps the required data to continue.
         *
         * @param element the element to initialize.
         * @return the {@link Result} that wraps the required data.
         */
        Result initialize(Element element);

        /**
         * Wraps all the data required to process a given {@link Container}.
         */
        class Result {
            /**
             * The type spec.
             */
            public final TypeSpec.Builder type;

            /**
             * The container's class name.
             */
            public final ClassName containerName;

            /**
             * @param type          the type spec.
             * @param containerName the container's class name.
             */
            public Result(
                    @NotNull TypeSpec.Builder type,
                    @NotNull ClassName containerName
            ) {
                this.type = type;
                this.containerName = containerName;
            }
        }
    }

    /**
     * Stage generally responsible for manipulating the processed container's
     * data.
     */
    @FunctionalInterface
    interface Intermediary extends ProcessingStage {
        /**
         * Returns the {@link ProcessingIntermediate processing
         * intermediates} used during this intermediate stage.
         *
         * @return the {@link ProcessingIntermediate intermediates}
         * @see ProcessingIntermediate
         */
        @Unmodifiable List<ProcessingIntermediate> intermediates();
    }

    /**
     * Stage responsible for writing the processed container's content.
     */
    interface Writing {
        /**
         * Syntactic sugar to the {@link JavaPoetWritingStage}.
         *
         * @param env the processing environment.
         * @return a new {@link JavaPoetWritingStage}.
         */
        static Writing usingJavaPoet(
                ProcessingEnvironment env
        ) {
            return new JavaPoetWritingStage(env);
        }

        /**
         * Called once every {@link ProcessingIntermediate} has been
         * applied to the future {@link Container}.
         * <p>
         * This method is responsible for writing the file in the correct
         * directory and handling any exception.
         * <p>
         * Unless anything went wrong, this method always returns an
         * empty {@link Optional}.
         *
         * @param containerName the container's name.
         * @param type          the type.
         * @throws Exception if something went wrong.
         */
        void write(
                ClassName containerName,
                TypeSpec.Builder type
        ) throws Exception;
    }
}
