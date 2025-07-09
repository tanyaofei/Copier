package io.github.tanyaofei.copier;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author tanyaofei
 * @see Properties
 * @since 2025/7/1
 **/
@FunctionalInterface
public interface Converter {

    Converter NULL = (value, property, type, assignable) -> {
        if (assignable) {
            return value;
        }
        return null;
    };

    /**
     * The copier invokes this method for each property being copied, and the returned value is assigned to the property of the target object.
     * <pre>{@code
     *
     * public class Source {
     *     String assignable;
     *     Integer unassignable;
     * }
     *
     * public class Target {
     *     String assignable;
     *     int    unassignable;
     * }
     *
     * class GeneratedCopier {
     *
     *     public void copyInto(Source source, Target target, Converter converter) {
     *         target.setAssignable(converter.convert(source.getAssignable(), "assignable", String.class, true));
     *         target.setUnassignable(converter.convert(source.getUnassignable(), "unassignable", Integer.class, false));
     *     }
     *
     * }
     *
     * }</pre>
     *
     * @param value        value from source object
     * @param property     property name
     * @param propertyType property type
     * @param assignable   if the value from the source object is assignable to the target property
     * @return converted value
     */
    @Nullable
    Object convert(@Nullable Object value, @Nonnull String property, @Nonnull Class<?> propertyType, boolean assignable);

    /**
     * The copier invokes this method for each property that cannot be found in the target object by name,
     * and assigns the returned value to the corresponding property in the target object.
     *
     * <pre>{@code
     *
     * public class Source {
     *     String notFound;
     *     String typeNotMatched;
     *     String foundAndMatched;
     * }
     *
     * public class Target {
     *     Integer typeNotMatched;
     *     String foundAndMatched;
     * }
     *
     * class GeneratedCopier {
     *
     *     public void copyInto(Source source, Target target, Converter converter) {
     *          target.setNotFound(converter.provide(source, "notFound", String.class));
     *          target.setTypeNotMatched(converter.convert(source.getTypeNotMatched(), "typeNotMatched", String.class, false));
     *          target.setFoundAndMatched(converter.convert(source.getFoundAndMatched(), "foundAndMatched", String.class, true));
     *     }
     *
     * }
     *
     * }</pre>
     *
     * @param source       source object
     * @param property     property name
     * @param propertyType property type
     * @return provided value
     */
    default Object provide(@Nullable Object source, @Nonnull String property, @Nonnull Class<?> propertyType) {
        return null;
    }

}
