package dabba.doo.annotationprocessor;

import com.squareup.javapoet.JavaFile;
import dabba.doo.annotationprocessor.core.writer.spring.layers.SpringRepositoryClassWriter;
import dabba.doo.annotationprocessor.core.writer.spring.layers.SpringRestCrudControllerClassWriter;
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
//        testSqlSentences(Pojo.class);

        writeServiceClazz(Pojo.class);
        writeRepositoryClazz(Pojo.class);
    }

    public static void testSqlSentences(Class<?> clazz) {
        System.out.println(SqlWriteSentenceGenerator.writeDeleteSentence(clazz));
        System.out.println(SqlWriteSentenceGenerator.writeInsertSentence(clazz));
        System.out.println(SqlWriteSentenceGenerator.writeUpdateSentence(clazz));
        System.out.println(SqlReadSentenceGenerator.writeSelectSentence(clazz));
        System.out.println(SqlReadSentenceGenerator.writeSelectSentenceById(clazz));
    }

//    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        writeJavaFile("PojoRepository", "src/main/java/j2d/generated/repository", writeRepositoryClazz());
//        writeJavaFile("PojoService", "src/main/java/j2d/generated/service", writeServiceClazz(Class.forName("j2d.generated.repository.PojoRepository")));
//    }

//
//    for (Element elementMethod : element.getEnclosedElements()) {
//        if (elementMethod.getKind() != ElementKind.METHOD) {
//            //skip non-method elements like final fields
//            continue;
//        }
//        ExecutableElement method = (ExecutableElement)elementMethod; //cast
//        MethodSpec intentMethod = MethodSpec
//                .methodBuilder(method.getSimpleName().toString())
//                .addModifiers(Modifier.PUBLIC)
//                .addStatement("return $S", "my code")
//                .returns(ClassName.get(method.getReturnType())) //should be ok now
//                .build();
//        classBuilder.addMethod(intentMethod);
//    }

    public static void demoSqWriter() {
        final Pojo instance = getSimplePojo();
        final SqlWriteSentenceGenerator sqlSentenceWriteWriter = new SqlWriteSentenceGenerator();
        System.out.println(sqlSentenceWriteWriter.writeInsertSentence(instance.getClass()));
        System.out.println(sqlSentenceWriteWriter.writeDeleteSentence(instance.getClass()));
    }

    public static void demoSqReader() {
        final Pojo instance = getSimplePojo();
        final SqlReadSentenceGenerator sqlSentenceReadWriter = new SqlReadSentenceGenerator();
//        System.out.println(sqlSentenceReadWriter.w(instance.getClass()));
//        System.out.println(sqlSentenceReadWriter.writeDeleteSentence(instance.getClass()));
    }

    public static void demoParameterCreator() {
        final Pojo instance = getSimplePojo();
        System.out.println(new ParameterMapCreator().buildParamsMap(instance).getValues());
    }

    public static String writeRepositoryClazz(Class<?> clazz) {
        final SpringRepositoryClassWriter classWriter = new SpringRepositoryClassWriter();
        final JavaFile javaFile = classWriter.writeFile(Pojo.class, clazz.getPackage().getName());
        return javaFile.toString();
    }

    public static String writeServiceClazz(Class<?> clazz) {
        final SpringServiceClassWriter classWriter = new SpringServiceClassWriter();
        final JavaFile javaFile = classWriter.writeFile(Pojo.class, clazz.getPackage().getName());
        return javaFile.toString();
    }

//    public static String writeControllerClazz(Class<?> clazz) {
//        final SpringRestCrudControllerClassWriter classWriter = new SpringRestCrudControllerClassWriter();
//        final JavaFile javaFile = classWriter.writeFile(Pojo.class, clazz.getPackage().getName());
//        return javaFile.toString();
//    }

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
