package karl.codes.example

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import karl.codes.example.json.DocumentMixin
import karl.codes.example.json.RootMixin
import karl.codes.example.json.RootWrapMixin
import karl.codes.jackson.*
import spock.lang.Specification

import static karl.codes.Groovy.*
import static org.hamcrest.Matchers.instanceOf
import static spock.util.matcher.HamcrestSupport.that

/**
 * Created by karl on 8/19/16.
 */
class DirectRootWrapTest extends Specification {
    def json = new ObjectMapper()
            .addMixIn(Root.class, RootMixin.class)
            .addMixIn(Document.class, DocumentMixin.class)
            .addMixIn(RootInputWrap.class, RootWrapMixin.class)
            .addMixIn(RootOutputWrap.class, RootWrapMixin.class)

    def 'extra fields deserialized'() {
        when:
        RootInputWrap<Root> root = json.readValue(data, new TypeReference<RootInputWrap<Root>>(){})
        Root ser = root.getBody()
        ser.links = ['link1']
        Document doc = ser.documents.entrySet().first().value
        String outStr = json.writeValueAsString(ser)
        JsonNode out = json.readTree(outStr)
        JsonNode outDoc = out.data.documents.a

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
        js([
            data: [
                baseUri: 'https://karl.codes',
                documents: [
                    a: [
                        name: 'a',
                        metadata: 'm',
                        a: '1',
                        b: '2'
                    ]
                ]
            ]
        ]) | 'a'  | 'm' |
        ['name',  'metadata', 'a', 'b'] |
        [null,    null,       '1', '2']
    }
}
