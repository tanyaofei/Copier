package io.github.tanyaofei.copier;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author tanyaofei
 * @since 2025/7/2
 **/
public class LombokTest {

    @BeforeAll
    public static void preTest() {
        System.setProperty("copier.debugLocation", "./target/generated-test-classes/" + LombokTest.class.getSimpleName());
    }


    @Test
    public void test() {
        var a = new A(1);
        var a2 = Copiers.copy(a, A.class);
        System.out.println(a);
        System.out.println(a2);
    }


    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class A {

        private Integer uAge;

        public Integer getuAge() {
            return uAge;
        }

        public A setuAge(Integer uAge) {
            this.uAge = uAge;
            return this;
        }
    }


}
