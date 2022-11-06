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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.lang.model.element.TypeElement;
import org.springframework.context.annotation.Description;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * SpringRestApiWriter class Spring layer writer based on J2D annotated class passing by type
 * element and target package name based on the same class, generated java files based on API
 * annotation
 *
 * @see dabba.doo.annotationprocessor.core.annotations.J2dSpringRestCrudApi
 * @author josue.rojas
 */
public class SpringRestApiWriter extends ClassWriter {

  /**
   * Write java files based on API annotations
   *
   * @param clazz entity class
   * @param targetPackage base package name where the files are going to be located
   * @return Collection<JavaFile> java class files based on J2D annotations
   */
  @Override
  public Collection<JavaFile> write(final TypeElement clazz, final String targetPackage) {

    // repository layer
    final ClassName classNameForJdbcTemplate = ClassName.get(NamedParameterJdbcTemplate.class);
    final JavaClassFile mapperJavaFile =
        new RowMapperCreator().buildRowMapperJavaClassFile(clazz, targetPackage);
    final JavaClassFile repositoryJavaFile =
        new SpringRepositoryClassWriter()
            .writeFile(
                clazz,
                targetPackage,
                mapperJavaFile.getPackageName(),
                mapperJavaFile.getClassName());
    final ClassName classNameForRepo =
        ClassReflectionTool.getClassNameFromClassName(
            repositoryJavaFile.getPackageName(), repositoryJavaFile.getClassName());

    // service layer
    final JavaClassFile serviceJavaFile =
        new SpringServiceClassWriter().writeFile(clazz, classNameForRepo, targetPackage);
    final ClassName classNameForService =
        ClassReflectionTool.getClassNameFromClassName(
            serviceJavaFile.getPackageName(), serviceJavaFile.getClassName());

    // controller layer
    final JavaClassFile controllerJavaFile =
        new SpringControllerClassWriter().writeFile(clazz, classNameForService, targetPackage);
    final ClassName classNameForController =
        ClassReflectionTool.getClassNameFromClassName(
            controllerJavaFile.getPackageName(), controllerJavaFile.getClassName());

    // config java file
    final JavaFile beansConfigJavaFile =
        getBeansConfigJavaClass(
            targetPackage,
            Arrays.asList(classNameForRepo, classNameForService, classNameForController),
            Arrays.asList(classNameForJdbcTemplate, classNameForRepo, classNameForService));

    // java files to be generated
    return Arrays.asList(
        mapperJavaFile.getJavaFile(),
        repositoryJavaFile.getJavaFile(),
        serviceJavaFile.getJavaFile(),
        controllerJavaFile.getJavaFile(),
        beansConfigJavaFile);
  }

  /**
   * Get java class file for beans for J2D API
   *
   * @param packageName package name as string
   * @param layers classname list
   * @param dependencies classname for dependencies
   * @return JavaFIle java file class
   */
  @Description("Beans class files generation based J2D annotation")
  public JavaFile getBeansConfigJavaClass(
      final String packageName, final List<ClassName> layers, final List<ClassName> dependencies) {
    return new SpringRestApiConfigWriter()
        .writeBeansConfigForApi(packageName + ".configuration", layers, dependencies);
  }
}
