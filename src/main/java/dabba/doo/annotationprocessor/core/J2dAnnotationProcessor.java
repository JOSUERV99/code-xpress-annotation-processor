package dabba.doo.annotationprocessor.core;

import com.google.auto.service.AutoService;
import dabba.doo.annotationprocessor.core.annotations.J2dSpringRestCrudApi;
import dabba.doo.annotationprocessor.core.writer.spring.SpringRestApiWriter;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@SupportedAnnotationTypes("dabba.doo.annotationprocessor.core.annotations.*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class J2dAnnotationProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
                           RoundEnvironment roundEnv) {

        annotations.forEach(annotation -> {
            try {
                applyAnnotations(annotation, roundEnv);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        return false;
    }

    public void applyAnnotations(TypeElement annotation, RoundEnvironment roundEnv) throws ClassNotFoundException {
        TypeMirror type = annotation.asType();

        for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {

            System.out.println(element.toString());
            System.out.println(Class.forName(element.toString(), true, this.getClass().getClassLoader()));
            if (J2dSpringRestCrudApi.class.getName().equals(type.toString())) {
                final SpringRestApiWriter springRestApiWriter = new SpringRestApiWriter();
                Class<?> clazz = null;
                try {
                    clazz = Class.forName(type.toString());
                } catch (ClassNotFoundException e) {
                    throw e;
                }
                springRestApiWriter.writeRestApiCode(clazz, "j2d.generated");
            }

        }
    }

    @Override
    public synchronized void init(ProcessingEnvironment env) {

    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        // Return the set of annotations supported
        return new HashSet<String>(Arrays.asList(J2dSpringRestCrudApi.class.getName()));
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        // Return the Java version supported
        return SourceVersion.RELEASE_8;
    }
}
