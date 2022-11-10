package dabba.doo.annotationprocessor.db.paramresolver;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dabba.doo.annotationprocessor.core.annotations.entity.J2dColumn;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dabba.doo.annotationprocessor.core.annotations.entity.J2dId;
import dabba.doo.annotationprocessor.core.reflection.ClassReflectionTool;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

/**
 * Parameter map creator
 *
 * @author josue.rojas
 */
public class ParameterMapCreator {

  public static ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Build params map from J2dEntity annotated class
   *
   * @param instance based on J2dEntity annotated class
   * @return map sql parameter source
   */
  public static MapSqlParameterSource buildParamsMap(final Object instance) {
    Map<String, Object> kvObject =
        objectMapper.convertValue(instance, new TypeReference<Map<String, Object>>() {});
    final Map<String, String> tableNames =
        Stream.of(instance.getClass().getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(J2dColumn.class))
            .collect(
                Collectors.toMap(
                        field -> field.getName(),
                        field -> field.getAnnotation(J2dColumn.class).name()
                    ));

    System.out.println("TABLENAMES" + tableNames);
    System.out.println("OBJECT" + kvObject);


    MapSqlParameterSource paramSource = new MapSqlParameterSource()
        .addValues(
            kvObject.entrySet().stream()
                    .filter(kv -> {
                        final Field field = ClassReflectionTool.getFieldWithName(instance.getClass(), kv.getKey());
                        return field.isAnnotationPresent(J2dColumn.class) && !field.isAnnotationPresent(J2dId.class);
                    })
                .collect(Collectors.toMap(kv -> tableNames.get(kv.getKey()), kv -> kv.getValue())));

      System.out.println("PARAMSOURCE: " + paramSource);
    return paramSource;
  }
}
