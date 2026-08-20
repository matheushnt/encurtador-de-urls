package dev.matheushnt.url_shortener.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {

    String message() default "A senha não está de acordo com as regras";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
