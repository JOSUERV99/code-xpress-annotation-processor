package dabba.doo.annotationprocessor.core.writer.spring;

import com.squareup.javapoet.JavaFile;
import dabba.doo.annotationprocessor.core.writer.spring.layers.SpringRepositoryClassWriter;
import dabba.doo.annotationprocessor.core.writer.spring.layers.SpringServiceClassWriter;

public class SpringRestApiWriter {

    public void writeRestApiCode(final Class<?> clazz, final String targetPackage) {

        final JavaFile repositoryJavaFile = new SpringRepositoryClassWriter().writeFile(clazz, targetPackage);
        final JavaFile serviceJavaFile = new SpringServiceClassWriter().writeFile(clazz, targetPackage);

        System.out.println("REPO { " + repositoryJavaFile.toString() + " }");
        System.out.println("SERVICE { " +serviceJavaFile.toString() + " }");
    }
}
