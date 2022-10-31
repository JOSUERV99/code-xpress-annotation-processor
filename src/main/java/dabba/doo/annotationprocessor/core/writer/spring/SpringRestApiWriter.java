package dabba.doo.annotationprocessor.core.writer.spring;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import dabba.doo.annotationprocessor.core.writer.spring.layers.SpringControllerClassWriter;
import dabba.doo.annotationprocessor.core.writer.spring.layers.SpringRepositoryClassWriter;
import dabba.doo.annotationprocessor.core.writer.spring.layers.SpringServiceClassWriter;

public class SpringRestApiWriter {

    public void writeRestApiCode(final Class<?> clazz, final String targetPackage) {

        final JavaFile repositoryJavaFile = new SpringRepositoryClassWriter().writeFile(clazz, targetPackage);

        final TypeSpec repositoryTypeName = repositoryJavaFile.typeSpec;
        final JavaFile serviceJavaFile = new SpringServiceClassWriter().writeFile(clazz, repositoryTypeName.superclass, targetPackage);

        final TypeSpec serviceTypeName = serviceJavaFile.typeSpec;
        final JavaFile controllerJavaFile = new SpringControllerClassWriter().writeFile(clazz, serviceTypeName.superclass, targetPackage);

        System.out.println("REPO { " + repositoryJavaFile + " }");
        System.out.println("SERVICE { " + serviceJavaFile + " }");
        System.out.println("CONTROLLER { " +controllerJavaFile.toString() + " }");
    }
}
