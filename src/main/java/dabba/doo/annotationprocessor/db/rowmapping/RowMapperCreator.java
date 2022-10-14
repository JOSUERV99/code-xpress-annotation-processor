package dabba.doo.annotationprocessor.db.rowmapping;

import dabba.doo.annotationprocessor.core.reflection.ClassReflectionTool;

import javax.swing.tree.RowMapper;
import java.sql.ResultSet;
import java.util.Map;
import java.util.function.BiFunction;

public class RowMapperCreator<T> {

    /*
        T mapRow(ResultSet rs, int rowNum)
    */
    public static <T> BiFunction<ResultSet, Integer, T> buildRowMapperFunction(final Class<T> clazz) {
        final Map<String, Class<?>> attributesMap = ClassReflectionTool.getAttributesMapFromClass(clazz);
        attributesMap.entrySet().stream().forEach(
                nameAndType -> {

                }
        );
        return null;
    }


}
