package dabba.doo.annotationprocessor.db;

import dabba.doo.annotationprocessor.db.paramresolver.ParameterMapCreator;
import dabba.doo.annotationprocessor.db.sqlwriter.SqlWriteSentenceGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SpringRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SqlWriteSentenceGenerator sqlSentenceWriter;
    private final ParameterMapCreator parameterMapCreator;

    @Autowired
    public SpringRepository(
            final NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            final SqlWriteSentenceGenerator sqlSentenceWriter,
            final ParameterMapCreator parameterMapCreator) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.sqlSentenceWriter = sqlSentenceWriter;
        this.parameterMapCreator = parameterMapCreator;
    }

    public boolean create(final Pojo instance) {
        return namedParameterJdbcTemplate.update(
                sqlSentenceWriter.writeInsertSentence(instance.getClass()),
                parameterMapCreator.buildParamsMap(instance)
        ) > 0;
    }

    public boolean delete(final Pojo instance) {
        return namedParameterJdbcTemplate.update(
                sqlSentenceWriter.writeDeleteSentence(instance.getClass()),
                parameterMapCreator.buildParamsMap(instance)
        ) > 0;
    }

//    public boolean update(final)
}
