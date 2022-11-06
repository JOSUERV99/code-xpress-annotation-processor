package dabba.doo.annotationprocessor.core.annotations.entity.types;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Type column for Integer MySQL database datatype
 *
 * @author josue.rojas
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IntColumn {
  int length() default 15;
}
