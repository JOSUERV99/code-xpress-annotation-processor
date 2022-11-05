package dabba.doo.annotationprocessor.core.annotations.entity.types;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DecimalColumn {
    int precision() default 5;
    int max() default 15;
}
