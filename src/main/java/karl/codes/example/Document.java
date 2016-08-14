package karl.codes.example;

/**
 * Created by karl on 8/13/2016.
 */
public class Document {
    private String name;
    private String metadata;
    private Object data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
