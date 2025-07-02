package io.github.tanyaofei.copier;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.HashMap;
import java.util.Objects;

/**
 * 对象拷贝时使用的额外属性
 *
 * @author tanyaofei
 * @since 2025/6/19
 **/
public class Properties extends HashMap<String, Object> {

    @Nonnull
    public static Properties of() {
        return new Properties();
    }

    @Nonnull
    public static Properties of(@Nonnull String k, @Nullable Object v) {
        return ofN(k, v);
    }

    @Nonnull
    public static Properties of(@Nonnull String k1, @Nullable Object v1, @Nonnull String k2, @Nullable Object v2) {
        return ofN(k1, v1, k2, v2);
    }

    @Nonnull
    public static Properties of(@Nonnull String k1, @Nullable Object v1, @Nonnull String k2, @Nullable Object v2, @Nonnull String k3, @Nullable Object v3) {
        return ofN(k1, v1, k2, v2, k3, v3);
    }

    @Nonnull
    public static Properties of(@Nonnull String k1, @Nullable Object v1, @Nonnull String k2, @Nullable Object v2, @Nonnull String k3, @Nullable Object v3, @Nonnull String k4, @Nullable Object v4) {
        return ofN(k1, v1, k2, v2, k3, v3, k4, v4);
    }

    @Nonnull
    public static Properties of(@Nonnull String k1, @Nullable Object v1, @Nonnull String k2, @Nullable Object v2, @Nonnull String k3, @Nullable Object v3, @Nonnull String k4, @Nullable Object v4, @Nonnull String k5, @Nullable Object v5) {
        return ofN(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
    }

    @Nonnull
    public static Properties of(@Nonnull String k1, @Nullable Object v1, @Nonnull String k2, @Nullable Object v2, @Nonnull String k3, @Nullable Object v3, @Nonnull String k4, @Nullable Object v4, @Nonnull String k5, @Nullable Object v5, @Nonnull String k6, @Nullable Object v6) {
        return ofN(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6);
    }

    @Nonnull
    public static Properties of(@Nonnull String k1, @Nullable Object v1, @Nonnull String k2, @Nullable Object v2, @Nonnull String k3, @Nullable Object v3, @Nonnull String k4, @Nullable Object v4, @Nonnull String k5, @Nullable Object v5, @Nonnull String k6, @Nullable Object v6, @Nonnull String k7, @Nullable Object v7) {
        return ofN(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7);
    }

    @Nonnull
    public static Properties of(@Nonnull String k1, @Nullable Object v1, @Nonnull String k2, @Nullable Object v2, @Nonnull String k3, @Nullable Object v3, @Nonnull String k4, @Nullable Object v4, @Nonnull String k5, @Nullable Object v5, @Nonnull String k6, @Nullable Object v6, @Nonnull String k7, @Nullable Object v7, @Nonnull String k8, @Nullable Object v8) {
        return ofN(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8);
    }

    @Nonnull
    public static Properties of(@Nonnull String k1, @Nullable Object v1, @Nonnull String k2, @Nullable Object v2, @Nonnull String k3, @Nullable Object v3, @Nonnull String k4, @Nullable Object v4, @Nonnull String k5, @Nullable Object v5, @Nonnull String k6, @Nullable Object v6, @Nonnull String k7, @Nullable Object v7, @Nonnull String k8, @Nullable Object v8, @Nonnull String k9, @Nullable Object v9) {
        return ofN(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9);
    }

    @Nonnull
    public static Properties of(@Nonnull String k1, @Nullable Object v1, @Nonnull String k2, @Nullable Object v2, @Nonnull String k3, @Nullable Object v3, @Nonnull String k4, @Nullable Object v4, @Nonnull String k5, @Nullable Object v5, @Nonnull String k6, @Nullable Object v6, @Nonnull String k7, @Nullable Object v7, @Nonnull String k8, @Nullable Object v8, @Nonnull String k9, @Nullable Object v9, @Nonnull String k10, @Nullable Object v10) {
        return ofN(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10);
    }

    @Nonnull
    public static Properties ofProperties(
            @Nonnull Property... kvs
    ) {
        if (kvs.length == 0) {
            return Properties.of();
        }
        var properties = new Properties();
        for (var kv : kvs) {
            properties.put(Objects.requireNonNull(kv.key(), "key"), kv.value());
        }
        return properties;
    }

    @Nonnull
    private static Properties ofN(@Nonnull Object... kv) {
        var properties = new Properties();
        int size = kv.length;
        for (int i = 0; i < size; i += 2) {
            var k = Objects.requireNonNull((String) kv[i], "key");
            var v = kv[i + 1];
            properties.put(k, v);
        }
        return properties;
    }

    public @Nonnull PropertiesConverter converter() {
        return new PropertiesConverter();
    }

    public record Property(

            @Nonnull
            String key,

            @Nullable
            Object value

    ) {
    }

    public class PropertiesConverter implements Converter {

        @Override
        public Object provide(@Nullable Object source, @Nonnull String property, @Nonnull Class<?> propertyType) {
            var value = Properties.this.get(property);
            if (value == null) {
                return null;
            }

            if (propertyType.isAssignableFrom(value.getClass())) {
                return value;
            }

            return null;
        }

    }


}
