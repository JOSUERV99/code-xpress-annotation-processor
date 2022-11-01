package dabba.doo.annotationprocessor.db.sqlwriter;

import dabba.doo.annotationprocessor.core.annotations.entity.J2dColumn;
import dabba.doo.annotationprocessor.core.reflection.ClassReflectionTool;

import javax.lang.model.element.TypeElement;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * SqlReadSentenceGenerator class
 * Util that generates sql sentences to read data from database
 * @author josue.rojas
 */
public class SqlReadSentenceGenerator {

    public static String writeSelectSentence(final Class<?> clazz) {

        final Map<String, String> columnNames = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(J2dColumn.class))
                .collect(
                        Collectors.toMap(
                                field -> field.getName(),
                                field -> field.getAnnotation(J2dColumn.class).name()));

        final StringBuilder selectSentence = new StringBuilder().append("select ");

        selectSentence.append(
                columnNames.entrySet().stream()
                        .map(entry -> entry.getValue() + " as " + entry.getKey())
                        .collect(Collectors.joining(",")));

        return selectSentence.append(" from ").append(ClassReflectionTool.getTableName(clazz)).toString();
    }

    public static String writeSelectSentence(final TypeElement clazz) {

        final Map<String, String> columnNames = ClassReflectionTool.getDeclaredFieldsByType(clazz).stream()
                .filter(field -> field.getAnnotation(J2dColumn.class) != null)
                .collect(
                        Collectors.toMap(
                                field -> field.getSimpleName().toString(),
                                field -> field.getAnnotation(J2dColumn.class).name()));

        final StringBuilder selectSentence = new StringBuilder().append("select ");

        selectSentence.append(
                columnNames.entrySet().stream()
                        .map(entry -> entry.getValue() + " as " + entry.getKey())
                        .collect(Collectors.joining(",")));

        return selectSentence.append(" from ").append(ClassReflectionTool.getTableName(clazz)).toString();
    }

    public static String writeSelectSentenceById(final Class<?> clazz) {
        final String idFieldAsString = ClassReflectionTool.getIdField(clazz).getAnnotation(J2dColumn.class).name();
        return "select * from " + ClassReflectionTool.getTableName(clazz) + " where " + idFieldAsString + "= :" + idFieldAsString;
    }

    public static String writeSelectSentenceById(TypeElement clazz) {
        final String idFieldAsString = ClassReflectionTool.getIdField(clazz).getAnnotation(J2dColumn.class).name();
        return "select * from " + ClassReflectionTool.getTableName(clazz) + " where " + idFieldAsString + "= :" + idFieldAsString;
    }
}
