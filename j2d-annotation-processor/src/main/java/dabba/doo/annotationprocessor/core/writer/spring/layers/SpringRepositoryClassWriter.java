package dabba.doo.annotationprocessor.core.writer.spring.layers;

import com.squareup.javapoet.*;
import dabba.doo.annotationprocessor.core.annotations.entity.J2dColumn;
import dabba.doo.annotationprocessor.core.reflection.ClassReflectionTool;
import dabba.doo.annotationprocessor.core.reflection.NameGenerationTool;
import dabba.doo.annotationprocessor.core.writer.JavaClassFile;
import dabba.doo.annotationprocessor.db.paramresolver.ParameterMapCreator;
import dabba.doo.annotationprocessor.db.sqlwriter.SqlReadSentenceGenerator;
import dabba.doo.annotationprocessor.db.sqlwriter.SqlWriteSentenceGenerator;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Spring Repository class writer
 *
 * @author josue.rojas
 */
public class SpringRepositoryClassWriter {

  public static final String NAMED_PARAMETER_JDBC_TEMPLATE = "namedParameterJdbcTemplate";

  /**
   * Builder method for repository class
   *
   * @return builder method
   */
  public MethodSpec writeBuilder() {
    return MethodSpec.constructorBuilder()
        .addAnnotation(Autowired.class)
        .addModifiers(Modifier.PUBLIC)
        .addParameter(NamedParameterJdbcTemplate.class, NAMED_PARAMETER_JDBC_TEMPLATE)
        .addStatement("this.$N = $N", NAMED_PARAMETER_JDBC_TEMPLATE, NAMED_PARAMETER_JDBC_TEMPLATE)
        .build();
  }

  /**
   * Creator method for repository class
   *
   * @return create method
   */
  public MethodSpec buildCreateMethod(final TypeElement clazz) {
    return MethodSpec.methodBuilder("create")
        .addModifiers(Modifier.PUBLIC)
        .addParameter(
            NameGenerationTool.getTypeNameFromTypeElement(clazz), "instance", Modifier.FINAL)
        .addStatement(
            "return namedParameterJdbcTemplate.update(\n"
                + "            $S,\n"
                + "            $T.buildParamsMap(instance)\n"
                + "            ) > 0",
            SqlWriteSentenceGenerator.writeInsertSentence(clazz),
            ParameterMapCreator.class)
        .returns(boolean.class)
        .build();
  }

  /**
   * Get method for repository class
   *
   * @return get method
   */
  public MethodSpec buildGetMethod(final TypeElement clazz, final ClassName mapperClassName) {
    return MethodSpec.methodBuilder("get")
        .addModifiers(Modifier.PUBLIC)
        .addStatement(
            "return namedParameterJdbcTemplate.query($S, new $T(), new $T())",
            SqlReadSentenceGenerator.writeSelectSentence(clazz),
            MapSqlParameterSource.class,
            mapperClassName)
        .returns(NameGenerationTool.getTypeNameForTemplateList(clazz))
        .build();
  }

  /**
   * Update method for repository class
   *
   * @return update method
   */
  public MethodSpec buildUpdateMethod(final TypeElement clazz) {
    return MethodSpec.methodBuilder("update")
        .addModifiers(Modifier.PUBLIC)
        .addParameter(
            ParameterSpec.builder(NameGenerationTool.getTypeNameForId(clazz), "id", Modifier.FINAL)
                .build())
        .addParameter(
            NameGenerationTool.getTypeNameFromTypeElement(clazz), "instance", Modifier.FINAL)
        .addStatement(
            "return namedParameterJdbcTemplate.update(\n"
                + "            $S,\n"
                + "            $T.buildParamsMap(instance).addValue($S, id)\n"
                + "            ) > 0",
            SqlWriteSentenceGenerator.writeUpdateSentence(clazz),
            ParameterMapCreator.class,
            ClassReflectionTool.getIdField(clazz).getAnnotation(J2dColumn.class).name())
        .returns(boolean.class)
        .build();
  }

  /**
   * Delete method for repository class
   *
   * @return delete method
   */
  public MethodSpec buildDeleteMethod(final TypeElement clazz) {
    return MethodSpec.methodBuilder("delete")
        .addModifiers(Modifier.PUBLIC)
        .addParameter(
            ParameterSpec.builder(NameGenerationTool.getTypeNameForId(clazz), "id", Modifier.FINAL)
                .build())
        .addStatement(
            "return namedParameterJdbcTemplate.update(\n"
                + "            $S,\n"
                + "            new $T().addValue($S, id)\n"
                + "            ) > 0",
            SqlWriteSentenceGenerator.writeDeleteSentence(clazz),
            MapSqlParameterSource.class,
                ClassReflectionTool.getIdField(clazz).getAnnotation(J2dColumn.class).name())
        .returns(boolean.class)
        .build();
  }

  /**
   * Write repository layer class
   *
   * @param clazz J2dEntity annotated class
   * @param targetPackage package where entity is located
   * @param mapperPackageName mapper package name
   * @param mapperClassName mapper class name
   * @return JavaClassFile for repository generated class
   */
  public JavaClassFile writeFile(
      TypeElement clazz,
      final String targetPackage,
      final String mapperPackageName,
      final String mapperClassName) {
    final String packageName = targetPackage + ".repository";
    final String className = String.format("%sRepository", clazz.getSimpleName());
    final JavaFile javaFile =
        JavaFile.builder(
                packageName,
                TypeSpec.classBuilder(className)
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Repository.class)
                    .addField(
                        NamedParameterJdbcTemplate.class,
                        NAMED_PARAMETER_JDBC_TEMPLATE,
                        Modifier.FINAL,
                        Modifier.PRIVATE)
                    .addMethod(writeBuilder())
                    .addMethod(
                        buildGetMethod(
                            clazz,
                            ClassReflectionTool.getClassNameFromClassName(
                                mapperPackageName, mapperClassName)))
                    .addMethod(buildCreateMethod(clazz))
                    .addMethod(buildDeleteMethod(clazz))
                    .addMethod(buildUpdateMethod(clazz))
                    .build())
            .build();

    return new JavaClassFile()
        .setJavaFile(javaFile)
        .setClassName(className)
        .setPackageName(packageName)
        .setFileName(String.format("%s.%s.java", packageName, className))
        .setName((String.format("%s.%s", packageName, className)));
  }
}
