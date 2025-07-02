package io.github.tanyaofei.copier;

import jakarta.annotation.Nonnull;

import java.lang.reflect.Type;

/**
 * @author tanyaofei
 * @since 2025/6/19
 **/
record PropertyType(

        @Nonnull
        Class<?> type,

        @Nonnull
        Type genericType,

        @Nonnull
        org.objectweb.asm.Type asmType

) {

    public PropertyType(@Nonnull Class<?> type, @Nonnull Type genericType) {
        this(type, genericType, org.objectweb.asm.Type.getType(type));
    }


}
