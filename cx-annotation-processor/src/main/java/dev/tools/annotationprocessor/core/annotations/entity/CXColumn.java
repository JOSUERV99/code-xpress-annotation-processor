package dev.tools.annotationprocessor.core.annotations.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** CX entity Column annotation for class attribute */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CXColumn {
  String name();
}
