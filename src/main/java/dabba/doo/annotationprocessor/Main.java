package dabba.doo.annotationprocessor;

import com.squareup.javapoet.JavaFile;
import dabba.doo.annotationprocessor.core.writer.spring.SpringRestApiWriter;
import dabba.doo.annotationprocessor.core.writer.spring.layers.SpringRepositoryClassWriter;
import dabba.doo.annotationprocessor.core.writer.spring.layers.SpringServiceClassWriter;
import dabba.doo.annotationprocessor.db.Pojo;
import dabba.doo.annotationprocessor.db.paramresolver.ParameterMapCreator;
import dabba.doo.annotationprocessor.db.sqlwriter.SqlReadSentenceGenerator;
import dabba.doo.annotationprocessor.db.sqlwriter.SqlWriteSentenceGenerator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        testJ2dSpringRestCrudApi();
    }

    public static void testJ2dSpringRestCrudApi() {
        SpringRestApiWriter writer = new SpringRestApiWriter();
        writer.writeRestApiCode(Pojo.class, "j2d.generated");
    }

    public static void testSqlSentences(Class<?> clazz) {
        System.out.println(SqlWriteSentenceGenerator.writeDeleteSentence(clazz));
        System.out.println(SqlWriteSentenceGenerator.writeInsertSentence(clazz));
        System.out.println(SqlWriteSentenceGenerator.writeUpdateSentence(clazz));
        System.out.println(SqlReadSentenceGenerator.writeSelectSentence(clazz));
        System.out.println(SqlReadSentenceGenerator.writeSelectSentenceById(clazz));
    }

    private static void writeJavaFile(final String filename, final String packageName, final String content) throws IOException {
        Files.createDirectories(Paths.get(packageName));
        final List<String> lines = Arrays.asList(content.split("\n"));
        final Path file = Paths.get(String.format("%s/%s.java", packageName, filename));
        Files.write(file, lines, StandardCharsets.UTF_8);
    }

    public static Pojo getSimplePojo() {
        final Pojo instance = new Pojo();
        instance.setId(1);
        instance.setMessageContent("Hello");
        instance.setCountNumberFromExternalService(10);
        return instance;
    }
}
