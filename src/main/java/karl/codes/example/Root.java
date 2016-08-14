package karl.codes.example;

import java.net.URI;
import java.util.Map;

/**
 * Created by karl on 8/13/2016.
 */
public class Root {
    private URI baseUri; // fiction
    private Map<String,Document> documents;

    public URI getBaseUri() {
        return baseUri;
    }

    public void setBaseUri(URI baseUri) {
        this.baseUri = baseUri;
    }

    public Map<String, Document> getDocuments() {
        return documents;
    }

    public void setDocuments(Map<String, Document> documents) {
        this.documents = documents;
    }
}
