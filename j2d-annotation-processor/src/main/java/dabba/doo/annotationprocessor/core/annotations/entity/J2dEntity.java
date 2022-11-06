package dabba.doo.annotationprocessor.core.annotations.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * J2D Entity Main entity annotation for API code generation
 *
 * @author josue.rojas
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface J2dEntity {
  String tableName();
}
