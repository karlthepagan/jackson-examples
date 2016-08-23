package karl.codes.example.json;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import karl.codes.example.Resource;
import karl.codes.jackson.NestedProp;
import karl.codes.jackson.RootWrap;

/**
 * Created by karl on 8/14/2016.
 */
@JsonAppend(prepend = true,
        props = @JsonAppend.Prop(value = NestedProp.class, namespace = "data", name = "links")
)
public abstract class RootWrapMixin<T extends Resource> extends RootWrap<T> {
    @Override
    @JsonGetter("data")
    @JsonUnwrapped(enabled = false)
    public abstract void setBody(T body);

    @Override
    @JsonGetter("data")
    @JsonUnwrapped(enabled = false)
    public abstract T getBody();
}
