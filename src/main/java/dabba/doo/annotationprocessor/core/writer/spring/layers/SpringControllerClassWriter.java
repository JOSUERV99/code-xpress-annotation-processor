package dabba.doo.annotationprocessor.core.writer.spring.layers;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import dabba.doo.annotationprocessor.core.annotations.J2dSpringRestCrudApi;
import dabba.doo.annotationprocessor.core.reflection.ClassReflectionTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.lang.model.element.Modifier;

public class SpringControllerClassWriter {

    private final String fileSuffixPackageName = "controller";
    private final String fileSuffixClassName = "Controller";

    public MethodSpec writeBuilder(TypeName previousTypeLayer) {
        return MethodSpec.constructorBuilder()
                .addAnnotation(Autowired.class)
                .addParameter(ParameterSpec.builder(previousTypeLayer, "service", Modifier.FINAL).build())
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
                .addAnnotation(AnnotationSpec.builder(PutMapping.class).addMember("path", ClassReflectionTool.getPathFromMainApiAnnotation(clazz, J2dSpringRestCrudApi.class)).build())
                .addParameter(ParameterSpec.builder(clazz, ClassReflectionTool.getIdField(clazz).getName(), Modifier.FINAL).addAnnotation(RequestParam.class).build())
                .addParameter(ParameterSpec.builder(clazz, "instance", Modifier.FINAL).addAnnotation(RequestBody.class).build())
                .addStatement("return service.update(id, body)")
                .returns(boolean.class)
                .build();
    }

    public <T> MethodSpec buildDeleteMethod(final Class<T> clazz) {
        return MethodSpec.methodBuilder("delete")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(DeleteMapping.class).addMember("path", ClassReflectionTool.getPathFromMainApiAnnotation(clazz, J2dSpringRestCrudApi.class)).build())
                .addParameter(ParameterSpec.builder(clazz, ClassReflectionTool.getIdField(clazz).getName(), Modifier.FINAL).addAnnotation(RequestParam.class).build())
                .addStatement("return service.delete(id)")
                .returns(boolean.class)
                .build();
    }

    public JavaFile writeFile(Class<?> clazz, TypeName previousLayerClazz, final String targetPackage) {
        return JavaFile.builder(targetPackage + "." + fileSuffixPackageName, TypeSpec.classBuilder(String.format("%s%s", clazz.getSimpleName(), fileSuffixClassName))
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .addAnnotation(Controller.class)
                        .addField(previousLayerClazz, "service", Modifier.FINAL, Modifier.PRIVATE)
                        .addMethod(writeBuilder(previousLayerClazz))
                        .addMethod(buildGetMethod(clazz))
                        .addMethod(buildCreateMethod(clazz))
                        .addMethod(buildDeleteMethod(clazz))
                        .addMethod(buildUpdateMethod(clazz))
                        .build())
                .build();
    }
}
