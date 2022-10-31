package dabba.doo.example;

import dabba.doo.annotationprocessor.core.writer.spring.SpringRestApiWriter;
import dabba.doo.example.model.Pojo;

public class Application {

    public static void main(String[] args) {
        SpringRestApiWriter writer = new SpringRestApiWriter();
        writer.writeRestApiCode(Pojo.class, "j2d.generated");
    }

}
