package dabba.doo.annotationprocessor.core.reflection;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import dabba.doo.annotationprocessor.core.annotations.entity.J2dEntity;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

/**
 * Type, TypeName, ClassName handling from annotation processor elements
 *
 * @author josue.rojas
 */
public class NameGenerationTool {

  /**
   * Get type name from type element
   *
   * @param typeElement annotation processing element
   * @return ClassName for type element
   */
  public static TypeName getTypeNameFromTypeElement(TypeElement typeElement) {

    return ClassName.get(typeElement);
  }

  /**
   * Get table name from J2DEntity
   *
   * @param clazz annotated type element with J2dEntity
   * @return path
   */
  public static String getTableName(TypeElement clazz) {
    return clazz.getAnnotation(J2dEntity.class).tableName();
  }

  /**
   * Get table name from J2DEntity
   *
   * @param clazz annotated class with J2dEntity
   * @return path
   */
  public static String getTableName(final Class<?> clazz) {
    return clazz.getAnnotation(J2dEntity.class).tableName();
  }

  /**
   * Get simple package name
   *
   * @param element element string like pa.ck.ag.e.Class
   * @return pa.ck.ag.e
   */
  public static String getPackageName(Element element) {
    return Arrays.stream(element.toString().split("\\."))
        .filter(part -> part.matches("[a-z_]+"))
        .collect(Collectors.joining("."));
  }

  /**
   * Get simple name for attribute
   *
   * @param element type name
   * @return return class name
   */
  public static String getSimpleNameForAttr(TypeName element) {
    final String simpleName =
        Arrays.stream(element.toString().split("\\."))
            .filter(part -> part.matches("[A-Z][a-zA-Z]+"))
            .findFirst()
            .get();
    return ("" + simpleName.charAt(0)).toLowerCase() + simpleName.substring(1);
  }

  /**
   * Get type name for template list
   *
   * @param clazz type element from annotation processing
   * @return typename for template list
   */
  public static TypeName getTypeNameForTemplateList(final TypeElement clazz) {
    ClassName clazzType = ClassName.get(clazz);
    ClassName list = ClassName.get("java.util", "List");
    TypeName instanceList = ParameterizedTypeName.get(list, clazzType);
    return instanceList;
  }

  /**
   * Get type name for id
   *
   * @param parentClass parent type element from annotation processing
   * @return type name for id field from parent
   */
  public static TypeName getTypeNameForId(TypeElement parentClass) {
    return TypeName.get(ClassReflectionTool.getIdField(parentClass).asType());
  }
}
