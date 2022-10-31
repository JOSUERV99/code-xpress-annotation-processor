package dabba.doo.annotationprocessor.core.annotations.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface J2dId {
    boolean generated() default false;
}
