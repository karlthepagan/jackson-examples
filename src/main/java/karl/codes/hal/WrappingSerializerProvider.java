package karl.codes.hal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;

import java.io.IOException;

/**
 * Created by karl on 8/14/2016.
 */
public class WrappingSerializerProvider extends DefaultSerializerProvider {

    public WrappingSerializerProvider() {
        super();
    }

    public WrappingSerializerProvider(WrappingSerializerProvider src) {
        super(src);
    }

    protected WrappingSerializerProvider(SerializerProvider src, SerializationConfig config,
                                         SerializerFactory f) {
        super(src, config, f);
    }

    @Override
    public DefaultSerializerProvider copy()
    {
        if (getClass() != WrappingSerializerProvider.class) {
            return super.copy();
        }
        return new WrappingSerializerProvider(this);
    }

    @Override
    public WrappingSerializerProvider createInstance(SerializationConfig config, SerializerFactory jsf) {
        return new WrappingSerializerProvider(this, config, jsf);
    }

    @Override
    public void serializeValue(JsonGenerator gen, Object value) throws IOException {
        super.serializeValue(gen, RootOutputWrap.wrap(value));
    }

    @Override
    public void serializeValue(JsonGenerator gen, Object value, JavaType rootType) throws IOException {
        super.serializeValue(gen, RootOutputWrap.wrap(value), RootOutputWrap.wrapType(rootType, getTypeFactory()));
    }

    @Override
    public void serializeValue(JsonGenerator gen, Object value, JavaType rootType, JsonSerializer<Object> ser) throws IOException {
        // TODO wrap ser?
        super.serializeValue(gen, RootOutputWrap.wrap(value), RootOutputWrap.wrapType(rootType, getTypeFactory()), ser);
    }

    @Override
    public void serializePolymorphic(JsonGenerator gen, Object value, JavaType rootType, JsonSerializer<Object> valueSer, TypeSerializer typeSer) throws IOException {
        // TODO wrap valueSer?
        // TODO wrap typeSer?
        super.serializePolymorphic(gen, RootOutputWrap.wrap(value), RootOutputWrap.wrapType(rootType, getTypeFactory()), valueSer, typeSer);
    }
}
