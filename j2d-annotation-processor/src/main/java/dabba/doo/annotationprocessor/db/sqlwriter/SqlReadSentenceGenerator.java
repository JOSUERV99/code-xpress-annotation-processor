package dabba.doo.annotationprocessor.db.sqlwriter;

import dabba.doo.annotationprocessor.core.annotations.entity.J2dColumn;
import dabba.doo.annotationprocessor.core.reflection.ClassReflectionTool;
import dabba.doo.annotationprocessor.core.reflection.NameGenerationTool;
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
   * @param clazz J2dEntity annotated class
   * @return select query as string
   */
  public static String writeSelectSentence(final TypeElement clazz) {
    final Map<String, String> columnNames =
        ClassReflectionTool.getDeclaredFields(clazz).stream()
            .filter(field -> field.getAnnotation(J2dColumn.class) != null)
            .collect(
                Collectors.toMap(
                    field -> field.getSimpleName().toString(),
                    field -> field.getAnnotation(J2dColumn.class).name()));

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
   * @param clazz J2dEntity annotated class
   * @return select query as string for id
   */
  public static String writeSelectSentenceById(TypeElement clazz) {
    final String idFieldAsString =
        ClassReflectionTool.getIdField(clazz).getAnnotation(J2dColumn.class).name();
    return "select * from "
        + NameGenerationTool.getTableName(clazz)
        + " where "
        + idFieldAsString
        + "= :"
        + idFieldAsString;
  }
}
