package karl.codes.jackson;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by karl on 8/13/2016.
 */
public class JsonDocument {
    private static final Collector<Map.Entry<String,?>, ?, Map<String,Object>> MAPPER =
            Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue);
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
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                data.fields(), Spliterator.ORDERED), false)
                .collect(MAPPER);
    }

    @JsonCreator
    public static JsonDocument wrap(JsonNode tree) {
        JsonDocument doc = new JsonDocument();
        doc.setData(tree);
        return doc;
    }
}
