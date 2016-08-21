package karl.codes.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DeserializerFactory;

import java.io.IOException;

/**
 * Created by karl on 8/14/16.
 */
public class WrappingDeserializationContext extends DefaultDeserializationContext implements JsonWrap {
    /**
     * Default constructor for a blueprint object, which will use the standard
     * {@link com.fasterxml.jackson.databind.deser.DeserializerCache}, given factory.
     */
    public WrappingDeserializationContext(DeserializerFactory df) {
        super(df, null);
    }

    protected WrappingDeserializationContext(WrappingDeserializationContext src,
                   DeserializationConfig config, JsonParser jp, InjectableValues values) {
        super(src, config, jp, values);
    }

    protected WrappingDeserializationContext(WrappingDeserializationContext src) { super(src); }

    protected WrappingDeserializationContext(WrappingDeserializationContext src, DeserializerFactory factory) {
        super(src, factory);
    }

    @Override
    public DefaultDeserializationContext copy() {
        if (getClass() != WrappingDeserializationContext.class) {
            return super.copy();
        }
        return new WrappingDeserializationContext(this);
    }

    @Override
    public DefaultDeserializationContext createInstance(DeserializationConfig config,
                                                        JsonParser jp, InjectableValues values) {
        return new WrappingDeserializationContext(this, config, jp, values);
    }

    @Override
    public DefaultDeserializationContext with(DeserializerFactory factory) {
        return new WrappingDeserializationContext(this, factory);
    }

    public JsonDeserializer<?> handleSecondaryContextualization(JsonDeserializer<?> deser,
                                                                BeanProperty prop, JavaType type)
            throws JsonMappingException
    {
        deser = super.handleSecondaryContextualization(deser, prop, type);

        if(prop == null && !JsonWrap.bypassWrap(type)) {
            JavaType rootInput = getTypeFactory().constructParametricType(JsonWrap.RootInput.class,type);
            JsonDeserializer<Object> rootDeser = _cache.findValueDeserializer(this,
                    _factory, rootInput);
            if(rootDeser == null) {
                return deser;
            }
            deser = super.handleSecondaryContextualization(rootDeser, null, rootInput);

            deser = new UnwrapDeserializer<>(deser);
        }

        return deser;
    }

    private static class UnwrapDeserializer<T> extends JsonDeserializer<Object> {
        private final JsonDeserializer<?> parent;

        public UnwrapDeserializer(JsonDeserializer<?> parent) {
            this.parent = parent;
        }

        @Override
        public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return ((JsonWrap.Root<T>)parent.deserialize(p, ctxt)).getBody();
        }
    }
}