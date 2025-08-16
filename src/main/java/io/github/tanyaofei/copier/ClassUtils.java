package io.github.tanyaofei.copier;

import jakarta.annotation.Nonnull;
import org.objectweb.asm.ClassReader;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.invoke.MethodHandles;

/**
 * @author tanyaofei
 * @since 2025/8/16
 **/
class ClassUtils {

    private ClassUtils() {
        throw new Error("Utility class");
    }

    /**
     * Define a class with specified Lookup
     *
     * @param lookup   Lookup
     * @param bytecode bytecode
     * @return class
     */
    @Nonnull
    public static Class<?> defineClass(@Nonnull MethodHandles.Lookup lookup, @Nonnull byte[] bytecode, boolean hidden) {
        try {
            if (hidden) {
                return lookup.defineHiddenClass(bytecode, true).lookupClass();
            } else {
                return lookup.defineClass(bytecode);
            }
        } catch (IllegalAccessException e) {
            throw new CopierException("Error defining class", e);
        }
    }

    /**
     * Dump bytecode to a *.class file
     *
     * @param directory base directory
     * @param bytecode  Java bytecode
     */
    public static void dumpClassFile(@Nonnull String directory, @Nonnull byte[] bytecode) {
        var reader = new ClassReader(bytecode);
        var className = reader.getClassName();

        var filepath = directory + File.separatorChar + className + ".class";

        var folder = new File(filepath).getParentFile();
        if (!folder.exists() && !folder.mkdirs()) {
            throw new CopierException("Failed to create debug location folder: " + folder.getPath());
        }

        try (var out = new BufferedOutputStream(new FileOutputStream(filepath))) {
            out.write(bytecode);
        } catch (Exception e) {
            throw new CopierException("Error dumping bytecode to disk", e);
        }
    }


}
