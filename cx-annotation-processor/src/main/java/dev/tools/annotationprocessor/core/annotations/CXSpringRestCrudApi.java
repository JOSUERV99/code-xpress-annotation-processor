package dev.tools.annotationprocessor.core.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Main CX Spring Full Rest Crud API annotation
 *
 * @author josue.rojas
 */
@Retention(RetentionPolicy.RUNTIME)
@CXSpringRestCrudRepository
@CXSpringRestCrudService
@CXSpringRestCrudController
public @interface CXSpringRestCrudApi {
  /**
   * Path -> base url for API address
   *
   * @return
   */
  String path() default "/";
}
