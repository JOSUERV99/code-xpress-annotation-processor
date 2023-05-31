package dev.tools.annotationprocessor.db.paramresolver;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.tools.annotationprocessor.core.annotations.entity.CXColumn;
import dev.tools.annotationprocessor.core.annotations.entity.CXId;
import dev.tools.annotationprocessor.core.reflection.ClassReflectionTool;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

/**
 * Parameter map creator
 *
 * @author josue.rojas
 */
public class ParameterMapCreator {

  public static ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Build params map from CXEntity annotated class
   *
   * @param instance based on CXEntity annotated class
   * @return map sql parameter source
   */
  public static MapSqlParameterSource buildParamsMap(final Object instance) {
    Map<String, Object> kvObject =
        objectMapper.convertValue(instance, new TypeReference<Map<String, Object>>() {});
    final Map<String, String> tableNames =
        Stream.of(instance.getClass().getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(CXColumn.class))
            .collect(
                Collectors.toMap(
                    field -> field.getName(),
                    field -> field.getAnnotation(CXColumn.class).name()));
    final MapSqlParameterSource paramSource =
        new MapSqlParameterSource()
            .addValues(
                kvObject.entrySet().stream()
                    .filter(
                        kv -> {
                          final Field field =
                              ClassReflectionTool.getFieldWithName(
                                  instance.getClass(), kv.getKey());
                          return field.isAnnotationPresent(CXColumn.class)
                              && !field.isAnnotationPresent(CXId.class);
                        })
                    .collect(
                        Collectors.toMap(kv -> tableNames.get(kv.getKey()), kv -> kv.getValue())));
    return paramSource;
  }
}
