package io.github.tanyaofei.copier;


import net.sf.cglib.core.Signature;
import org.objectweb.asm.Type;

/**
 * @author tanyaofei
 * @since 2025/7/1
 **/
interface Constants extends net.sf.cglib.core.Constants {

    Type TYPE_CONVERTER = Type.getType(Converter.class);

    Type TYPE_COPIER = Type.getType(Copier.class);

    Signature SIGNATURE_CONVERTER$provide = new Signature(
            "provide", TYPE_OBJECT, new Type[]{TYPE_OBJECT, TYPE_STRING, TYPE_CLASS}
    );

    Signature SIGNATURE_CONVERTER$convert = new Signature(
            "convert", TYPE_OBJECT, new Type[]{TYPE_OBJECT, TYPE_STRING, TYPE_CLASS, Type.BOOLEAN_TYPE}
    );

    Signature SIGNATURE_COPIER$copy = new Signature("copy", Constants.TYPE_OBJECT, new Type[]{
            Constants.TYPE_OBJECT, Constants.TYPE_CONVERTER,
    });

    Signature SIGNATURE_COPIER$copyInto = new Signature("copyInto", Type.VOID_TYPE, new Type[]{
            Constants.TYPE_OBJECT, Constants.TYPE_OBJECT, Constants.TYPE_CONVERTER
    });


}
