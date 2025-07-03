package io.github.tanyaofei.copier;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

/**
 * @author tanyaofei
 * @since 2025/7/1
 **/
public class ToBeanTest extends Assertions {

    @BeforeAll
    public static void preTest() {
        System.setProperty("copier.debugLocation", "./target/generated-test-classes/" + ToBeanTest.class.getSimpleName());
    }

    @Test
    public void testAssignable() {
        var a = new A(
                "a", 1, 1, List.of("d"), List.of(1)
        );

        var b = Copiers.copy(a, B.class);

        System.out.println(a);
        System.out.println(b);

        assertEquals(a.a, b.a);
        assertEquals(a.b, b.b);
        assertEquals(a.c, b.c);
        assertEquals(a.d, b.d);
        assertEquals(a.e, b.e);
    }

    @Test
    public void testAssignable2() {
        var a = new A(
                "a", 1, 1, List.of("d"), List.of(1)
        );

        var a2 = Copiers.copy(a, A.class);
        System.out.println(a);
        System.out.println(a2);

        assertEquals(a, a2);
    }

    @Test
    public void testProperties() {
        var empty = new Empty();
        var b = Copiers.copy(empty, B.class, Properties.of(
                "a", "a",
                "b", 1,
                "c", 1,
                "d", List.of("d"),
                "e", List.of(1)
        ));

        System.out.println(empty);
        System.out.println(b);

        assertEquals("a", b.a);
        assertEquals(1, b.b);
        assertEquals(1, b.c);
        assertEquals(List.of("d"), b.d);
        assertEquals(List.of(1), b.e);
    }

    @Test
    public void testUnsignable() {
        var empty = new Empty();
        var b = Copiers.copy(empty, B.class);
        System.out.println(empty);
        System.out.println(b);

        assertNull(b.a);
        assertNull(b.b);
        assertEquals(0, b.c);
        assertNull(b.d);
        assertNull(b.e);
    }

    @Test
    public void testUnsignable2() {
        var a = new A2(
                "a", 1, 1, List.of("d"), List.of(1)
        );
        var b = Copiers.copy(a, B.class);
        System.out.println(a);
        System.out.println(b);
        assertNull(b.a);
        assertNull(b.b);
        assertEquals(0, b.c);
        assertNull(b.d);
        assertNull(b.e);
    }

    public record Empty() {

    }

    public record A(
            String a,
            Integer b,
            int c,
            List<String> d,
            List<Integer> e
    ) {

    }

    public record A2(
            Object a,
            Object b,
            Object c,
            Object d,
            Object e
    ) {

    }

    @Data
    @NoArgsConstructor
    public static class B {
        private String a;
        private Integer b;
        private int c;
        private List<String> d;
        private Collection<? extends Number> e;
    }


}
