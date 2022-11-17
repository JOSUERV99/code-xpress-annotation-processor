# j2d-annotation-processor
A Java annotation processor for entity persistence using Spring

With a class like this, using the J2D annotations, the annotation processor will generate a full CRUD RESTful service
that be can accesed through `<base_url>:<port>/j2d/message` with `GET`, `POST`, `PUT`, `DELETE` HTTP requests!

```java
@Data
@J2dSpringRestCrudApi(path = "/message")
@J2dEntity(tableName = "message_demo_01")
public class Message {

    @J2dId(generated = true)
    @J2dColumn(name = "ID")
    @BigIntColumn()
    private Long id;

    @J2dColumn(name = "CONTENT")
    @VarcharColumn()
    private String content;

    @J2dColumn(name = "FROM_CONTACT_NAME")
    @VarcharColumn(length = 200)
    private String from;

    @J2dColumn(name = "TO_CONTACT_NAME")
    @VarcharColumn(length = 199)
    private String to;

    @J2dColumn(name = "READERS_COUNTER")
    @IntColumn
    private Integer readBy;
}

```
