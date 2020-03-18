package com.github.rodionmoiseev.c10n;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Marks a c10n enum message interface in order to make it detectable by the
 * guice module and be registered with C10NFilters.enumMapping
 *
 * @author lelmarir
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface C10NEnumMessages {
    Class<? extends Enum<?>> value();
}
