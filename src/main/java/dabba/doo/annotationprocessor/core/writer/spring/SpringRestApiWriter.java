package dabba.doo.annotationprocessor.core.writer.spring;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import dabba.doo.annotationprocessor.core.reflection.ClassReflectionTool;
import dabba.doo.annotationprocessor.core.writer.ClassWriter;
import dabba.doo.annotationprocessor.core.writer.JavaClassFile;
import dabba.doo.annotationprocessor.core.writer.spring.config.SpringRestApiConfigWriter;
import dabba.doo.annotationprocessor.core.writer.spring.layers.SpringControllerClassWriter;
import dabba.doo.annotationprocessor.core.writer.spring.layers.SpringRepositoryClassWriter;
import dabba.doo.annotationprocessor.core.writer.spring.layers.SpringServiceClassWriter;
import dabba.doo.annotationprocessor.db.rowmapping.RowMapperCreator;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.lang.model.element.TypeElement;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SpringRestApiWriter extends ClassWriter {

    @Override
    public Collection<JavaFile> write(TypeElement clazz, String targetPackage) {

        final ClassName classNameForJdbcTemplate = ClassName.get(NamedParameterJdbcTemplate.class);
        final JavaClassFile mapperJavaFile = new RowMapperCreator().buildRowMapperJavaClassFile(clazz, targetPackage);
        final JavaClassFile repositoryJavaFile = new SpringRepositoryClassWriter().writeFile(clazz, targetPackage, mapperJavaFile.getPackageName(), mapperJavaFile.getClassName());
        final ClassName classNameForRepo = ClassReflectionTool.getClassNameFromClassName(repositoryJavaFile.getPackageName(), repositoryJavaFile.getClassName());

        final JavaClassFile serviceJavaFile = new SpringServiceClassWriter().writeFile(clazz, classNameForRepo, targetPackage);
        final ClassName classNameForService = ClassReflectionTool.getClassNameFromClassName(serviceJavaFile.getPackageName(), serviceJavaFile.getClassName());

        final JavaClassFile controllerJavaFile = new SpringControllerClassWriter().writeFile(clazz, classNameForService, targetPackage);
        final ClassName classNameForController = ClassReflectionTool.getClassNameFromClassName(controllerJavaFile.getPackageName(), controllerJavaFile.getClassName());

        final JavaFile beansConfigJavaFile =
                getBeansConfigJavaClass(
                    targetPackage,
                    Arrays.asList(classNameForRepo, classNameForService, classNameForController),
                    Arrays.asList(classNameForJdbcTemplate, classNameForRepo, classNameForService)
                );

        return Arrays.asList(
                mapperJavaFile.getJavaFile(),
                repositoryJavaFile.getJavaFile(),
                serviceJavaFile.getJavaFile(),
                controllerJavaFile.getJavaFile(),
                beansConfigJavaFile
        );
    }

    public JavaFile getBeansConfigJavaClass(
            final String packageName,
            final List<ClassName> layers,
            final List<ClassName> dependencies
    ) {
        return new SpringRestApiConfigWriter().writeBeansConfigForApi(packageName + ".configuration", layers, dependencies);
    }
}
