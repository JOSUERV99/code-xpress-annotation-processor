package dabba.doo.annotationprocessor.core.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Main J2D Spring Full Rest Crud API annotation
 *
 * @author josue.rojas
 */
@Retention(RetentionPolicy.RUNTIME)
@J2dSpringRestCrudRepository
@J2dSpringRestCrudService
@J2dSpringRestCrudController
public @interface J2dSpringRestCrudApi {
  /**
   * Path -> base url for API address
   *
   * @return
   */
  String path() default "/";
}
