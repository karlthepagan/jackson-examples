package karl.codes.hal;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;

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
}
