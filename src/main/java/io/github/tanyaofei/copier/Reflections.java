package io.github.tanyaofei.copier;

import jakarta.annotation.Nonnull;

import java.lang.invoke.MethodHandles;

/**
 * @author tanyaofei
 * @since 2025/7/1
 **/
abstract class Reflections {


    @Nonnull
    public static Class<?> defineClass(@Nonnull MethodHandles.Lookup lookup, @Nonnull byte[] bytecode) {
        try {
            return lookup.defineHiddenClass(bytecode, true).lookupClass();
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Error defining class", e);
        }
    }


}
