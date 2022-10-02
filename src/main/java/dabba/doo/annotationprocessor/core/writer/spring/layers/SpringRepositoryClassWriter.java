package dabba.doo.annotationprocessor.core.writer.spring.layers;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import dabba.doo.annotationprocessor.core.writer.spring.SpringRestApiWriter;

import javax.lang.model.element.Modifier;

public class SpringRepositoryClassWriter<T> extends SpringRestApiWriter {

    public void xd() {
        JavaFile javaFile = JavaFile.builder("com.ankit.annotation", TypeSpec.classBuilder("HelloWorld")
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .addMethod(MethodSpec.methodBuilder("testFiler")
                                .addModifiers(Modifier.PUBLIC)
                                .returns(void.class)
                                .addParameter(String[].class, "args")
                                .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
                                .build())
                        .build())
                .build();
    }

    public <T> MethodSpec buildCreateMethod(final Class<T> clazz) {
        return null;
//            MethodSpec.methodBuilder("create")
//                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
//                    .addParameter(clazz, "instance")
//                    .addStatement("jdbcTemplate.queryForObject($S, $T, new $T)", 1)
//                    .returns(clazz)
//                    .build();
    }

    public MethodSpec buildGetMethod() {
        return null;
    }

    public MethodSpec buildDeleteMethod() {
        return null;
    }

    @Override
    public JavaFile writeFile() {
        return null;
    }

}
