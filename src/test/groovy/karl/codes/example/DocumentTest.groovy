package karl.codes.example

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import karl.codes.example.json.DocumentMixin
import karl.codes.example.json.RootMixin
import karl.codes.example.json.RootWrapMixin
import karl.codes.jackson.JsonDocument
import karl.codes.jackson.RootInputWrap
import karl.codes.jackson.RootOutputWrap
import spock.lang.Specification

import static karl.codes.Groovy.js
import static org.hamcrest.Matchers.*
import static spock.util.matcher.HamcrestSupport.*

/**
 * Created by karl on 8/13/2016.
 */
class DocumentTest extends Specification {
    def json = new ObjectMapper() //null,null,new WrappingDeserializationContext(BeanDeserializerFactory.instance))
//        .setSerializerProvider(new WrappingSerializerProvider())
        .addMixIn(Root.class, RootMixin.class)
        .addMixIn(Document.class, DocumentMixin.class)
        .addMixIn(RootInputWrap.class, RootWrapMixin.class)
        .addMixIn(RootOutputWrap.class, RootWrapMixin.class)

    def 'extra fields deserialized'() {
        when:
        Root ser = json.readValue(data, Root.class)
        ser.links = ['link1']
        Document doc = ser.documents.entrySet().first().value
        String outStr = json.writeValueAsString(ser)
        JsonNode out = json.readTree(outStr)
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
        js([
            baseUri: 'https://karl.codes',
            documents: [
                a: [
                    name: 'a',
                    metadata: 'm',
                    a: '1',
                    b: '2'
                ]
            ]
        ]) | 'a'  | 'm' |
        ['name',  'metadata', 'a', 'b'] |
        [null,    null,       '1', '2']
    }
}
