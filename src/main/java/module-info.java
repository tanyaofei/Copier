/**
 * @author tanyaofei
 * @since 2025/7/2
 **/
module io.github.tanyaofei.copier {
    requires jakarta.annotation;
    requires cglib;
    requires org.objectweb.asm;
    requires org.apache.commons.lang3;
    requires java.desktop;
    requires jdk.jfr;
    exports io.github.tanyaofei.copier;
}