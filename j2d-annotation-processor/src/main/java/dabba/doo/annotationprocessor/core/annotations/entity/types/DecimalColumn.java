package dabba.doo.annotationprocessor.core.annotations.entity.types;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Type column for Decimal(M,D) MySQL database datatype
 *
 * @author josue.rojas
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DecimalColumn {
  int precision() default 5;

  int max() default 15;
}
