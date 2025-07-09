package io.github.tanyaofei.copier;

import jakarta.annotation.Nonnull;

import java.util.function.Predicate;

/**
 * @author tanyaofei
 * @since 2025/7/1
 **/
interface NamingPolicy {


    @Nonnull
    String getName(@Nonnull String prefix, @Nonnull Object key, @Nonnull Predicate<String> available);

    @Nonnull
    String getTag();


}
