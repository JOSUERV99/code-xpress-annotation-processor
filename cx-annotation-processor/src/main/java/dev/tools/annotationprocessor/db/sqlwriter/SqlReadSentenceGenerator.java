package dev.tools.annotationprocessor.db.sqlwriter;

import dev.tools.annotationprocessor.core.annotations.entity.CXColumn;
import dev.tools.annotationprocessor.core.reflection.ClassReflectionTool;
import dev.tools.annotationprocessor.core.reflection.NameGenerationTool;
import java.util.Map;
import java.util.stream.Collectors;
import javax.lang.model.element.TypeElement;

/**
 * SqlReadSentenceGenerator class Util that generates sql sentences to read data from database
 *
 * @author josue.rojas
 */
public class SqlReadSentenceGenerator {

  /**
   * Write select with all columns
   *
   * @param clazz CXEntity annotated class
   * @return select query as string
   */
  public static String writeSelectSentence(final TypeElement clazz) {
    final Map<String, String> columnNames =
        ClassReflectionTool.getDeclaredFields(clazz).stream()
            .filter(field -> field.getAnnotation(CXColumn.class) != null)
            .collect(
                Collectors.toMap(
                    field -> field.getSimpleName().toString(),
                    field -> field.getAnnotation(CXColumn.class).name()));

    final StringBuilder selectSentence = new StringBuilder().append("select ");

    selectSentence.append(
        columnNames.entrySet().stream()
            .map(entry -> String.format("t.`%s` as `%s`", entry.getValue(), entry.getKey()))
            .collect(Collectors.joining(",")));

    return selectSentence
        .append(" from ")
        .append(NameGenerationTool.getTableName(clazz))
        .append(" t")
        .toString();
  }

  /**
   * Write select sentence by id
   *
   * @param clazz CXEntity annotated class
   * @return select query as string for id
   */
  public static String writeSelectSentenceById(TypeElement clazz) {
    final String idFieldAsString =
        ClassReflectionTool.getIdField(clazz).getAnnotation(CXColumn.class).name();
    return "select * from "
        + NameGenerationTool.getTableName(clazz)
        + " where "
        + idFieldAsString
        + "= :"
        + idFieldAsString;
  }
}
