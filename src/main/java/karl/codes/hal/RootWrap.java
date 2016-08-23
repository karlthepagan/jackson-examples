package karl.codes.hal;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
* Created by karl on 8/21/16.
*/
public class RootWrap<T> {
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
