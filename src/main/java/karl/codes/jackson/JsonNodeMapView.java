package karl.codes.jackson;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by karl on 8/14/16.
 */
public class JsonNodeMapView implements Map<String,Object> {
    private final JsonNode data;

    public JsonNodeMapView(JsonNode data) {
        this.data = data;
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public boolean isEmpty() {
        return data.size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        if(key instanceof String) {
            return data.get((String)key) != null;
        }

        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        if(value instanceof JsonNode) {
            for(JsonNode n : data) {
                if(n.equals(value)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public JsonNode get(Object key) {
        if(key instanceof String) {
            return data.get((String)key);
        }

        return null;
    }

    @Override
    public Object put(String key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<Object> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Map.Entry<String, Object>> entrySet() {
        throw new UnsupportedOperationException();
    }
}
