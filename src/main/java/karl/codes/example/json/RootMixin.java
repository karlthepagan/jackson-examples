package karl.codes.example.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import karl.codes.example.Document;
import karl.codes.example.Root;

import java.util.List;
import java.util.Map;

/**
 * Created by karl on 8/13/2016.
 */
public abstract class RootMixin extends Root {
    @JsonUnwrapped
    public abstract Map<String, Document> getDocuments();

    @JsonUnwrapped
    public abstract void setDocuments(Map<String, Document> documents);

    @JsonIgnore
    public abstract List<String> getLinks();
}
