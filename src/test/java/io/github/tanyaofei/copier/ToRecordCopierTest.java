package io.github.tanyaofei.copier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

/**
 * @author tanyaofei
 * @since 2025/7/1
 **/
public class ToRecordCopierTest extends Assertions {

    @BeforeAll
    public static void preTest() {
        System.setProperty("copier.debugLocation", "./target/generated-test-classes");
    }

    @Test
    public void testAssignable() {
        var a = new A(
                "a", 1, List.of(1), List.of("d"), List.of(true), 1
        );
        var b = Copiers.copy(a, B.class);

        System.out.println(a);
        System.out.println(b);

        assertEquals(a.a(), b.a());
        assertEquals(a.b(), b.b());
        assertEquals(a.c(), b.c());
        assertEquals(a.d(), b.d());
        assertEquals(a.e(), b.e());
        assertEquals(a.f(), b.f());
    }

    @Test
    public void testAssignable2() {
        var a = new A(
                "a", 1, List.of(1), List.of("d"), List.of(true), 1
        );

        var a2 =  Copiers.copy(a, A.class);
        System.out.println(a);
        System.out.println(a2);

        assertEquals(a, a2);
    }

    @Test
    public void testAssignableProperties() {
        var empty = new Empty();
        var b = Copiers.copy(empty, B.class, Properties.of(
                "a", "a",
                "b", 1,
                "c", List.of(1),
                "d", List.of("d"),
                "e", List.of(true),
                "f", 1
        ));


        System.out.println(empty);
        System.out.println(b);

        assertEquals("a", b.a);
        assertEquals(1, b.b);
        assertEquals(List.of(1), b.c);
        assertEquals(List.of("d"), b.d);
        assertEquals(List.of(true), b.e);
        assertEquals(1, b.f);
    }

    @Test
    public void testUnassignableProperties() {
        var empty = new Empty();
        var b = Copiers.copy(empty, B.class, Properties.of(
                "a", new Object(),
                "b", new Object(),
                "c", new Object(),
                "d", new Object(),
                "e", new Object(),
                "f", new Object()
        ));

        System.out.println(empty);
        System.out.println(b);

        assertNull(b.a);
        assertNull(b.b);
        assertNull(b.c);
        assertNull(b.d);
        assertNull(b.e);
        assertEquals(0, b.f);
    }

    public record Empty() {

    }


    public record A(
            String a,
            Integer b,
            Collection<Integer> c,
            List<String> d,
            List<Boolean> e,
            int f
    ) {
    }

    public record B(
            String a,
            Number b,
            Collection<? extends Number> c,
            List<String> d,
            Collection<Boolean> e,
            int f
    ) {
    }


}
