package dabba.doo.annotationprocessor.core.writer.spring.layers;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import dabba.doo.annotationprocessor.db.paramresolver.ParameterMapCreator;
import dabba.doo.annotationprocessor.db.sqlwriter.SqlWriteSentenceGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.lang.model.element.Modifier;

public class SpringRepositoryClassWriter<T> {

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

    public MethodSpec buildGetMethod() {
        return null;
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

    public JavaFile writeFile(Class<?> clazz) {
        return JavaFile.builder("j2d.generated.repository", TypeSpec.classBuilder(String.format("%sRepository", clazz.getSimpleName()))
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .addAnnotation(Repository.class)
                        .addField(NamedParameterJdbcTemplate.class, "namedParameterJdbcTemplate", Modifier.FINAL, Modifier.PRIVATE)
                        .addMethod(writeBuilder())
                        .addMethod(buildCreateMethod(clazz))
                        .addMethod(buildDeleteMethod(clazz))
                        .build())
                .build();
    }

}
