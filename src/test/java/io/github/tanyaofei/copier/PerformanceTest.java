package io.github.tanyaofei.copier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanCopier;

import java.lang.invoke.MethodHandles;

/**
 * @author tanyaofei
 * @since 2025/7/2
 **/
public class PerformanceTest {

    @BeforeAll
    public static void preTest() {
        System.setProperty("copier.debugLocation", "./target/generated-test-classes/" + PerformanceTest.class.getSimpleName());
    }

    @Test
    public void testCopier() {
        var source = new Obj(
                "a", "a", "a", "a", "a", "a", "a", "a", "a"
        );
        var copier = Copier.create(Obj.class, Obj.class, false, MethodHandles.lookup());

        var startedAt = System.currentTimeMillis();
        for (int i = 0; i < 10_000_000; i++) {
            copier.copy(source, null);
        }
        var endAt = System.currentTimeMillis();
        System.out.println((endAt - startedAt) + " ms");
    }

    @Test
    public void testBeanCopier() {
        var copier = BeanCopier.create(Obj.class, Obj.class, false);

        var source = new Obj(
                "a", "a", "a", "a", "a", "a", "a", "a", "a"
        );

        var startedAt = System.currentTimeMillis();
        for (int i = 0; i < 10_000_000; i++) {
            copier.copy(source, new Obj(), null);
        }

        var endAt = System.currentTimeMillis();
        System.out.println((endAt - startedAt) + " ms");
    }

    @Test
    public void testBeanUtils() {
        var source = new Obj(
                "a", "a", "a", "a", "a", "a", "a", "a", "a"
        );

        var startedAt = System.currentTimeMillis();
        for (int i = 0; i < 10_000_000; i++) {
            BeanUtils.copyProperties(source, new Obj());
        }

        var endAt = System.currentTimeMillis();
        System.out.println((endAt - startedAt) + " ms");
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Obj {
        private String a;
        private String b;
        private String c;
        private String d;
        private String e;
        private String f;
        private String h;
        private String j;
        private String k;
    }

}
