package dabba.doo.annotationprocessor.db.sqlwriter.sentences;

import dabba.doo.annotationprocessor.core.annotations.entity.J2dColumn;
import dabba.doo.annotationprocessor.core.annotations.entity.J2dEntity;
import dabba.doo.annotationprocessor.db.sqlwriter.SqlSentenceWriter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SqlInsertSentenceWriter extends SqlSentenceWriter {

    public SqlInsertSentenceWriter() {
        super();
    }

    @Override
    public <T> void validateEntityContract(final Class<T> clazz) {
        super.validateEntityContract(clazz);
    }

    @Override
    public <T> String write(final Class<T> clazz)
    {
        validateEntityContract(clazz);

        final StringBuilder insertStringBuilder = new StringBuilder("insert into ").append(clazz.getAnnotation(J2dEntity.class).tableName()).append("(");
        final StringBuilder valuesStringBuilder = new StringBuilder("values(");

        // add insert columns
        final List<String> fields =
                Arrays.stream(clazz.getDeclaredFields())
                        .filter(field -> field.isAnnotationPresent(J2dColumn.class))
                        .map(field -> field.getAnnotation(J2dColumn.class).name().toUpperCase())
                        .collect(Collectors.toList());

        for (int i = 0; i < fields.size(); i++) {
            final String columnName = fields.get(i);
            insertStringBuilder.append(columnName).append((i < fields.size() - 1 ? ", " : ")"));
            valuesStringBuilder.append(":").append(columnName).append((i < fields.size() - 1 ? ", " : ")"));
        }

        return insertStringBuilder.append(" ").append(valuesStringBuilder.toString()).toString();
    }

}
