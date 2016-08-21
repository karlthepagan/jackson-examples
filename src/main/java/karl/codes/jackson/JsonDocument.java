package karl.codes.jackson;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by karl on 8/13/2016.
 */
public class JsonDocument {
    private JsonNode data;

    @JsonIgnore
    public JsonNode getData() {
        return data;
    }

    @JsonIgnore
    public void setData(JsonNode data) {
        this.data = data;
    }

    @JsonAnyGetter
    public Map<String,Object> getValues() {
        Map<String,Object> values = new HashMap<>();
        Iterator<Map.Entry<String,JsonNode>> iter = data.fields();
        while(iter.hasNext()) {
            Map.Entry<String,JsonNode> e = iter.next();
            values.put(e.getKey(),e.getValue());
        }
        return values;
    }

    @JsonCreator
    public static JsonDocument wrap(JsonNode tree) {
        JsonDocument doc = new JsonDocument();
        doc.setData(tree);
        return doc;
    }
}
