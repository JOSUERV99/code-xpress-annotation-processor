package dabba.doo.annotationprocessor.db.executer;

import javax.sql.DataSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class JdbcTemplateHolder {

  private static NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  private JdbcTemplateHolder() {}

  public static NamedParameterJdbcTemplate getConnection() {
    if (namedParameterJdbcTemplate == null)
      namedParameterJdbcTemplate = namedParameterJdbcTemplate(dataSource());
    return namedParameterJdbcTemplate;
  }

  private static DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("com.mysql.jdbc.Driver");
    dataSource.setUrl("jdbc:mysql://localhost:3306/l2gs");
    dataSource.setUsername("root");
    dataSource.setPassword("root");
    return dataSource;
  }

  private static NamedParameterJdbcTemplate namedParameterJdbcTemplate(
      final DataSource dataSource) {
    return new NamedParameterJdbcTemplate(dataSource);
  }
}
