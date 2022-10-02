package dabba.doo.annotationprocessor.db.executer;

import java.util.List;
import java.util.Map;

public class NamedJdbcRunner {

    public NamedJdbcRunner() { }

    public static <T> List<T> select(final String selectSqlStatement, final Map<String, Object> params, final Class<T> clazz) {
        return (List<T>) JdbcTemplateHolder.getConnection().queryForList(selectSqlStatement, params, clazz.getClass());
    }


}
