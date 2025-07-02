package io.github.tanyaofei.copier;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

/**
 * @author tanyaofei
 * @since 2025/6/4
 **/
public abstract class Copier {

    /**
     * This method will be overridden by the generated Copier
     *
     * @param source    source object
     * @param converter converter
     * @return target object
     */
    public Object copy(@Nonnull Object source, @Nullable Converter converter) {
        throw new UnsupportedOperationException("copy");
    }

    /**
     * This method will be overridden by the generated Copier
     *
     * @param source    source object
     * @param target    target object
     * @param converter converter
     */
    public void copyInto(@Nonnull Object source, @Nonnull Object target, @Nullable Converter converter) {
        throw new UnsupportedOperationException("copyInto");
    }

    /**
     * Create a copier
     *
     * @param sourceClass  source class
     * @param targetClass  target class
     * @param useConverter if use converter
     * @param lookup       Lookup
     * @return Copier
     */
    @Nonnull
    public static Copier create(@Nonnull Class<?> sourceClass, @Nonnull Class<?> targetClass, boolean useConverter, @Nonnull MethodHandles.Lookup lookup) {
        try {
            if (targetClass.isRecord()) {
                var gen = new AllArgsConstructorCopierGenerator(sourceClass, targetClass, useConverter, lookup);
                return gen.create();
            } else if (hasNoArgsConstructor(targetClass)) {
                // 非 Record 类型必须提供无参数构造器以满足 "copy() -> copyInto()" 方法
                var gen = new NoArgsConstructorCopierGenerator(sourceClass, targetClass, useConverter, lookup);
                return gen.create();
            }
        } catch (Exception e) {
            throw CopierException.wrap("Error generating copier for: %s -> %s".formatted(sourceClass.getName(), targetClass.getName()), e);
        }

        throw new CopierException("Can not find a suitable constructor for " + targetClass.getName());
    }

    private static boolean hasNoArgsConstructor(@Nonnull Class<?> clazz) {
        Constructor<?> ctor;
        try {
            ctor = clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            return false;
        }

        var modifiers = ctor.getModifiers();
        return !Modifier.isProtected(modifiers) && !Modifier.isPrivate(modifiers); // public or default(package)
    }

}
