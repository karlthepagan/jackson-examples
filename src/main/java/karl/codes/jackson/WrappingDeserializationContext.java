package karl.codes.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DeserializerFactory;

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

        if(prop == null) {
            // TODO is this the right way to go?
            System.out.println("TODO wrap deserializer");
        }

        return deser;
    }
}