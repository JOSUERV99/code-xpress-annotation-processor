package dabba.doo.annotationprocessor.core.writer.spring.layers;

import com.squareup.javapoet.MethodSpec;
import dabba.doo.annotationprocessor.core.writer.spring.SpringRestApiWriter;

import javax.lang.model.element.Modifier;

public class SpringRepositoryClassWriter extends SpringRestApiWriter {

    public MethodSpec buildCreateMethod() {

        final MethodSpec methodSpec =
            MethodSpec.methodBuilder("create")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addStatement("final Integer rows = ")
                    .returns(Integer.class)
                    .build();


        return null;
    }

    public MethodSpec buildGetMethod() {
        return null;
    }

    public MethodSpec buildDeleteMethod() {
        return null;
    }
}
