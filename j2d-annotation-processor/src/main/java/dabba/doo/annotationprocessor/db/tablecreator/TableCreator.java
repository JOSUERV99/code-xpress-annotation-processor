package dabba.doo.annotationprocessor.db.tablecreator;

import com.squareup.javapoet.TypeName;
import dabba.doo.annotationprocessor.core.annotations.entity.types.BigIntColumn;
import dabba.doo.annotationprocessor.core.annotations.entity.types.DecimalColumn;
import dabba.doo.annotationprocessor.core.annotations.entity.types.IntColumn;
import dabba.doo.annotationprocessor.core.annotations.entity.types.VarcharColumn;
import dabba.doo.annotationprocessor.core.reflection.ClassReflectionTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TableCreator {

    @Autowired
    public static NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public static Boolean createTableIfNotExists(final Class<?> clazz) {
        final String creationScript = createQueryBasedOnType(clazz);
        System.out.println(creationScript);
        final String tableExistenceScript = getTableExistenceScript(ClassReflectionTool.getTableName(clazz));
        System.out.println(tableExistenceScript);
        System.out.println("namedjdbctemplate " + namedParameterJdbcTemplate);
        return true;
    }

    private static final String getTableExistenceScript(final String tableName) {
        return "SELECT 1 FROM `" + tableName + "`;";
    }

    private static String createQueryBasedOnType(final Class<?> clazz)
    {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE `").append(ClassReflectionTool.getTableName(clazz)).append("` (\n");

        final List<Field> fields = ClassReflectionTool.getDeclaredFieldsByType(clazz);
        final String fieldDefinitionString = fields.stream().map(field -> {
            final StringBuilder fieldStr = new StringBuilder();
            fieldStr.append("`").append(field.getName()).append("` ");
            fieldStr.append(getMySqlTypeBasedOnJavaType(clazz, field)).append(" ");
            // TODO: add not null constraint
            fieldStr.append("COMMENT '").append(clazz.getSimpleName()).append("->").append(field.getName()).append(" J2D Annotation Processor field'");
            return fieldStr.toString();
        }).collect(Collectors.joining(",\n"));

        stringBuilder.append(fieldDefinitionString).append(",\n").append("PRIMARY KEY (").append("`").append(ClassReflectionTool.getIdField(clazz).getName()).append("`)");
        return stringBuilder.append(") ROW_FORMAT=COMPRESSED COMMENT='").append(clazz).append(" J2D Annotation processor generated table").append("';").toString();
    }

    private static String getMySqlTypeBasedOnJavaType(final Class<?> clazz, final Field field) {
        final TypeName type = TypeName.get(field.getType());
        try {
            if (type.equals(TypeName.get(Date.class)))
                return "date";
            else if (type.equals(TypeName.get(Boolean.class)) || type.equals(TypeName.get(boolean.class)))
                return "tinyint(1)";
            else if (type.equals(TypeName.get(Integer.class)) || type.equals(TypeName.get(int.class)))
                return "int(" + field.getAnnotation(IntColumn.class).length() + ")";
            else if (type.equals(TypeName.get(Long.class)) || type.equals(TypeName.get(long.class)))
                return "bigint(" + field.getAnnotation(BigIntColumn.class).length() + ")";
            else if (type.equals(TypeName.get(String.class)))
                return "varchar(" + field.getAnnotation(VarcharColumn.class).length() + ")";
            else if (type.equals(TypeName.get(Float.class)) || type.equals(TypeName.get(float.class)))
                return "decimal(" + field.getAnnotation(DecimalColumn.class).max() + "," + field.getAnnotation(DecimalColumn.class).precision() + ")";
        } catch (NullPointerException e) {
            throw new IllegalStateException(clazz.getName() + " must be annotated with @*Column annotation for database type definition "+ e);
        }
        throw new IllegalStateException(clazz.getName() + "=>" + field.getName() + " type not supported yet");
    }

}
