package dabba.doo.annotationprocessor.core.writer.spring.layers;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import dabba.doo.annotationprocessor.core.annotations.J2dSpringRestCrudApi;
import dabba.doo.annotationprocessor.core.reflection.ClassReflectionTool;
import dabba.doo.annotationprocessor.core.reflection.NameGenerationTool;
import dabba.doo.annotationprocessor.core.writer.JavaClassFile;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * SpringControllerClassWriter Writes a Spring-like controller class, where the request will be
 * received
 *
 * @author josue.rojas
 */
public class SpringControllerClassWriter {

  private final String fileSuffixPackageName = "controller";
  private final String fileSuffixClassName = "Controller";
  private String lastLayerAttributeName = "service";

  public MethodSpec writeBuilder(TypeName previousTypeLayer) {
    return MethodSpec.constructorBuilder()
        .addAnnotation(Autowired.class)
        .addModifiers(Modifier.PUBLIC)
        .addParameter(
            ParameterSpec.builder(previousTypeLayer, lastLayerAttributeName, Modifier.FINAL)
                .build())
        .addStatement("this.$N = $N", lastLayerAttributeName, lastLayerAttributeName)
        .build();
  }

  public MethodSpec buildCreateMethod(final TypeElement clazz) {
    return MethodSpec.methodBuilder("create")
        .addModifiers(Modifier.PUBLIC)
        .addAnnotation(
            AnnotationSpec.builder(PostMapping.class)
                .addMember(
                    "path",
                    "$S",
                    ClassReflectionTool.getPathFromMainApiAnnotation(
                        clazz, J2dSpringRestCrudApi.class))
                .build())
        .addParameter(
            ParameterSpec.builder(
                    NameGenerationTool.getTypeNameFromTypeElement(clazz), "body", Modifier.FINAL)
                .addAnnotation(RequestBody.class)
                .build())
        .addStatement("return $N.create(body)", lastLayerAttributeName)
        .returns(boolean.class)
        .build();
  }

  public MethodSpec buildGetMethod(final TypeElement clazz) {
    return MethodSpec.methodBuilder("get")
        .addModifiers(Modifier.PUBLIC)
        .addAnnotation(
            AnnotationSpec.builder(GetMapping.class)
                .addMember(
                    "path",
                    "$S",
                    ClassReflectionTool.getPathFromMainApiAnnotation(
                        clazz, J2dSpringRestCrudApi.class))
                .build())
        .addStatement("return $N.get()", lastLayerAttributeName)
        .returns(NameGenerationTool.getTypeNameForTemplateList(clazz))
        .build();
  }

  public <T> MethodSpec buildUpdateMethod(final TypeElement clazz) {
    return MethodSpec.methodBuilder("update")
        .addModifiers(Modifier.PUBLIC)
        .addAnnotation(
            AnnotationSpec.builder(PutMapping.class)
                .addMember(
                    "path",
                    "$S",
                    ClassReflectionTool.getPathFromMainApiAnnotation(
                        clazz, J2dSpringRestCrudApi.class, "id"))
                .build())
        .addParameter(
            ParameterSpec.builder(NameGenerationTool.getTypeNameForId(clazz), "id", Modifier.FINAL)
                .addAnnotation(
                    AnnotationSpec.builder(RequestParam.class)
                        .addMember("value", "$S", "id")
                        .build())
                .build())
        .addParameter(
            ParameterSpec.builder(
                    NameGenerationTool.getTypeNameFromTypeElement(clazz), "body", Modifier.FINAL)
                .addAnnotation(RequestBody.class)
                .build())
        .addStatement("return $N.update(id, body)", lastLayerAttributeName)
        .returns(boolean.class)
        .build();
  }

  public MethodSpec buildDeleteMethod(final TypeElement clazz) {
    return MethodSpec.methodBuilder("delete")
        .addModifiers(Modifier.PUBLIC)
        .addAnnotation(
            AnnotationSpec.builder(DeleteMapping.class)
                .addMember(
                    "path",
                    "$S",
                    ClassReflectionTool.getPathFromMainApiAnnotation(
                        clazz, J2dSpringRestCrudApi.class, "id"))
                .build())
        .addParameter(
            ParameterSpec.builder(
                    NameGenerationTool.getTypeNameForId(clazz),
                    ClassReflectionTool.getIdField(clazz).getSimpleName().toString(),
                    Modifier.FINAL)
                .addAnnotation(
                    AnnotationSpec.builder(RequestParam.class)
                        .addMember("value", "$S", "id")
                        .build())
                .build())
        .addStatement("return $N.delete(id)", lastLayerAttributeName)
        .returns(boolean.class)
        .build();
  }

  public JavaClassFile writeFile(
      TypeElement clazz, TypeName previousLayerClazz, final String targetPackage) {
    lastLayerAttributeName = NameGenerationTool.getSimpleNameForAttr(previousLayerClazz);
    final String packageName = targetPackage + "." + fileSuffixPackageName;
    final String className = String.format("%sController", clazz.getSimpleName());
    final JavaFile javaFile =
        JavaFile.builder(
                targetPackage + "." + fileSuffixPackageName,
                TypeSpec.classBuilder(className)
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(RestController.class)
                    .addField(
                        previousLayerClazz,
                        lastLayerAttributeName,
                        Modifier.FINAL,
                        Modifier.PRIVATE)
                    .addMethod(writeBuilder(previousLayerClazz))
                    .addMethod(buildGetMethod(clazz))
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
