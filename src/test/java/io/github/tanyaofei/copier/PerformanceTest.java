package io.github.tanyaofei.copier;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

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
    public void test() {
        var sources = IntStream.range(0, 10_000_000).mapToObj(i -> new Obj(
                "a", "a", "a", "a", "a", "a", "a", "a", "a", "a"
        )).toList();


        var startedAt = System.currentTimeMillis();
        Copiers.copyList(sources, Obj.class);
        var endAt = System.currentTimeMillis();
        System.out.println((endAt - startedAt) + " ms");
    }


    public record Obj(

            String a,
            String b,
            String c,
            String d,
            String e,
            String f,
            String h,
            String i,
            String j,
            String k

    ) {

    }


}
