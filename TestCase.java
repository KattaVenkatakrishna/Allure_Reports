package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to store Test Case ID for test methods
 */
@Retention(RetentionPolicy.RUNTIME)  // Available at runtime for reflection
@Target(ElementType.METHOD)           // Can only be used on methods
public @interface TestCase {
    String id();                      // Test case ID (e.g., "TCHP15")
    String description() default "";   // Optional description
}