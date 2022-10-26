package dabba.doo.example.model;

import dabba.doo.annotationprocessor.core.annotations.J2dSpringRestCrudApi;
import dabba.doo.annotationprocessor.core.annotations.entity.J2dColumn;
import dabba.doo.annotationprocessor.core.annotations.entity.J2dEntity;
import dabba.doo.annotationprocessor.core.annotations.entity.J2dId;

@J2dSpringRestCrudApi
@J2dEntity(tableName = "pojo_table")
public class Pojo {

    @J2dId(generated = true)
    @J2dColumn(name = "id")
    private Integer id;

    @J2dId
    @J2dColumn(name = "secondId")
    private Integer secondId;

    @J2dColumn(name = "message_content")
    private String messageContent;

    @J2dColumn(name = "count_number")
    private Integer countNumberFromExternalService;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSecondId() {
        return secondId;
    }

    public void setSecondId(Integer secondId) {
        this.secondId = secondId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Integer getCountNumberFromExternalService() {
        return countNumberFromExternalService;
    }

    public void setCountNumberFromExternalService(Integer countNumberFromExternalService) {
        this.countNumberFromExternalService = countNumberFromExternalService;
    }

    @Override
    public String toString() {
        return "Pojo{" +
                "id=" + id +
                ", secondId=" + secondId +
                ", messageContent='" + messageContent + '\'' +
                ", countNumberFromExternalService=" + countNumberFromExternalService +
                '}';
    }
}
