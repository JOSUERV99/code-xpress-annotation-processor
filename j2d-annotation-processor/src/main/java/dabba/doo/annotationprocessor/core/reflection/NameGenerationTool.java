package dabba.doo.annotationprocessor.core.reflection;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import dabba.doo.annotationprocessor.core.annotations.entity.J2dEntity;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

public class NameGenerationTool {

  public static TypeName getTypeNameFromTypeElement(TypeElement typeElement) {
    return ClassName.get(typeElement);
  }

  public static String getTableName(TypeElement clazz) {
    return clazz.getAnnotation(J2dEntity.class).tableName();
  }

  public static String getTableName(final Class<?> clazz) {
    return clazz.getAnnotation(J2dEntity.class).tableName();
  }

  public static String getPackageName(Element element) {
    return Arrays.stream(element.toString().split("\\."))
        .filter(part -> part.matches("[a-z_]+"))
        .collect(Collectors.joining("."));
  }

  public static String getSimpleClassName(Element element) {
    return Arrays.stream(element.toString().split("\\."))
        .filter(part -> part.matches("[A-Z][a-z_]+"))
        .findFirst()
        .get();
  }

  public static String getSimpleNameForAttr(TypeName element) {
    final String simpleName =
        Arrays.stream(element.toString().split("\\."))
            .filter(part -> part.matches("[A-Z][a-zA-Z]+"))
            .findFirst()
            .get();
    return ("" + simpleName.charAt(0)).toLowerCase() + simpleName.substring(1);
  }

  public static TypeName getTypeNameForTemplateList(final TypeElement clazz) {
    ClassName clazzType = ClassName.get(clazz);
    ClassName list = ClassName.get("java.util", "List");
    TypeName instanceList = ParameterizedTypeName.get(list, clazzType);

    return instanceList;
  }

  public static TypeName getTypeNameForId(TypeElement parentClass) {
    return TypeName.get(ClassReflectionTool.getIdField(parentClass).asType());
  }
}
