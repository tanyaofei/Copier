package io.github.tanyaofei.copier;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * Copier Base Exception
 * @author tanyaofei
 * @since 2025/7/1
 **/
public class CopierException extends RuntimeException {

    public CopierException() {
        super();
    }

    public CopierException(@Nullable String message) {
        super(message);
    }

    public CopierException(@Nullable String message, @Nullable Throwable cause) {
        super(message, cause);
    }

    public CopierException(@Nullable Throwable cause) {
        super(cause);
    }

    @Nonnull
    static CopierException wrap(@Nonnull Exception e) {
        if (e instanceof CopierException) {
            return (CopierException) e;
        }
        return new CopierException(e);
    }

    @Nonnull
    static CopierException wrap(@Nonnull String message, @Nonnull Exception e) {
        if (e instanceof CopierException) {
            return (CopierException) e;
        }
        return new CopierException(message, e);
    }

}
