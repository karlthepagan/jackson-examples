package karl.codes.example.json;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import karl.codes.example.Document;
import karl.codes.jackson.JsonDocument;

/**
 * Created by karl on 8/13/2016.
 */
public abstract class DocumentMixin extends Document {
    @JsonUnwrapped
    @JsonSerialize(as = JsonDocument.class)
    public abstract Object getData();

    @JsonUnwrapped
    @JsonDeserialize(as = JsonDocument.class)
    public abstract void setData(Object data);
}
