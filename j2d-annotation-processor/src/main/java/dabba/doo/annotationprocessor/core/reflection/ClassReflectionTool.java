package dabba.doo.annotationprocessor.core.reflection;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import dabba.doo.annotationprocessor.core.annotations.J2dSpringRestCrudApi;
import dabba.doo.annotationprocessor.core.annotations.entity.J2dColumn;
import dabba.doo.annotationprocessor.core.annotations.entity.J2dEntity;
import dabba.doo.annotationprocessor.core.annotations.entity.J2dId;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ClassReflectionTool class
 * Util class to play with reflection and annotation processor elements
 * @author josue.rojas
 */
public class ClassReflectionTool {

    public static List<? extends Element> getDeclaredFieldsByType(TypeElement typeElement) {
        return typeElement.getEnclosedElements().stream()
                .filter(e -> ElementKind.FIELD.equals(e.getKind()))
                .collect(Collectors.toList());
    }

    public static List<Field> getDeclaredFieldsByType(Class<?> clazz) {
        return Arrays.asList(clazz.getDeclaredFields());
    }

    public static Field getIdField(Class<?> clazz) {

        final Optional<Field> idFieldOptional = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(J2dColumn.class) && field.isAnnotationPresent(J2dId.class))
                .findFirst();

        if (!idFieldOptional.isPresent()) {
            throw new IllegalStateException("Id field must be annotated with @J2dId for " + clazz.getName() + " class");
        }

        return idFieldOptional.get();
    }

    public static Element getIdField(TypeElement clazz) {

        final Optional<? extends Element> idFieldOptional = ClassReflectionTool.getDeclaredFieldsByType(clazz).stream()
                .filter(field -> field.getAnnotation(J2dColumn.class) != null && field.getAnnotation(J2dId.class) != null)
                .findFirst();

        if (!idFieldOptional.isPresent()) {
            throw new IllegalStateException("Id field must be annotated with @J2dId for " + clazz.getSimpleName() + " class");
        }

        return idFieldOptional.get();
    }

    public static TypeName getTypeNameForId(TypeElement parentClass) {
        return TypeName.get(getIdField(parentClass).asType());
    }

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
        return Arrays.stream(element.toString().split("\\.")).filter(part -> part.matches("[a-z_]+")).collect(Collectors.joining("."));
    }

    public static String getSimpleClassName(Element element) {
        return Arrays.stream(element.toString().split("\\.")).filter(part -> part.matches("[A-Z][a-z_]+")).findFirst().get();
    }

    public static String getSimpleNameForAttr(TypeName element) {
        final String simpleName =  Arrays.stream(element.toString().split("\\.")).filter(part -> part.matches("[A-Z][a-zA-Z]+")).findFirst().get();
        return ("" + simpleName.charAt(0)).toLowerCase() + simpleName.substring(1);
    }

    public static TypeName getTypeNameForTemplateList(final TypeElement clazz) {
        ClassName clazzType = ClassName.get(clazz);
        ClassName list = ClassName.get("java.util", "List");
        TypeName instanceList = ParameterizedTypeName.get(list, clazzType);

        return instanceList;
    }

    public static String getPathFromMainApiAnnotation(TypeElement clazz, Class<?> annotationClass) {

        if (J2dSpringRestCrudApi.class.equals(annotationClass)) {
            return clazz.getAnnotation(J2dSpringRestCrudApi.class).path();
        }

        throw new IllegalStateException("No path fetched from annotation " + annotationClass.getName());
    }

    public static String getPathFromMainApiAnnotation(TypeElement clazz, Class<?> annotationClass, String paramName) {

        if (J2dSpringRestCrudApi.class.equals(annotationClass)) {
            return String.format("%s/{%s}", clazz.getAnnotation(J2dSpringRestCrudApi.class).path(), paramName);
        }

        throw new IllegalStateException("No path fetched from annotation " + annotationClass.getName());
    }

    public static ClassName getClassNameFromClassName(String packageName, String simpleName) {
        return ClassName.get(packageName, simpleName);
    }
}
