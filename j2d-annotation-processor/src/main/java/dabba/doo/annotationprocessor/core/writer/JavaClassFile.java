package dabba.doo.annotationprocessor.core.writer;

import com.squareup.javapoet.JavaFile;

public class JavaClassFile {

  private String className;
  private String fileName;
  private String name;
  private JavaFile javaFile;
  private String packageName;

  public JavaClassFile() {}

  public String getClassName() {
    return className;
  }

  public JavaClassFile setClassName(String className) {
    this.className = className;
    return this;
  }

  public String getFileName() {
    return fileName;
  }

  public JavaClassFile setFileName(String fileName) {
    this.fileName = fileName;
    return this;
  }

  public String getName() {
    return name;
  }

  public JavaClassFile setName(String name) {
    this.name = name;
    return this;
  }

  public JavaFile getJavaFile() {
    return javaFile;
  }

  public JavaClassFile setJavaFile(JavaFile javaFile) {
    this.javaFile = javaFile;
    return this;
  }

  public String getPackageName() {
    return packageName;
  }

  public JavaClassFile setPackageName(String packageName) {
    this.packageName = packageName;
    return this;
  }

  @Override
  public String toString() {
    return "JavaClassFile{"
        + "className='"
        + className
        + '\''
        + ", fileName='"
        + fileName
        + '\''
        + ", name='"
        + name
        + '\''
        + ", javaFile="
        + javaFile
        + ", packageName='"
        + packageName
        + '\''
        + '}';
  }
}
