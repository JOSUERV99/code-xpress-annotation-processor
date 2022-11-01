package dabba.doo.annotationprocessor.core.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@J2dSpringRestCrudRepository
@J2dSpringRestCrudService
@J2dSpringRestCrudController
public @interface J2dSpringRestCrudApi {
    String path() default "/";
}
