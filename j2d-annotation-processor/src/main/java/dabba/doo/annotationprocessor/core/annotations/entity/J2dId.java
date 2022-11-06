package dabba.doo.annotationprocessor.core.annotations.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * J2D Entity Identifier annotation for entity database persistence
 *
 * @author josue.rojas
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface J2dId {
  /**
   * Generated flag for autoincrement stuff
   *
   * @return
   */
  boolean generated() default false;
}
