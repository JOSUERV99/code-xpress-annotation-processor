package dabba.doo.annotationprocessor.db.sqlwriter;

import dabba.doo.annotationprocessor.core.annotations.entity.J2dColumn;
import dabba.doo.annotationprocessor.core.annotations.entity.J2dEntity;
import org.apache.http.util.Asserts;

import java.util.Arrays;

public abstract class SqlSentenceWriter {

    public <T> void validateEntityContract(final Class<T> clazz) {

        if (!clazz.isAnnotationPresent(J2dEntity.class)) {
            return;
        }

        Asserts.check(
                Arrays.stream(
                        clazz.getDeclaredFields()
                ).anyMatch(field -> field.isAnnotationPresent(J2dColumn.class)),
                "Every J2D class definition, at least must have one attribute annotated with @J2dColumn");
    }

    public abstract <T> String write(final Class<T> clazz);
}
