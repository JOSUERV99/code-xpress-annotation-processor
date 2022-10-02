package dabba.doo.annotationprocessor;

import dabba.doo.annotationprocessor.db.Pojo;
import dabba.doo.annotationprocessor.db.paramresolver.ParameterMapCreator;
import dabba.doo.annotationprocessor.db.sqlwriter.SqlSentenceReadWriter;
import dabba.doo.annotationprocessor.db.sqlwriter.SqlSentenceWriteWriter;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {


    }

    public static void demoSqWriter() {
        final Pojo instance = getSimplePojo();
        final SqlSentenceWriteWriter sqlSentenceWriteWriter = new SqlSentenceWriteWriter();
        System.out.println(sqlSentenceWriteWriter.writeInsertSentence(instance.getClass()));
        System.out.println(sqlSentenceWriteWriter.writeDeleteSentence(instance.getClass()));
    }

    public static void demoSqReader() {
        final Pojo instance = getSimplePojo();
        final SqlSentenceReadWriter sqlSentenceReadWriter = new SqlSentenceReadWriter();
//        System.out.println(sqlSentenceReadWriter.w(instance.getClass()));
//        System.out.println(sqlSentenceReadWriter.writeDeleteSentence(instance.getClass()));
    }

    public static void demoParameterCreator() {
        final Pojo instance = getSimplePojo();
        System.out.println(new ParameterMapCreator().buildParamsMap(instance).getValues());
    }

    public static Pojo getSimplePojo() {
        final Pojo instance = new Pojo();
        instance.setId(1);
        instance.setMessageContent("Hello");
        instance.setCountNumberFromExternalService(10);
        instance.setSecondId(2);
        return instance;
    }
}
