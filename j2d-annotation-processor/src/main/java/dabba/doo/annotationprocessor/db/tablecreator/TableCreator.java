package dabba.doo.annotationprocessor.db.tablecreator;

import com.squareup.javapoet.TypeName;
import dabba.doo.annotationprocessor.core.annotations.entity.J2dColumn;
import dabba.doo.annotationprocessor.core.annotations.entity.J2dId;
import dabba.doo.annotationprocessor.core.annotations.entity.types.BigIntColumn;
import dabba.doo.annotationprocessor.core.annotations.entity.types.DecimalColumn;
import dabba.doo.annotationprocessor.core.annotations.entity.types.IntColumn;
import dabba.doo.annotationprocessor.core.annotations.entity.types.VarcharColumn;
import dabba.doo.annotationprocessor.core.reflection.ClassReflectionTool;
import dabba.doo.annotationprocessor.core.reflection.NameGenerationTool;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * Table creator for entities
 *
 * @author josue.rojas
 */
public class TableCreator {

  public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public TableCreator(final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  /**
   * Create table if not exists in current db connection
   *
   * @param clazz J2dEntity annotated class
   * @return true if the table created correctly, false otherwise
   */
  public Boolean createTableIfNotExists(final Class<?> clazz) {
    final String creationScript = createQueryBasedOnType(clazz);
    final String tableExistenceScript =
        getTableExistenceScript(NameGenerationTool.getTableName(clazz));
    try {
      namedParameterJdbcTemplate.queryForObject(
          tableExistenceScript, new MapSqlParameterSource(), Integer.class);
    } catch (Exception e) {
      if (e.toString().matches("already exists")) {
        System.out.println(
                "J2D-Annotation-Processor: table '"
                        + NameGenerationTool.getTableName(clazz)
                        + "' already created");
        return false;
      }
      try {
        namedParameterJdbcTemplate.execute(
            creationScript,
            (PreparedStatement preparedStatement) -> preparedStatement.executeUpdate());
      } catch (BadSqlGrammarException ex) {
        System.out.println(
            "J2D-Annotation-Processor: table '"
                + NameGenerationTool.getTableName(clazz)
                + "' generated correctly");
        return true;
      }
    }
    return false;
  }

  private final String getTableExistenceScript(final String tableName) {
    return "SHOW TABLE LIKE `" + tableName + "`;";
  }

  /**
   * Create query based on type for table creation
   *
   * @param clazz J2dEntity annotated class
   * @return
   */
  private String createQueryBasedOnType(final Class<?> clazz) {
    final StringBuilder stringBuilder = new StringBuilder();
    stringBuilder
        .append("CREATE TABLE `")
        .append(NameGenerationTool.getTableName(clazz))
        .append("` (\n");

    final List<Field> fields = ClassReflectionTool.getDeclaredFields(clazz);
    final String fieldDefinitionString =
        fields.stream()
            .map(
                field -> {
                  final StringBuilder fieldStr = new StringBuilder();
                  fieldStr
                      .append("`")
                      .append(field.getAnnotation(J2dColumn.class).name())
                      .append("` ");
                  fieldStr.append(getMySqlTypeBasedOnJavaType(clazz, field)).append(" ");
                  // TODO: add not null constraint
                  if (field.isAnnotationPresent(J2dId.class)) fieldStr.append("AUTO_INCREMENT ");
                  fieldStr
                      .append("COMMENT '")
                      .append(clazz.getSimpleName())
                      .append("->")
                      .append(field.getName())
                      .append(" J2D Annotation Processor field'");
                  return fieldStr.toString();
                })
            .collect(Collectors.joining(",\n"));

    stringBuilder
        .append(fieldDefinitionString)
        .append(",\n")
        .append("PRIMARY KEY (")
        .append("`")
        .append(ClassReflectionTool.getIdField(clazz).getName())
        .append("`)");
    return stringBuilder
        .append(") ROW_FORMAT=COMPRESSED COMMENT='")
        .append(clazz)
        .append(" J2D Annotation processor generated table")
        .append("';")
        .toString();
  }

  /**
   * Get Mysql datatype on java class
   *
   * @param clazz J2dEntity annotated class
   * @param field field to pass type to mysql datatype
   * @return mysql data type
   */
  private String getMySqlTypeBasedOnJavaType(final Class<?> clazz, final Field field) {
    final TypeName type = TypeName.get(field.getType());
    try {
      if (type.equals(TypeName.get(Date.class))) return "date";
      else if (type.equals(TypeName.get(Boolean.class)) || type.equals(TypeName.get(boolean.class)))
        return "tinyint(1)";
      else if (type.equals(TypeName.get(Integer.class)) || type.equals(TypeName.get(int.class)))
        return "int(" + field.getAnnotation(IntColumn.class).length() + ")";
      else if (type.equals(TypeName.get(Long.class)) || type.equals(TypeName.get(long.class)))
        return "bigint(" + field.getAnnotation(BigIntColumn.class).length() + ")";
      else if (type.equals(TypeName.get(String.class)))
        return "varchar(" + field.getAnnotation(VarcharColumn.class).length() + ")";
      else if (type.equals(TypeName.get(Float.class)) || type.equals(TypeName.get(float.class)))
        return "decimal("
            + field.getAnnotation(DecimalColumn.class).max()
            + ","
            + field.getAnnotation(DecimalColumn.class).precision()
            + ")";
    } catch (NullPointerException e) {
      throw new IllegalStateException(
          clazz.getName()
              + " must be annotated with @*Column annotation for database type definition "
              + e);
    }
    throw new IllegalStateException(
        clazz.getName() + "=>" + field.getName() + " type not supported yet");
  }
}
