package io.github.tanyaofei.copier;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.lang.invoke.MethodHandles;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 对象拷贝工具
 *
 * @author tanyaofei
 * @since 2025/6/19
 **/
public abstract class Copiers {


    private Copiers() {
        throw new Error("Utility class");
    }

    /**
     * Cache of Copiers
     */
    final static Map<Object, IdentifierWeakReference<CacheKey, Copier>> COPIERS = new ConcurrentHashMap<>();

    /**
     * Reference queue
     */
    private final static ReferenceQueue<Copier> REFERENCE_QUEUE = new ReferenceQueue<>();

    static {
        var cleaner = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    var ref = (IdentifierWeakReference<CacheKey, ?>) REFERENCE_QUEUE.remove();
                    COPIERS.remove(ref.getIdentifier());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        cleaner.setName("Copier Cleaner");
        cleaner.setDaemon(true);
        cleaner.start();
    }

    /**
     * Clone a object
     *
     * @param source source
     * @param <T>    source type
     * @return 目标
     */
    @SuppressWarnings("unchecked")
    public static <T> T clone(@Nullable T source) {
        if (source == null) {
            return null;
        }
        return (T) copy(source, source.getClass());
    }

    /**
     * Copy an object
     *
     * @param source      source
     * @param targetClass target class
     * @param <U>         target type
     * @return target object
     */
    public static <U> U copy(@Nullable Object source, @Nonnull Class<U> targetClass) {
        return copy(source, targetClass, (Converter) null);
    }

    /**
     * Copy an object with extract properties, copier will
     *
     * @param source      source object
     * @param targetClass target class
     * @param properties  extra properties, see {@link Properties#converter()} and {@link  Properties.PropertiesConverter}
     * @param <U>         target type
     * @return target object
     * @see Properties.PropertiesConverter
     */
    public static <U> U copy(@Nullable Object source, @Nonnull Class<U> targetClass, @Nonnull Properties properties) {
        return copy(source, targetClass, properties.converter());
    }

    /**
     * Copy an object
     *
     * @param source      source object
     * @param targetClass target class
     * @param converter   converter
     * @param <U>         target type
     * @return target object
     */
    public static <U> U copy(@Nullable Object source, @Nonnull Class<U> targetClass, @Nullable Converter converter) {
        return copy(source, targetClass, converter, getLookup(targetClass));
    }

    /**
     * Copy an object
     *
     * @param source      source
     * @param targetClass target class
     * @param converter   converter
     * @param lookup      Lookup
     * @param <U>         target type
     * @return target object
     */
    public static <U> U copy(
            @Nullable Object source,
            @Nonnull Class<U> targetClass,
            @Nullable Converter converter,
            @Nonnull MethodHandles.Lookup lookup
    ) {
        if (source == null) {
            return null;
        }

        var copier = getCopier(source.getClass(), targetClass, converter != null, lookup);
        var target = copier.copy(source, converter);
        return targetClass.cast(target);
    }


    /**
     * Copy properties
     *
     * @param source source object
     * @param target target object
     */
    public static void copyInto(@Nullable Object source, @Nullable Object target) {
        if (source == null || target == null) {
            return;
        }
        copyInto(source, target, getLookup(target.getClass()));
    }

    /**
     * Copy properties
     *
     * @param source source object
     * @param target target object
     * @param lookup Lookup
     */
    public static void copyInto(@Nullable Object source, @Nullable Object target, @Nonnull MethodHandles.Lookup lookup) {
        if (source == null || target == null) {
            return;
        }

        if (target instanceof Record) {
            //noinspection DataFlowIssue
            copyInto(source, (Record) target);
        }

        var copier = getCopier(source.getClass(), target.getClass(), false, lookup);
        copier.copyInto(source, target, null);
    }

    /**
     * Cannot copy into a record object
     *
     * @throws IllegalArgumentException always
     * @deprecated Not supported
     */
    @Deprecated(forRemoval = true)
    @SuppressWarnings("DeprecatedIsStillUsed")
    public static void copyInto(@Nullable Object source, @Nullable Record target) throws IllegalArgumentException {
        if (source == null || target == null) {
            return;
        }
        throw new IllegalArgumentException("target is a record class: " + target.getClass().getName());
    }


    /**
     * Copy a collection of sources
     *
     * @param sources     collection of sources
     * @param targetClass target class
     * @param <T>         source type
     * @param <U>         target type
     * @return a collection of targets
     */
    public static <T, U> List<U> copyList(@Nullable Collection<T> sources, @Nonnull Class<U> targetClass) {
        return copyList(sources, targetClass, getLookup(targetClass));
    }

    /**
     * Copy a collection of sources
     *
     * @param sources     collection of sources
     * @param targetClass target class
     * @param lookup      Lookup
     * @param <T>         source type
     * @param <U>         target type
     * @return a collection of targets
     */
    public static <T, U> List<U> copyList(@Nullable Collection<T> sources, @Nonnull Class<U> targetClass, @Nonnull MethodHandles.Lookup lookup) {
        return copyList(sources, targetClass, null, lookup);
    }

    /**
     * Copy a collection of sources
     *
     * @param sources     collection of sources
     * @param targetClass target class
     * @param converter   converter
     * @param lookup      Lookup
     * @param <T>         source type
     * @param <U>         target type
     * @return a collection of targets
     */
    @SuppressWarnings("unchecked")
    public static <T, U> List<U> copyList(@Nullable Collection<T> sources, @Nonnull Class<U> targetClass, @Nullable Converter converter, @Nonnull MethodHandles.Lookup lookup) {
        if (sources == null) {
            return null;
        }

        var targets = new ArrayList<U>(sources.size());

        Copier copier = null;
        for (var source : sources) {
            U target;
            if (source == null) {
                target = null;
            } else {
                if (copier == null) {
                    copier = getCopier(source.getClass(), targetClass, converter != null, lookup);
                }
                target = (U) copier.copy(source, converter);
            }
            targets.add(target);
        }

        return targets;
    }

    @Nonnull
    private static Copier getCopier(@Nonnull Class<?> sourceClass, @Nonnull Class<?> targetClass, boolean useConverter, @Nonnull MethodHandles.Lookup lookup) {
        var cacheKey = new CacheKey(sourceClass, targetClass, useConverter);
        var copier = COPIERS.computeIfAbsent(
                cacheKey,
                __ -> new IdentifierWeakReference<>(cacheKey, generateCopier(sourceClass, targetClass, useConverter, lookup), REFERENCE_QUEUE)
        ).get();

        if (copier == null) {
            copier = generateCopier(sourceClass, targetClass, useConverter, lookup);
            COPIERS.put(cacheKey, new IdentifierWeakReference<>(cacheKey, copier, REFERENCE_QUEUE));
        }
        return copier;
    }

    private static Copier generateCopier(@Nonnull Class<?> sourceClass, @Nonnull Class<?> targetClass, boolean useConverter, @Nonnull MethodHandles.Lookup lookup) {
        return Copier.create(sourceClass, targetClass, useConverter, lookup);
    }

    private static MethodHandles.Lookup getLookup(@Nonnull Class<?> clazz) {
        try {
            return MethodHandles.privateLookupIn(clazz, MethodHandles.lookup());
        } catch (IllegalAccessException e) {
            throw new CopierException("Can not access lookup for: " + clazz.getName(), e);
        }
    }

}
