package dabba.doo.annotationprocessor.db.paramresolver;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dabba.doo.annotationprocessor.core.annotations.entity.J2dColumn;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParameterMapCreator {

    public ObjectMapper objectMapper = new ObjectMapper();

    public <T> MapSqlParameterSource buildParamsMap(final T instance) {
        final Map<String, Object> kvObject = objectMapper.convertValue(instance, new TypeReference<Map<String, Object>>() {});
        final Map<String, String> tableNames =
                Stream.of(instance.getClass().getDeclaredFields())
                        .filter(field -> field.isAnnotationPresent(J2dColumn.class))
                        .collect(
                                Collectors.toMap(
                                        field -> field.getName(),
                                        field -> field.getAnnotation(J2dColumn.class).name())
                        );
        return new MapSqlParameterSource()
                .addValues(kvObject.entrySet().stream().collect(
                        Collectors.toMap(
                                kv -> tableNames.get(kv.getKey()),
                                kv-> kv.getValue()
                        )
                ));
    }

}
