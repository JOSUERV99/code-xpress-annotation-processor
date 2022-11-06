package dabba.doo.annotationprocessor.core;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeName;
import dabba.doo.annotationprocessor.core.annotations.J2dSpringRestCrudApi;
import dabba.doo.annotationprocessor.core.reflection.ClassReflectionTool;
import dabba.doo.annotationprocessor.core.writer.spring.SpringRestApiWriter;
import dabba.doo.annotationprocessor.db.sqlwriter.SqlReadSentenceGenerator;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Main class for the J2dAnnotationProcessor
 *
 * An annotation processor to generate java class files
 * to handle J2dEntities, aiming for persistence and code generation
 *
 * @author josue.rojas
 */
@SupportedAnnotationTypes("dabba.doo.annotationprocessor.core.annotations.*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class J2dAnnotationProcessor extends AbstractProcessor {

    private SpringRestApiWriter springRestApiWriter;
    private Filer filer;
    private Messager messager;

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
                           RoundEnvironment roundEnv) {
        annotations.forEach(annotation -> {
            Collection<JavaFile> javaFiles;
            try {
                javaFiles= applyAnnotations(annotation, roundEnv);
                javaFiles.stream().forEach(jf -> {
                    try {
                        jf.writeTo(filer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return true;
    }

    public Collection<JavaFile> applyAnnotations(TypeElement annotation, RoundEnvironment roundEnv) {
        final List<JavaFile> javaFileList = new ArrayList<>();
        for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
            if (J2dSpringRestCrudApi.class.getName().equals(annotation.asType().toString())) {
                final String packageName = ClassReflectionTool.getPackageName(element);
                final Collection<JavaFile> javaFiles = springRestApiWriter.write((TypeElement) element, packageName);
                javaFileList.addAll(javaFiles);
            }
        }

        return javaFileList;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        springRestApiWriter = new SpringRestApiWriter();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

}
