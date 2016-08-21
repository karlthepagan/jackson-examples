package karl.codes.example;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import javax.print.Doc;
import java.net.URI;
import java.util.Map;

/**
 * Created by karl on 8/13/2016.
 */
public class Root {
    private Document document;

    @JsonUnwrapped
    public Document getDocument() {
        return document;
    }

    @JsonUnwrapped
    public void setDocument(Document document) {
        this.document = document;
    }
}
