package karl.codes.example.json;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import karl.codes.example.Resource;
import karl.codes.jackson.JsonWrap;

import java.util.List;

/**
 * Created by karl on 8/14/2016.
 */
@JsonPropertyOrder({"links","data"})
public abstract class RootWrapMixin<T extends Resource> extends JsonWrap.Root<T> {
    @JsonGetter("links")
    public List<String> getLinks() {
        return getBody().getLinks();
    }

    @JsonGetter("data")
    public abstract void setBody(T body);

    @JsonGetter("data")
    public abstract T getBody();
}
