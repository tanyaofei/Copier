package io.github.tanyaofei.copier;

import jakarta.annotation.Nonnull;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * @author tanyaofei
 * @since 2025/6/20
 **/
class IdentifierWeakReference<K, T> extends WeakReference<T> {

    @Nonnull
    private final K identifier;

    public IdentifierWeakReference(@Nonnull K identifier, @Nonnull T referent, @Nonnull ReferenceQueue<T> referenceQueue) {
        super(referent, referenceQueue);
        this.identifier = identifier;
    }

    @Nonnull
    public K getIdentifier() {
        return identifier;
    }

}
