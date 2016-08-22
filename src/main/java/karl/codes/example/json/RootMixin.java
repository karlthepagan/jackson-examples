package karl.codes.example.json;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import karl.codes.example.Container;
import karl.codes.example.Document;

import java.util.Map;

/**
 * Created by karl on 8/13/2016.
 */
public abstract class RootMixin extends Container {
    @JsonUnwrapped(enabled = false)
    public abstract Document getDocument();

    @JsonUnwrapped(enabled = false)
    public abstract void setDocument(Document document);
}
