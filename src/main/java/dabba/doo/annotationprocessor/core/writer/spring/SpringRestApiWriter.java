package dabba.doo.annotationprocessor.core.writer.spring;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import dabba.doo.annotationprocessor.core.reflection.ClassReflectionTool;
import dabba.doo.annotationprocessor.core.writer.ClassWriter;
import dabba.doo.annotationprocessor.core.writer.JavaClassFile;
import dabba.doo.annotationprocessor.core.writer.spring.layers.SpringControllerClassWriter;
import dabba.doo.annotationprocessor.core.writer.spring.layers.SpringRepositoryClassWriter;
import dabba.doo.annotationprocessor.core.writer.spring.layers.SpringServiceClassWriter;

import javax.lang.model.element.TypeElement;
import java.util.Arrays;
import java.util.Collection;

public class SpringRestApiWriter extends ClassWriter {

    @Override
    public Collection<JavaFile> write(TypeElement clazz, String targetPackage) {

        final JavaClassFile repositoryJavaFile = new SpringRepositoryClassWriter().writeFile(clazz, targetPackage);

        final ClassName classNameForRepo = ClassReflectionTool.getClassNameFromClassName(repositoryJavaFile.getPackageName(), repositoryJavaFile.getClassName());
        final JavaClassFile serviceJavaFile = new SpringServiceClassWriter().writeFile(clazz, classNameForRepo, targetPackage);

        final ClassName classNameForService = ClassReflectionTool.getClassNameFromClassName(serviceJavaFile.getPackageName(), serviceJavaFile.getClassName());
        final JavaClassFile controllerJavaFile = new SpringControllerClassWriter().writeFile(clazz, classNameForService, targetPackage);

        return Arrays.asList(
                repositoryJavaFile.getJavaFile(),
                serviceJavaFile.getJavaFile(),
                controllerJavaFile.getJavaFile()
        );
    }
}
