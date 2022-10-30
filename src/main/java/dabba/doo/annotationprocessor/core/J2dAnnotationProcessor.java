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
import java.io.DataInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return false;
    }

    public static List<Class> getClasses(ClassLoader cl,String pack) throws Exception{

        String dottedPackage = pack.replaceAll("[/]", ".");
        List<Class> classes = new ArrayList<Class>();
        URL upackage = cl.getResource(pack);

        DataInputStream dis = new DataInputStream((InputStream) upackage.getContent());
        String line = null;
        while ((line = dis.readLine()) != null) {
            if(line.endsWith(".class")) {
                classes.add(Class.forName(dottedPackage+"."+line.substring(0,line.lastIndexOf('.'))));
            }
        }
        return classes;
    }

    public void applyAnnotations(TypeElement annotation, RoundEnvironment roundEnv) throws Exception {
        TypeMirror type = annotation.asType();

        for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {

            if (J2dSpringRestCrudApi.class.getName().equals(type.toString())) {
                final SpringRestApiWriter springRestApiWriter = new SpringRestApiWriter();
//                System.out.println(element.asType());
//                System.out.println(element.getAnnotationMirrors());
//                System.out.println(element.getKind());
//                System.out.println(element.getSimpleName());
//                System.out.println(element.getEnclosedElements());
//                System.out.println(element.getEnclosingElement());

                final String packageName = Arrays.stream(element.toString().split(".")).filter(part -> part.matches("[a-z_]")).collect(Collectors.joining("."));
                System.out.println(packageName);

                List<Class> classes = getClasses(getClass().getClassLoader(),packageName);
                for(Class c:classes){
                    System.out.println("Class: "+c);
                }

                System.out.println("asdasdasd");

//                dabba.doo.example.model.Pojo
//                @dabba.doo.annotationprocessor.core.annotations.J2dSpringRestCrudApi,@dabba.doo.annotationprocessor.core.annotations.entity.J2dEntity(tableName="pojo_table")
//                CLASS
//                        Pojo
//                Pojo(),id,secondId,messageContent,countNumberFromExternalService,getId(),setId(java.lang.Integer),getSecondId(),setSecondId(java.lang.Integer),getMessageContent(),setMessageContent(java.lang.String),getCountNumberFromExternalService(),setCountNumberFromExternalService(java.lang.Integer),toString()
//                dabba.doo.example.model

                System.exit(0);
                springRestApiWriter.writeRestApiCode(null, "j2d.generated");
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
