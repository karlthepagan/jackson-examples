package karl.codes.jackson;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.util.List;

/**
 * Created by karl on 8/14/2016.
 */
public interface JsonWrap {
    class Root<T> {
        private T body;

        @JsonGetter
        @JsonUnwrapped
        public void setBody(T body) {
            this.body = body;
        }

        @JsonGetter
        @JsonUnwrapped
        public T getBody() {
            return body;
        }
    }

    class RootInput<T> extends Root<T> {
        public static <T> RootInput<T> wrap(T body) {
            RootInput<T> root = new RootInput<>();
            root.setBody(body);
            return root;
        }

        public static JavaType wrapType(Class<?> type, TypeFactory tf) {
            return tf.constructParametricType(RootInput.class, type);
        }

        public static JavaType wrapType(JavaType type, TypeFactory tf) {
            return tf.constructParametricType(RootInput.class, type);
        }
    }

    class RootOutput<T> extends Root<T> {
        public static <T> RootOutput<T> wrap(T body) {
            RootOutput<T> root = new RootOutput<>();
            root.setBody(body);
            return root;
        }

        public static JavaType wrapType(Class<?> type, TypeFactory tf) {
            return tf.constructParametricType(RootOutput.class, type);
        }

        public static JavaType wrapType(JavaType type, TypeFactory tf) {
            return tf.constructParametricType(RootOutput.class, type);
        }
    }
}
