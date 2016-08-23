package karl.codes.hal

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory
import karl.codes.hal.example.RootMixin
import karl.codes.hal.example.Simple
import spock.lang.Specification
import spock.lang.Unroll

import static org.hamcrest.Matchers.instanceOf
import static spock.util.matcher.HamcrestSupport.that

/**
 * Created by karl on 8/22/16.
 */
class NewRootWrapTest extends Specification {
    static standardJson = new ObjectMapper()
    static mixinJson = new ObjectMapper()
            .addMixIn(RootInputWrap.class, RootMixin.class)
            .addMixIn(RootOutputWrap.class, RootMixin.class)
    static injectingJson = new ObjectMapper(null, new WrappingSerializerProvider(), new WrappingDeserializationContext(BeanDeserializerFactory.instance))
    static injMixJson = new ObjectMapper(null, new WrappingSerializerProvider(), new WrappingDeserializationContext(BeanDeserializerFactory.instance))
            .addMixIn(RootInputWrap.class, RootMixin.class)
            .addMixIn(RootOutputWrap.class, RootMixin.class)

    static directType = new TypeReference<Simple>(){}
    static wrappedType = new TypeReference<RootInputWrap<Simple>>() {}


    @Unroll('root wrap case #desc')
    def testRootWrap() {
        when:
        if(skip) {
            return;
        }
        def ser = json.readValue(data, type)
        def doc = model.inject(ser,{a,v -> a[v]})
        String txt = json.writeValueAsString(ser)
        JsonNode out = json.readTree(txt)
        JsonNode outDoc = child.inject(out,{a,v -> a[v]})


        then:
        that doc, instanceOf(Simple)
        outDoc[k].asText() == v

        where:
        desc | skip | json | type | child | model | data | k | v
        1 | true | standardJson | wrappedType | [] | ['body'] | '''{
            "key": "key",
            "value": "value"
        }''' | "key" | "key"
        2 | false | mixinJson | wrappedType | ['body'] | ['body'] | '''{
            "body": {
                "key": "key",
                "value": "value"
            }
        }''' | "key" | "key"
        3 | true | injectingJson | directType| [] | [] | '''{
            "key": "key",
            "value": "value"
        }''' | "key" | "key"
        4 | false | injMixJson | directType| ['body'] | [] | '''{
            "body": {
                "key": "key",
                "value": "value"
            }
        }''' | "key" | "key"
    }
}
