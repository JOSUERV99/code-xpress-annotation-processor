package dabba.doo.annotationprocessor.core.writer.spring.layers;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import dabba.doo.annotationprocessor.core.reflection.ClassReflectionTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.lang.model.element.Modifier;

public class SpringServiceClassWriter<T> {

    public MethodSpec writeBuilder(Class<?> previousClassLayer) {
        return MethodSpec.constructorBuilder()
                .addAnnotation(Autowired.class)
                .addParameter(previousClassLayer, "repository", Modifier.FINAL)
                .addStatement("this.$N = $N", "repository", "repository")
                .build();
    }

    public <T> MethodSpec buildCreateMethod(final Class<T> clazz) {
        return MethodSpec.methodBuilder("create")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(clazz, "instance", Modifier.FINAL)
                .addStatement("return repository.create(instance)")
                .returns(boolean.class)
                .build();
    }

    public <T> MethodSpec buildGetMethod(final Class<T> clazz) {
        return MethodSpec.methodBuilder("get")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return repository.get()")
                .returns(ClassReflectionTool.getTypeNameForTemplateList(clazz))
                .build();
    }

    public <T> MethodSpec buildDeleteMethod(final Class<T> clazz) {
        return MethodSpec.methodBuilder("delete")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(clazz, "instance", Modifier.FINAL)
                .addStatement("return repository.delete(instance)")
                .returns(boolean.class)
                .build();
    }

    public <T> MethodSpec buildUpdateMethod(final Class<T> clazz) {
        return MethodSpec.methodBuilder("update")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Object.class, "id", Modifier.FINAL)
                .addParameter(clazz, "instance", Modifier.FINAL)
                .addStatement("return repository.update(id, instance)")
                .returns(boolean.class)
                .build();
    }

    public JavaFile writeFile(Class<?> clazz, Class<?> previousLayerClazz, String targetPackage) {
        return JavaFile.builder(targetPackage + ".service", TypeSpec.classBuilder(String.format("%sService", clazz.getSimpleName()))
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .addAnnotation(Service.class)
                        .addField(clazz, "repository", Modifier.FINAL, Modifier.PRIVATE)
                        .addMethod(writeBuilder(previousLayerClazz))
                        .addMethod(buildGetMethod(clazz))
                        .addMethod(buildUpdateMethod(clazz))
                        .addMethod(buildCreateMethod(clazz))
                        .addMethod(buildDeleteMethod(clazz))
                        .build())
                .build();
    }
}

