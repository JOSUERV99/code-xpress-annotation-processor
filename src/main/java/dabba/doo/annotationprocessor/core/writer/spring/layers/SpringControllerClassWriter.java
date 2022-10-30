package dabba.doo.annotationprocessor.core.writer.spring.layers;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import dabba.doo.annotationprocessor.core.annotations.J2dSpringRestCrudApi;
import dabba.doo.annotationprocessor.core.reflection.ClassReflectionTool;
import dabba.doo.annotationprocessor.db.paramresolver.ParameterMapCreator;
import dabba.doo.annotationprocessor.db.sqlwriter.SqlWriteSentenceGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Field;

public class SpringControllerClassWriter {

    public MethodSpec writeBuilder(Class<?> previousClassLayer) {
        return MethodSpec.constructorBuilder()
                .addAnnotation(Autowired.class)
                .addParameter(previousClassLayer, "service", Modifier.FINAL)
                .addStatement("this.$N = $N", "service", "service")
                .build();
    }

    public <T> MethodSpec buildCreateMethod(final Class<T> clazz) {
        return MethodSpec.methodBuilder("create")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(PostMapping.class).addMember("path", ClassReflectionTool.getPathFromMainApiAnnotation(clazz, J2dSpringRestCrudApi.class)).build())
                .addParameter(ParameterSpec.builder(clazz, "body", Modifier.FINAL).addAnnotation(RequestBody.class).build())
                .addStatement("return service.create(body)")
                .returns(boolean.class)
                .build();
    }

    public <T> MethodSpec buildGetMethod(final Class<T> clazz) {
        return MethodSpec.methodBuilder("get")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(GetMapping.class).addMember("path", ClassReflectionTool.getPathFromMainApiAnnotation(clazz, J2dSpringRestCrudApi.class)).build())
                .addParameter(clazz, "instance", Modifier.FINAL)
                .addStatement("return service.get(body)")
                .returns(ClassReflectionTool.getTypeNameForTemplateList(clazz))
                .build();
    }

    public <T> MethodSpec buildUpdateMethod(final Class<T> clazz) {
        return MethodSpec.methodBuilder("update")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(clazz, ClassReflectionTool.getIdField(clazz).getName(), Modifier.FINAL).addAnnotation(RequestParam.class).build())
                .addParameter(ParameterSpec.builder(clazz, "instance", Modifier.FINAL).addAnnotation(RequestBody.class).build())
                .addStatement("return service.update(id, body)")
                .returns(boolean.class)
                .build();
    }

    public <T> MethodSpec buildDeleteMethod(final Class<T> clazz) {
        return MethodSpec.methodBuilder("delete")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(clazz, ClassReflectionTool.getIdField(clazz).getName(), Modifier.FINAL).addAnnotation(RequestParam.class).build())
                .addStatement("return service.delete(id)")
                .returns(boolean.class)
                .build();
    }

    public JavaFile writeFile(Class<?> clazz, Class<?> previousLayerClazz, final String targetPackage) {
        return JavaFile.builder(targetPackage + ".repository", TypeSpec.classBuilder(String.format("%sRepository", clazz.getSimpleName()))
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .addAnnotation(Repository.class)
                        .addField(NamedParameterJdbcTemplate.class, "namedParameterJdbcTemplate", Modifier.FINAL, Modifier.PRIVATE)
                        .addMethod(writeBuilder(previousLayerClazz))
                        .addMethod(buildCreateMethod(clazz))
                        .addMethod(buildDeleteMethod(clazz))
                        .addMethod(buildUpdateMethod(clazz))
                        .build())
                .build();
    }
}
