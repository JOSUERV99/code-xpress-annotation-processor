package dabba.doo.annotationprocessor.core.reflection;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClassReflectionTool {

    public static <T> Map<String, Class<?>> getAttributesMapFromClass(final Class<T> clazz) {
        return Stream.of(clazz.getDeclaredFields()).collect(Collectors.toMap(Field::getName, Field::getType));
    }

}
