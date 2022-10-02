package dabba.doo.annotationprocessor.core.writer;

import com.squareup.javapoet.JavaFile;

public abstract class ClassWriter {

    public ClassWriter() {}

    public abstract JavaFile writeFile();


}
