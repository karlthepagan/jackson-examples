package karl.codes.hal;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
* Created by karl on 8/21/16.
*/
public class RootOutputWrap<T> extends RootWrap<T> {
    public static <T> RootOutputWrap<T> wrap(T body) {
        RootOutputWrap<T> root = new RootOutputWrap<>();
        root.setBody(body);
        return root;
    }

    public static JavaType wrapType(Class<?> type, TypeFactory tf) {
        if(JsonWrap.bypassWrap(type)) {
            return tf.constructType(type);
        }
        return tf.constructParametricType(RootOutputWrap.class, type);
    }

    public static JavaType wrapType(JavaType type, TypeFactory tf) {
        if(JsonWrap.bypassWrap(type)) {
            return type;
        }
        return tf.constructParametricType(RootOutputWrap.class, type);
    }
}
