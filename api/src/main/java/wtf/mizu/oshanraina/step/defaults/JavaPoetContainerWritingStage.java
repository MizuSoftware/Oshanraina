package wtf.mizu.oshanraina.step.defaults;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import wtf.mizu.oshanraina.step.ContainerProcessingStage;

import javax.annotation.processing.ProcessingEnvironment;
import java.util.Optional;

/**
 * A {@link ContainerProcessingStage.Writing} stage implementation that
 * simply uses <b>JavaPoet</b>'s {@link JavaFile#writeTo(javax.annotation.processing.Filer) writeTo}
 * method.
 */
public class JavaPoetContainerWritingStage implements ContainerProcessingStage.Writing {

    private final ProcessingEnvironment processingEnvironment;

    public JavaPoetContainerWritingStage(ProcessingEnvironment processingEnvironment) {
        this.processingEnvironment = processingEnvironment;
    }

    @Override
    public void write(
            ClassName containerName, TypeSpec.Builder type
    ) throws Exception {
        final var file = JavaFile.builder(
                containerName.packageName(),
                type.build()
        ).build();

        file.writeTo(processingEnvironment.getFiler());
    }
}
