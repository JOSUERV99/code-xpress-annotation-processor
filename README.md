# Code-Xpress-annotation-processor
A Java annotation processor for entity persistence using Spring

With a class like this, using the CX annotations, the annotation processor will generate a full CRUD RESTful service
that be can accesed through `<base_url>:<port>/j2d/message` with `GET`, `POST`, `PUT`, `DELETE` HTTP requests!

```java
@Data
@CXSpringRestCrudApi(path = "/message")
@CXEntity(tableName = "message_demo_01")
public class Message {

    @CXId(generated = true)
    @CXColumn(name = "ID")
    @BigInt()
    private Long id;

    @CXColumn(name = "CONTENT")
    @Varchar()
    private String content;

    @CXColumn(name = "FROM_CONTACT_NAME")
    @Varchar(length = 200)
    private String from;

    @CXColumn(name = "TO_CONTACT_NAME")
    @Varchar(length = 199)
    private String to;

    @CXColumn(name = "READERS_COUNTER")
    @Int
    private Integer readBy;
}

```
