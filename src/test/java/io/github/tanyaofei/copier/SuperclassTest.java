package io.github.tanyaofei.copier;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author tanyaofei
 * @since 2025/7/3
 **/
public class SuperclassTest extends Assertions {


    @Test
    public void test() {
        var son = new Son();
        son.setA("a");
        son.setB("b");

        var son2 = Copiers.clone(son);
        System.out.println(son);
        System.out.println(son2);

        assertEquals(son.getA(), son2.getA());
        assertEquals(son.getB(), son2.getB());
    }


    @Data
    @NoArgsConstructor
    public static class Parent {
        private String a;
    }

    @Data
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class Son extends Parent {
        private String b;
    }

}
