package dev.tools.annotationprocessor.core.writer.spring.layers;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import dev.tools.annotationprocessor.core.reflection.NameGenerationTool;
import dev.tools.annotationprocessor.core.writer.JavaClassFile;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Spring service class writer
 *
 * @author josue.rojas
 */
public class SpringServiceClassWriter {

  private String lastLayerAttributeName = "repository";

  /**
   * Builder method
   *
   * @param previousClassLayer repository class generated type name
   * @return builder method for service class
   */
  public MethodSpec writeBuilder(TypeName previousClassLayer) {
    return MethodSpec.constructorBuilder()
        .addAnnotation(Autowired.class)
        .addModifiers(Modifier.PUBLIC)
        .addParameter(previousClassLayer, lastLayerAttributeName, Modifier.FINAL)
        .addStatement("this.$N = $N", lastLayerAttributeName, lastLayerAttributeName)
        .build();
  }

  /**
   * Creator method
   *
   * @param clazz CXEntity annotated class
   * @return create method for service class
   */
  public MethodSpec buildCreateMethod(final TypeElement clazz) {
    return MethodSpec.methodBuilder("create")
        .addModifiers(Modifier.PUBLIC)
        .addParameter(
            NameGenerationTool.getTypeNameFromTypeElement(clazz), "instance", Modifier.FINAL)
        .addStatement("return $N.create(instance)", lastLayerAttributeName)
        .returns(boolean.class)
        .build();
  }

  /**
   * Get method
   *
   * @param clazz CXEntity annotated class
   * @return get method for service class
   */
  public MethodSpec buildGetMethod(final TypeElement clazz) {
    return MethodSpec.methodBuilder("get")
        .addModifiers(Modifier.PUBLIC)
        .addStatement("return $N.get()", lastLayerAttributeName)
        .returns(NameGenerationTool.getTypeNameForTemplateList(clazz))
        .build();
  }

  /**
   * Delete method
   *
   * @param clazz CXEntity annotated class
   * @return delete method for service class
   */
  public MethodSpec buildDeleteMethod(final TypeElement clazz) {
    return MethodSpec.methodBuilder("delete")
        .addModifiers(Modifier.PUBLIC)
        .addParameter(NameGenerationTool.getTypeNameForId(clazz), "id", Modifier.FINAL)
        .addStatement("return $N.delete(id)", lastLayerAttributeName)
        .returns(boolean.class)
        .build();
  }

  /**
   * Update method
   *
   * @param clazz CXEntity annotated class
   * @return update method for service class
   */
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

  /**
   * Write class for Service layer
   *
   * @param clazz CXEntity annotated class name
   * @param previousLayerClazz repository generated class type name
   * @param targetPackage package name from entity package name
   * @return JavaClassFile for service generated class
   */
  public JavaClassFile writeFile(
      final TypeElement clazz, final TypeName previousLayerClazz, final String targetPackage) {
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
