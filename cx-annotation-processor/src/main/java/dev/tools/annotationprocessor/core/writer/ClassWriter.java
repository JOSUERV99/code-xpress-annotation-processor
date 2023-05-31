package dev.tools.annotationprocessor.core.writer;

import com.squareup.javapoet.JavaFile;
import java.util.Collection;
import javax.lang.model.element.TypeElement;

/**
 * ClassWriter Parent of java files generator based on annotations
 *
 * @author josue.rojas
 */
public abstract class ClassWriter {

  /**
   * Write java classes files based on clazz
   *
   * @param clazz entity class
   * @param targetPackage base package name where the files are going to be located
   * @return list of JavaFile's
   */
  public abstract Collection<JavaFile> write(final TypeElement clazz, final String targetPackage);
}
