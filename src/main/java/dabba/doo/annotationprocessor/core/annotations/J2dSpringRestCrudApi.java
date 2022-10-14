package dabba.doo.annotationprocessor.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.SOURCE)
@J2dSpringRestCrudRepository
@J2dSpringRestCrudService
@J2dSpringRestCrudController
public @interface J2dSpringRestCrudApi {

}
