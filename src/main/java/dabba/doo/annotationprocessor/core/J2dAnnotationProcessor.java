package dabba.doo.annotationprocessor.core;

import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotationTypes("dabba.doo.annotationprocessor.core.annotations.J2dSpringRestCrudApi")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class J2dAnnotationProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
                           RoundEnvironment roundEnv) {
        System.out.println("process");
        return false;
    }

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        // Initialize the processor
        System.out.println("init");
    }

//    @Override
//    public Set<String> getSupportedAnnotationTypes() {
//        // Return the set of annotations supported
//        return new HashSet<String>(Arrays.asList(J2dSpringRestCrudApi.class.getName()));
//    }
//
//    @Override
//    public SourceVersion getSupportedSourceVersion() {
//        // Return the Java version supported
//        return SourceVersion.RELEASE_8;
//    }
}
