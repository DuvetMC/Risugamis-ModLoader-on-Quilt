package org.duvetmc.risugami;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Because Mixin doesn't support static fields, this annotation is used to mark
 * fields that should be static in order to declare them in the mixin and then
 * manually transform them to static in the plugin (while also removing the annotation).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Static {
}
