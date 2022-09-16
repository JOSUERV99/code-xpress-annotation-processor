package dabba.doo.annotationprocessor.db;

import dabba.doo.annotationprocessor.core.annotations.entity.J2dColumn;
import dabba.doo.annotationprocessor.core.annotations.entity.J2dEntity;
import dabba.doo.annotationprocessor.core.annotations.entity.J2dId;
import lombok.Data;

@Data
@J2dEntity(tableName = "pojo_table")
public class Pojo {

    @J2dId
    @J2dColumn(name = "id")
    private Integer id;

    @J2dId
    @J2dColumn(name = "secondId")
    private Integer secondId;

    @J2dColumn(name = "message_content")
    private String messageContent;

    @J2dColumn(name = "count_number")
    private Integer countNumberFromExternalService;
}
