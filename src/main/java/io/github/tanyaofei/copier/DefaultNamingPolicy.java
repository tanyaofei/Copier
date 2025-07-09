package io.github.tanyaofei.copier;

import jakarta.annotation.Nonnull;

import java.util.function.Predicate;

/**
 * @author tanyaofei
 * @since 2025/7/1
 **/
class DefaultNamingPolicy implements NamingPolicy {

    @Nonnull
    @Override
    public String getName(@Nonnull String prefix, @Nonnull Object key, @Nonnull Predicate<String> available) {

        var base = prefix + "$$" + this.getTag() + "$$" + Integer.toHexString(key.hashCode());

        int i = 2;
        String attempt = base;
        while (!available.test(attempt)) {
            attempt = attempt + "_" + (i++);
        }
        return attempt;
    }

    @Nonnull
    @Override
    public String getTag() {
        return "GeneratedByCopier";
    }

}
