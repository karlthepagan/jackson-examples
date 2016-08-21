package karl.codes.example.json;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import karl.codes.example.Document;

/**
 * Created by karl on 8/13/2016.
 */
public abstract class DocumentMixin extends Document {
    public abstract Object getData();

    public abstract void setData(Object data);
}
