package dabba.doo.annotationprocessor.db.sqlwriter.sentences;

import dabba.doo.annotationprocessor.core.annotations.entity.J2dColumn;
import dabba.doo.annotationprocessor.core.annotations.entity.J2dEntity;
import dabba.doo.annotationprocessor.core.annotations.entity.J2dId;
import dabba.doo.annotationprocessor.db.sqlwriter.SqlSentenceWriter;
import org.apache.http.util.Asserts;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SqlDeleteSentenceWriter extends SqlSentenceWriter {

    public SqlDeleteSentenceWriter() {
        super();
    }

    @Override
    public <T> void validateEntityContract(final Class<T> clazz) {
        super.validateEntityContract(clazz);

        Asserts.check(
                Arrays.stream(
                        clazz.getDeclaredFields()
                ).anyMatch(field -> field.isAnnotationPresent(J2dId.class) && field.isAnnotationPresent(J2dColumn.class)), "At least one of the attribute must have @J2dId annotation in order to write sql delete sentences");
    }

    @Override
    public <T> String write(Class<T> clazz) {
        validateEntityContract(clazz);

        final StringBuilder deleteSentenceStringBuilder = new StringBuilder("delete from ").append(clazz.getAnnotation(J2dEntity.class).tableName());
        final StringBuilder whereStringBuilder = new StringBuilder("where ");

        final String whereSentence = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(J2dId.class) && field.isAnnotationPresent(J2dColumn.class))
                .map(field -> field.getAnnotation(J2dColumn.class).name().toUpperCase())
                .map(field -> new StringBuilder(field).append("=:").append(field).toString())
                .collect(Collectors.joining(","));

        return deleteSentenceStringBuilder.append(" where ").append(whereSentence).toString();
    }
}
