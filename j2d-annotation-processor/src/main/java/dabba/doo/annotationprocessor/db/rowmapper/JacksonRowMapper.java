package dabba.doo.annotationprocessor.db.rowmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JacksonRowMapper {

  public static <T> List<T> mapMultipleRecords(final List<Map> records, final Class<T> clazz) {
    return records.stream()
        .map(r -> clazz.cast(mapSingleRecord(r, clazz)))
        .collect(Collectors.toList());
  }

  public static Object mapSingleRecord(final Map record, final Class<?> clazz) {
    final ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.convertValue(record, clazz);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
