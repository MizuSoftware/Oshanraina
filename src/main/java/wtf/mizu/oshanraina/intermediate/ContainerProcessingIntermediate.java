package wtf.mizu.oshanraina.intermediate;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import java.util.Optional;

public interface ContainerProcessingIntermediate {

    void process(TypeSpec.Builder type, Element element,
                 ClassName containerName, ProcessingEnvironment env) throws Exception;

}
