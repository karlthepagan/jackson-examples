package karl.codes.example.json;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import karl.codes.example.Document;
import karl.codes.example.Root;

import java.net.URI;
import java.util.Map;

/**
 * Created by karl on 8/13/2016.
 */
public abstract class RootMixin extends Root {
    public abstract Map<String, Document> getDocuments();

    public abstract void setDocuments(Map<String, Document> documents);
}
