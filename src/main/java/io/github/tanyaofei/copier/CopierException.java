package io.github.tanyaofei.copier;

import jakarta.annotation.Nonnull;

/**
 * @author tanyaofei
 * @since 2025/7/1
 **/
public class CopierException extends RuntimeException {

    public CopierException() {
        super();
    }

    public CopierException(String message) {
        super(message);
    }

    public CopierException(String message, Throwable cause) {
        super(message, cause);
    }

    public CopierException(Throwable cause) {
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
