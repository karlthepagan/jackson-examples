package karl.codes.example;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * Created by karl on 8/13/2016.
 */
public class Root implements Resource {
    private URI baseUri; // fiction
    private Map<String,Document> documents;
    private List<String> links;

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

    public void setLinks(List<String> links) {
        this.links = links;
    }

    @Override
    public List<String> getLinks() {
        return links;
    }
}
