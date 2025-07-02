package io.github.tanyaofei.copier;

import jakarta.annotation.Nonnull;
import net.sf.cglib.core.ReflectUtils;
import net.sf.cglib.core.Signature;
import org.objectweb.asm.Type;

import java.lang.reflect.Method;

/**
 * @param name        属性名称
 * @param type        属性类型
 * @param readMethod  获取属性的方法
 * @param writeMethod 写入属性的方法
 * @author tanyaofei
 * @since 2025/6/19
 **/
record Property(

        @Nonnull
        String name,

        @Nonnull
        PropertyType type,

        @Nonnull
        Method readMethod,

        // Record 没有
        Method writeMethod


) {

    public @Nonnull Class<?> propertyType() {
        return type.type();
    }

    public @Nonnull java.lang.reflect.Type propertyGenericType() {
        return type.genericType();
    }

    public @Nonnull Type propertyAsmType() {
        return type.asmType();
    }

    public @Nonnull Signature readMethodSignature() {
        return new Signature(this.readMethod.getName(), Type.getReturnType(this.readMethod), new Type[0]);
    }

    public @Nonnull Signature writeMethodSignature() {
        if (this.writeMethod == null) {
            throw new IllegalStateException("writeMethod is null");
        }

        return new Signature(this.writeMethod.getName(), Type.getReturnType(this.writeMethod), new Type[]{this.propertyAsmType()});
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public static Property[] forClass(@Nonnull Class<?> type) {
        if (type.isRecord()) {
            return forRecord((Class<? extends Record>) type);
        } else {
            return forBean(type);
        }
    }

    @Nonnull
    private static Property[] forRecord(@Nonnull Class<? extends Record> type) {
        var components = type.getRecordComponents();
        int size = components.length;
        var properties = new Property[size];
        for (int i = 0; i < size; i++) {
            var component = components[i];
            properties[i] = new Property(
                    component.getName(),
                    new PropertyType(component.getType(), component.getGenericType()),
                    component.getAccessor(),
                    null
            );
        }
        return properties;
    }

    @Nonnull
    private static Property[] forBean(@Nonnull Class<?> type) {
        var descriptors = ReflectUtils.getBeanProperties(type);
        int size = descriptors.length;
        var properties = new Property[size];
        for (int i = 0; i < size; i++) {
            var descriptor = descriptors[i];
            var readMethod = descriptor.getReadMethod();
            var writeMethod = descriptor.getWriteMethod();

            java.lang.reflect.Type genericType;
            if (readMethod != null) {
                genericType = readMethod.getGenericReturnType();
            } else if (writeMethod != null) {
                genericType = writeMethod.getGenericParameterTypes()[0];
            } else {
                continue;
            }

            try {
                properties[i] = new Property(
                        descriptor.getName(),
                        new PropertyType(descriptor.getPropertyType(), genericType),
                        descriptor.getReadMethod(),
                        descriptor.getWriteMethod()
                );
            } catch (Exception e) {
                throw new IllegalStateException("Error getting property descriptor: " + descriptor.getName(), e);
            }
        }
        return properties;
    }


}
