package io.github.tanyaofei.copier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author tanyaofei
 * @since 2025/7/4
 **/

public class BoxValueTest {
    @BeforeAll
    public static void preTest() {
        System.setProperty("copier.debugLocation", "./target/generated-test-classes/" + BoxValueTest.class.getSimpleName());
    }

    @Test
    public void testBox() {
        var a = new A(1, 2L);
        var b = Copiers.copy(a, B.class);
        System.out.println(a);
        System.out.println(b);

        // 不支持拆箱
        Assertions.assertEquals(0, b.a);
        Assertions.assertEquals(0L, b.b);
    }

    @Test
    public void testUnbox() {
        var b = new B(1, 2L);
        var a = Copiers.copy(b, A.class);
        System.out.println(b);
        System.out.println(a);

        Assertions.assertNull(a.a);
        Assertions.assertNull(a.b);
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class A {
        private Integer a;
        private Long b;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class B {
        private int a;
        private long b;
    }


}
