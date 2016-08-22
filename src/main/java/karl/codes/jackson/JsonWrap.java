package karl.codes.jackson;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Created by karl on 8/14/2016.
 */
public interface JsonWrap {
    public static boolean bypassWrap(Class<?> type) {
        // bypass root wrap for JsonNode
        return type.equals(JsonNode.class);
    }

    public static boolean bypassWrap(JavaType type) {
        // bypass root wrap for JsonNode
        return type.getRawClass().equals(JsonNode.class);
    }

    public static class Root<T> {
        private T body;

        @JsonUnwrapped
        public void setBody(T body) {
            this.body = body;
        }

        @JsonUnwrapped
        public T getBody() {
            return body;
        }

    }

    public static class RootInput<T> extends Root<T> {
        public static <T> RootInput<T> wrap(T body) {
            RootInput<T> root = new RootInput<>();
            root.setBody(body);
            return root;
        }

        public static JavaType wrapType(Class<?> type, TypeFactory tf) {
            if(bypassWrap(type)) {
                return tf.constructType(type);
            }
            return tf.constructParametricType(RootInput.class, type);
        }

        public static JavaType wrapType(JavaType type, TypeFactory tf) {
            if(bypassWrap(type)) {
                return type;
            }
            return tf.constructParametricType(RootInput.class, type);
        }
    }

    public static class RootOutput<T> extends Root<T> {
        public static <T> RootOutput<T> wrap(T body) {
            RootOutput<T> root = new RootOutput<>();
            root.setBody(body);
            return root;
        }

        public static JavaType wrapType(Class<?> type, TypeFactory tf) {
            if(bypassWrap(type)) {
                return tf.constructType(type);
            }
            return tf.constructParametricType(RootOutput.class, type);
        }

        public static JavaType wrapType(JavaType type, TypeFactory tf) {
            if(bypassWrap(type)) {
                return type;
            }
            return tf.constructParametricType(RootOutput.class, type);
        }
    }
}
