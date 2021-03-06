package karl.codes.example

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import karl.codes.example.json.DocumentMixin
import karl.codes.example.json.RootMixin
import karl.codes.jackson.JsonDocument
import spock.lang.Specification

import static org.hamcrest.Matchers.*
import static spock.util.matcher.HamcrestSupport.*

/**
 * Created by karl on 8/13/2016.
 */
class DocumentTest extends Specification {
    def json = new ObjectMapper()
        .addMixIn(Root.class, RootMixin.class)
        .addMixIn(Document.class, DocumentMixin.class)

    def 'extra fields deserialized'() {
        when:
        Root ser = json.readValue(data, Root.class)
        Document doc = ser.documents.entrySet().first().value
        JsonNode out = json.readTree(json.writeValueAsString(ser))
        JsonNode outDoc = out.documents.a

        then:
        that doc, instanceOf(Document)

        doc.name == name
        doc.metadata == metadata
        keys.eachWithIndex{ key, int i ->
            assert that(doc.data, instanceOf(JsonDocument))
            assert that(doc.data.data, instanceOf(JsonNode))
            assert (doc.data.data as JsonNode).get(key)?.asText() == values[i]
            assert i < 2 || outDoc.get(key)?.asText() == values[i]
        }

        where:
        data | name | metadata  | keys | values
        '''{
            "baseUri": "https://karl.codes",
            "documents": {
                "a": {
                    "name": "a",
                    "metadata": "m",
                    "a": "1",
                    "b": "2"
                }
            }
        }''' | 'a'  | 'm' |
        ['name',  'metadata', 'a', 'b'] |
        [null,    null,       '1', '2']
    }
}
