import dabba.doo.annotationprocessor.db.Pojo;
import dabba.doo.annotationprocessor.db.sqlwriter.sentences.SqlDeleteSentenceWriter;
import dabba.doo.annotationprocessor.db.sqlwriter.sentences.SqlISelectSentenceWriter;
import dabba.doo.annotationprocessor.db.sqlwriter.sentences.SqlInsertSentenceWriter;

public class Main {

    public static void main(String[] args) {

        System.out.println(new SqlInsertSentenceWriter().write(Pojo.class));
        System.out.println(new SqlDeleteSentenceWriter().write(Pojo.class));
        System.out.println(new SqlISelectSentenceWriter().write(Pojo.class));
    }

}
