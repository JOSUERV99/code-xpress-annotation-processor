package dabba.doo.annotationprocessor.core.writer.spring.layers;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import dabba.doo.annotationprocessor.core.reflection.NameGenerationTool;
import dabba.doo.annotationprocessor.core.writer.JavaClassFile;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public class SpringServiceClassWriter {

  private String lastLayerAttributeName = "repository";

  public MethodSpec writeBuilder(TypeName previousClassLayer) {
    return MethodSpec.constructorBuilder()
        .addAnnotation(Autowired.class)
        .addModifiers(Modifier.PUBLIC)
        .addParameter(previousClassLayer, lastLayerAttributeName, Modifier.FINAL)
        .addStatement("this.$N = $N", lastLayerAttributeName, lastLayerAttributeName)
        .build();
  }

  public MethodSpec buildCreateMethod(final TypeElement clazz) {
    return MethodSpec.methodBuilder("create")
        .addModifiers(Modifier.PUBLIC)
        .addParameter(
            NameGenerationTool.getTypeNameFromTypeElement(clazz), "instance", Modifier.FINAL)
        .addStatement("return $N.create(instance)", lastLayerAttributeName)
        .returns(boolean.class)
        .build();
  }

  public MethodSpec buildGetMethod(final TypeElement clazz) {
    return MethodSpec.methodBuilder("get")
        .addModifiers(Modifier.PUBLIC)
        .addStatement("return $N.get()", lastLayerAttributeName)
        .returns(NameGenerationTool.getTypeNameForTemplateList(clazz))
        .build();
  }

  public MethodSpec buildDeleteMethod(final TypeElement clazz) {
    return MethodSpec.methodBuilder("delete")
        .addModifiers(Modifier.PUBLIC)
        .addParameter(NameGenerationTool.getTypeNameForId(clazz), "id", Modifier.FINAL)
        .addStatement("return $N.delete(id)", lastLayerAttributeName)
        .returns(boolean.class)
        .build();
  }

  public MethodSpec buildUpdateMethod(final TypeElement clazz) {
    return MethodSpec.methodBuilder("update")
        .addModifiers(Modifier.PUBLIC)
        .addParameter(NameGenerationTool.getTypeNameForId(clazz), "id", Modifier.FINAL)
        .addParameter(
            NameGenerationTool.getTypeNameFromTypeElement(clazz), "instance", Modifier.FINAL)
        .addStatement("return $N.update(id, instance)", lastLayerAttributeName)
        .returns(boolean.class)
        .build();
  }

  public JavaClassFile writeFile(
      TypeElement clazz, TypeName previousLayerClazz, String targetPackage) {
    lastLayerAttributeName = NameGenerationTool.getSimpleNameForAttr(previousLayerClazz);
    final String packageName = targetPackage + ".service";
    final String className = String.format("%sService", clazz.getSimpleName());
    final JavaFile javaFile =
        JavaFile.builder(
                packageName,
                TypeSpec.classBuilder(className)
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Service.class)
                    .addField(
                        previousLayerClazz,
                        lastLayerAttributeName,
                        Modifier.FINAL,
                        Modifier.PRIVATE)
                    .addMethod(writeBuilder(previousLayerClazz))
                    .addMethod(buildGetMethod(clazz))
                    .addMethod(buildUpdateMethod(clazz))
                    .addMethod(buildCreateMethod(clazz))
                    .addMethod(buildDeleteMethod(clazz))
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
