package karl.codes.example.json;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import karl.codes.example.Resource;
import karl.codes.jackson.JsonWrap;
import karl.codes.jackson.NestedProp;

/**
 * Created by karl on 8/14/2016.
 */
@JsonAppend(prepend = true,
        props = @JsonAppend.Prop(value = NestedProp.class, namespace = "data", name = "links")
)
public class RootWrapMixin<T extends Resource> extends JsonWrap.Root<T> {
    @JsonGetter("data")
    public void setBody(T body) {
        super.setBody(body);
    }

    @JsonGetter("data")
    public T getBody() {
        return super.getBody();
    }
}
