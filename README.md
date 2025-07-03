# 对象拷贝工具

## 使用前提

+ JDK 17+

## 如何安装
```xml
<dependency>
    <groupId>io.github.tanyaofei</groupId>
    <artifactId>copier</artifactId>
    <version>0.1.3</version>
</dependency>
```
## 特性

1. 使用字节码技术生成拷贝工具, 相比反射拷贝性能更高
2. 支持 JDK 14+ 的 Record 类, 同时也支持普通的 class
3. 支持属性兼容拷贝, 如 `Integer => Number`, `List<Integer> => Collection<? extends Number>`
4. 支持自定义转换器 `Converter`
5. 生成出来的 Copier 对象和类是 `WeakReference`, 允许 GC 进行回收
6. 支持 Lombok `@Accessor(chain=true)` 生成出来的带返回值的 setter 方法

## 性能对比

拷贝 1 千万对象耗时

| 工具                | 耗时              |
|-------------------|-----------------|
| **Copier**        | 50ms ~ 70ms     |
| CGLib Bean Copier | 50ms ~ 70ms     |
| Spring BeanUtils  | 1500ms ~ 1700ms |

## 注意事项

+ 如果 Target 是一个 **非** record 的对象, 需要提供无参构造函数
+ 如果 Target 是一个 record 对象, 不支持 `Copiers.copyInto()` 方法

## 简单使用

```java

import io.github.tanyaofei.copier.Copiers;

import java.util.Collection;
import java.util.List;

public record Source(
        String a,
        Integer b,
        List<Integer> c,
        Boolean d
) {

}


public record Target(
        String a,
        Integer b,
        Collection<? extends Number> c,
        boolean d,
        float e
) {
}

public static void main(String[] args) {
    var source = new Source("a", 1, List.of(1), true);
    var target = Copiers.copy(source, Target.class);
    assert target.a.equals("a");        // ✅
    assert target.b.equals(1);          // ✅
    assert target.c.equals(List.of(1)); // ✅
    assert !target.d;                   // ❌, 默认 false
    assert target.e == 0F;              // ❌, 默认 0
}
```

## 为 Record 提供不存在的属性值

由于 Record 类只能构造时提供所有属性值, 当 Source 没有提供对应的属性进行拷贝时, 可以使用 `Properties` 来提供额外的属性值

```java

import io.github.tanyaofei.copier.Copiers;
import io.github.tanyaofei.copier.Properties;

import java.util.AbstractSequentialList;

public record Target(
        String a,
        String b
) {
}

public static void main(String[] args) {
    var target = Copiers.copy(new Object(), Target.class, Properties.of(
            "a", "this is a",
            "b", "this is b"
    ));
    assert target.a.equals("this is a");
    assert target.b.equals("this is b");
}


```

## 使用 Converter 来转换属性值

```java
import io.github.tanyaofei.copier.Converter;
import io.github.tanyaofei.copier.Copiers;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.AbstractSequentialList;

public record Source(
        Integer a
) {
}

public record Target(
        String b
) {
}

public class MyConverter implements Converter {

    @Override
    public Object convert(@Nullable Object value, @Nonnull String property, @Nonnull Class<?> propertyType, boolean assignable) {
        if (property.equals("a")) {
            if (value == null) {
                return null;
            } else {
                return value.toString();
            }
        }

        return assignable ? value : null;
    }
}

public static void main(String[] args) {
    var source = new Source(1);
    var target = Copiers.copy(source, Target.class, new MyConverter());
    assert target.b.equals("1");
}

```

## 调试

Copier 允许将生成出来的字节码写入到磁盘, 只需要设置 JVM 参数

方式 1. 启动时添加参数 `-Dcopier.debugLocation=/your/filepath`

方式 2. 代码里执行 `System.setProperty("copier.debugLocation", "/your/filepath");`

## 运行时生成的字节码

Copier 是在运行时通过字节码技术生成对应的 Copier, 并用它来进行对象拷贝, 相比于反射具有更高的性能, 以下是一些运行时自动生成的字节码

```java
public record A(
        String a,
        Integer b,
        int c,
        List<String> d,
        List<Integer> e
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
```

生成出来的 Copier (不使用 Converter)

```java
public class BCopier$$A$$ByCopier$$1394afd4 extends Copier {
    public Object copy(Object var1, Converter var2) {
        B var3 = new B();
        this.copyInto(var1, var3, var2);
        return var3;
    }

    public void copyInto(Object var1, Object var2, Converter var3) {
        A var4 = (A) var1;
        B var5 = (B) var2;
        var5.setA(var4.a());
        var5.setB(var4.b());
        var5.setC(var4.c());
        var5.setD(var4.d());
        var5.setE(var4.e());
    }
}
```