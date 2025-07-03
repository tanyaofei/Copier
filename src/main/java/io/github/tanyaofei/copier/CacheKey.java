package io.github.tanyaofei.copier;

import jakarta.annotation.Nonnull;

/**
 * @author tanyaofei
 * @since 2025/7/2
 **/
record CacheKey(
        @Nonnull String source,
        @Nonnull String target,
        boolean useConverter
) {

    CacheKey(@Nonnull Class<?> source, @Nonnull Class<?> target, boolean useConverter) {
        this(source.getName(), target.getName(), useConverter);
    }

}
