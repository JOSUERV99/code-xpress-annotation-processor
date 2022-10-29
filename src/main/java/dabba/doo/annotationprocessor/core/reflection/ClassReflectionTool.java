package dabba.doo.annotationprocessor.core.reflection;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import dabba.doo.annotationprocessor.core.annotations.entity.J2dColumn;
import dabba.doo.annotationprocessor.core.annotations.entity.J2dEntity;
import dabba.doo.annotationprocessor.core.annotations.entity.J2dId;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClassReflectionTool {

    public static <T> Map<String, Class<?>> getAttributesMapFromClass(final Class<T> clazz) {
        return Stream.of(clazz.getDeclaredFields()).collect(Collectors.toMap(Field::getName, Field::getType));
    }

    public static Field getIdField(Class<?> clazz) {

        System.out.println(Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(J2dColumn.class) && field.isAnnotationPresent(J2dId.class))
                .collect(Collectors.toList()));

        final Optional<Field> idFieldOptional = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(J2dColumn.class) && field.isAnnotationPresent(J2dId.class))
                .findFirst();

        if (!idFieldOptional.isPresent()) {
            throw new IllegalStateException("Id field must be annotated with @J2dId for " + clazz.getName() + " class");
        }

        return idFieldOptional.get();
    }

    public static String getTableName(Class<?> clazz) {
        return clazz.getAnnotation(J2dEntity.class).tableName();
    }

    public static <T> TypeName getTypeNameForTemplateList(final Class<T> clazz) {
        ClassName clazzType = ClassName.get(clazz);
        ClassName list = ClassName.get("java.util", "List");
        TypeName instanceList = ParameterizedTypeName.get(list, clazzType);
        return instanceList;
    }

}
