package dabba.doo.annotationprocessor.core.writer.spring.layers;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import dabba.doo.annotationprocessor.core.reflection.ClassReflectionTool;
import dabba.doo.annotationprocessor.db.paramresolver.ParameterMapCreator;
import dabba.doo.annotationprocessor.db.sqlwriter.SqlReadSentenceGenerator;
import dabba.doo.annotationprocessor.db.sqlwriter.SqlWriteSentenceGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Field;

public class SpringRepositoryClassWriter {

    public MethodSpec writeBuilder() {
        return MethodSpec.constructorBuilder()
                .addAnnotation(Autowired.class)
                .addParameter(NamedParameterJdbcTemplate.class, "namedParameterJdbcTemplate")
                .addStatement("this.$N = $N", "namedParameterJdbcTemplate", "namedParameterJdbcTemplate")
                .build();
    }

    public <T> MethodSpec buildCreateMethod(final Class<T> clazz) {
        return MethodSpec.methodBuilder("create")
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(clazz, "instance", Modifier.FINAL)
                    .addStatement("return namedParameterJdbcTemplate.update(\n" +
                            "            $S,\n" +
                            "            $T.buildParamsMap(instance)\n" +
                            "            ) > 0", SqlWriteSentenceGenerator.writeInsertSentence(clazz), ParameterMapCreator.class)
                    .returns(boolean.class)
                    .build();
    }

    public <T> MethodSpec buildGetMethod(final Class<T> clazz) {
        return MethodSpec.methodBuilder("get")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(clazz, "instance", Modifier.FINAL)
                .addStatement("return namedParameterJdbcTemplate.queryForList(\n" +
                        "            $S,\n" +
                        "            $T.class"+
                        "            )", SqlReadSentenceGenerator.writeSelectSentence(clazz), clazz)
                .returns(ClassReflectionTool.getTypeNameForTemplateList(clazz))
                .build();
    }

    public <T> MethodSpec buildUpdateMethod(final Class<T> clazz) {
        Field idField = ClassReflectionTool.getIdField(clazz);

        return MethodSpec.methodBuilder("update")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(idField.getType(), "id", Modifier.FINAL)
                .addParameter(clazz, "instance", Modifier.FINAL)
                .addStatement("return namedParameterJdbcTemplate.update(\n" +
                        "            $S,\n" +
                        "            $T.buildParamsMap(instance).addValue($S, id)\n" +
                        "            ) > 0", SqlWriteSentenceGenerator.writeUpdateSentence(clazz), ParameterMapCreator.class, "id")
                .returns(boolean.class)
                .build();
    }

    public <T> MethodSpec buildDeleteMethod(final Class<T> clazz) {
        return MethodSpec.methodBuilder("delete")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(clazz, "instance", Modifier.FINAL)
                .addStatement("return namedParameterJdbcTemplate.update(\n" +
                        "            $S,\n" +
                        "            $T.buildParamsMap(instance)\n" +
                        "            ) > 0", SqlWriteSentenceGenerator.writeDeleteSentence(clazz), ParameterMapCreator.class)
                .returns(boolean.class)
                .build();
    }

    public JavaFile writeFile(Class<?> clazz, final String targetPackage) {
        return JavaFile.builder(targetPackage + ".repository", TypeSpec.classBuilder(String.format("%sRepository", clazz.getSimpleName()))
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .addAnnotation(Repository.class)
                        .addField(NamedParameterJdbcTemplate.class, "namedParameterJdbcTemplate", Modifier.FINAL, Modifier.PRIVATE)
                        .addMethod(writeBuilder())
                        .addMethod(buildGetMethod(clazz))
                        .addMethod(buildCreateMethod(clazz))
                        .addMethod(buildDeleteMethod(clazz))
                        .addMethod(buildUpdateMethod(clazz))
                        .build())
                .build();
    }

}
