package dabba.doo.annotationprocessor.db.sqlwriter.sentences;

import dabba.doo.annotationprocessor.core.annotations.entity.J2dColumn;
import dabba.doo.annotationprocessor.core.annotations.entity.J2dEntity;
import dabba.doo.annotationprocessor.db.sqlwriter.SqlSentenceWriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SqlISelectSentenceWriter extends SqlSentenceWriter {

    public SqlISelectSentenceWriter() {
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

        final StringBuilder selectStringBuilder = new StringBuilder("select ");

        // add insert columns
        final Map<String, Object> fieldsMap =
                Arrays.stream(clazz.getDeclaredFields())
                        .filter(field -> field.isAnnotationPresent(J2dColumn.class))
                        .collect(Collectors.toMap(
                                field -> field.getName(),
                                field -> field.getAnnotation(J2dColumn.class).name().toUpperCase()));

        final String[] fields = fieldsMap.keySet().toArray(new String[0]);
        for (int i = 0; i < fields.length; i++) {
            final String field = fields[i];
            selectStringBuilder
                    .append(fieldsMap.get(field))
                    .append(" as '")
                    .append(field)
                    .append("'")
                    .append(i < fields.length - 1 ? ", " : "");
        }

        return selectStringBuilder.append(" from ").append(clazz.getAnnotation(J2dEntity.class).tableName()).toString();
    }

}
