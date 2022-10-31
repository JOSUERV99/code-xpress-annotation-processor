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
import java.util.Set;
import java.util.stream.Collectors;

@SupportedAnnotationTypes("dabba.doo.annotationprocessor.core.annotations.*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class J2dAnnotationProcessor extends AbstractProcessor {

    private SpringRestApiWriter springRestApiWriter;

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
                           RoundEnvironment roundEnv) {

        annotations.forEach(annotation -> {
            try {
                applyAnnotations(annotation, roundEnv);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return true;
    }

    public void applyAnnotations(TypeElement annotation, RoundEnvironment roundEnv) throws Exception {
        TypeMirror type = annotation.asType();

        for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {

            if (J2dSpringRestCrudApi.class.getName().equals(type.toString())) {
                final String packageName = Arrays.stream(element.toString().split(".")).filter(part -> part.matches("[a-z_]")).collect(Collectors.joining("."));
                System.out.println(packageName);

//                Class<?> clazz = Class.forName(element.toString());
                System.out.println(element);

                //springRestApiWriter.writeRestApiCode(clazz, packageName);
            }

        }
    }

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        springRestApiWriter = new SpringRestApiWriter();
    }

}
