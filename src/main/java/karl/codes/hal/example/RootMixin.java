package karl.codes.hal.example;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import karl.codes.hal.RootInputWrap;

/**
 * Created by karl on 8/22/16.
 */
public abstract class RootMixin<T> extends RootInputWrap<T> {
    @JsonUnwrapped(enabled = false)
    public abstract void setBody(T body);

    @JsonUnwrapped(enabled = false)
    public abstract T getBody();
}
