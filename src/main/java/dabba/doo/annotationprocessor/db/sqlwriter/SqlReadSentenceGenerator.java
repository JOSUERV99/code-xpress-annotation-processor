package dabba.doo.annotationprocessor.db.sqlwriter;

import dabba.doo.annotationprocessor.core.annotations.entity.J2dColumn;
import dabba.doo.annotationprocessor.core.annotations.entity.J2dEntity;
import dabba.doo.annotationprocessor.core.reflection.ClassReflectionTool;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class SqlReadSentenceGenerator {

    public static <T> String writeSelectSentence(final Class<?> clazz) {

        final Map<String, String> columnNames = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(J2dColumn.class))
                .collect(
                        Collectors.toMap(
                                field -> field.getName(),
                                field -> field.getAnnotation(J2dColumn.class).name().toUpperCase()));

        final StringBuilder selectSentence = new StringBuilder().append("select ");

        selectSentence.append(
                columnNames.entrySet().stream()
                        .map(entry -> entry.getValue() + " as " + entry.getKey())
                        .collect(Collectors.joining(",")));

        return selectSentence.append(" from ").append(ClassReflectionTool.getTableName(clazz)).toString();
    }

    public static <T> String writeSelectSentenceById(final Class<?> clazz) {
        final String idFieldAsString = ClassReflectionTool.getIdField(clazz).getAnnotation(J2dColumn.class).name();
        return "select * from " + ClassReflectionTool.getTableName(clazz) + " where " + idFieldAsString + "= :" + idFieldAsString;
    }

}
