package karl.codes.example

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import karl.codes.example.json.DocumentMixin
import karl.codes.example.json.RootMixin
import spock.lang.Specification

import static org.hamcrest.Matchers.*
import static spock.util.matcher.HamcrestSupport.*

/**
 * Created by karl on 8/13/2016.
 */
class DocumentTest extends Specification {
    static standard = new ObjectMapper()
    static mixins = new ObjectMapper()
        .addMixIn(Document.class, DocumentMixin.class)
        .addMixIn(Root.class, RootMixin.class)


    def 'template test'() {
        when:
        Root ser = json.readValue(data, Root.class)
        def doc = ser.document
        String txt = json.writeValueAsString(ser)
        JsonNode out = json.readTree(txt)
        JsonNode outDoc = child.inject(out,{a,v -> a[v]})


        then:
        that doc, instanceOf(Document)

        doc.name == name
        doc.metadata == metadata
        keys.eachWithIndex{ key, int i ->
            assert that(doc.data, instanceOf(Map))
            assert (doc.data as Map).get(key) == values[i]
            assert i < 2 || outDoc.get(key)?.asText() == values[i]
        }

        where:
        data | name | metadata  | keys | values | json | child
        '''{
            "name": "a",
            "metadata": "m",
            "data": {
                "a": "1",
                "b": "2"
            }
        }''' | 'a'  | 'm' |
        ['name',  'metadata', 'a', 'b'] |
        [null,    null,       '1', '2'] | standard | ['data']
        '''{
            "document": {
                "name": "a",
                "metadata": "m",
                "data": {
                    "a": "1",
                    "b": "2"
                }
            }
        }''' | 'a'  | 'm' |
        ['name',  'metadata', 'a', 'b'] |
        [null,    null,       '1', '2'] | mixins | ['document','data']
    }
}
