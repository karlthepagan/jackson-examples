package karl.codes.jackson;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.ser.VirtualBeanPropertyWriter;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.util.List;

/**
 * Created by karl on 8/14/2016.
 */
public interface JsonWrap {
    class Root<T> {
        private T body;

        @JsonGetter
        @JsonUnwrapped
        public void setBody(T body) {
            this.body = body;
        }

        @JsonGetter
        @JsonUnwrapped
        public T getBody() {
            return body;
        }
    }

    class RootInput<T> extends Root<T> {
        public static <T> RootInput<T> wrap(T body) {
            RootInput<T> root = new RootInput<>();
            root.setBody(body);
            return root;
        }

        public static JavaType wrapType(Class<?> type, TypeFactory tf) {
            return tf.constructParametricType(RootInput.class, type);
        }

        public static JavaType wrapType(JavaType type, TypeFactory tf) {
            return tf.constructParametricType(RootInput.class, type);
        }
    }

    class RootOutput<T> extends Root<T> {
        public static <T> RootOutput<T> wrap(T body) {
            RootOutput<T> root = new RootOutput<>();
            root.setBody(body);
            return root;
        }

        public static JavaType wrapType(Class<?> type, TypeFactory tf) {
            return tf.constructParametricType(RootOutput.class, type);
        }

        public static JavaType wrapType(JavaType type, TypeFactory tf) {
            return tf.constructParametricType(RootOutput.class, type);
        }
    }

    /**
     * VirtualBeanProperty which is retrieved from the
     */
    class Appender extends VirtualBeanPropertyWriter {
        private BeanPropertyDefinition _propDef;

        public Appender() {
            super();
        }

        public Appender(MapperConfig<?> config, AnnotatedClass declaringClass,
                        BeanPropertyDefinition propDef, JavaType declaredType, JavaType serializedType) {
            super(propDef, declaringClass.getAnnotations(), declaredType, /*JsonSerializer*/null, /*TypeSerializer*/ null, serializedType, /*JsonInclude.Value*/null);

            this._propDef = propDef;
        }

        @Override
        protected Object value(Object bean, JsonGenerator gen, SerializerProvider prov) throws Exception {
            if(bean instanceof Root) { // TODO type specific? replace with @JsonAppend.Prop.type!
                bean = ((Root)bean).getBody(); // TODO type specific? replace with @JsonAppend.Prop.namespace!
                AnnotatedMember accessor = _propDef.getAccessor();
                if (accessor.getDeclaringClass().isAssignableFrom(bean.getClass())) {
                    return accessor.getValue(bean);
                }
            }

            return null; // TODO empty value node?
        }

        @Override
        public VirtualBeanPropertyWriter withConfig(MapperConfig<?> config, AnnotatedClass declaringClass, BeanPropertyDefinition propDef, JavaType declaredType) {
            JavaType beanType = declaringClass.getType().getBindings().getBoundType(0);
            // TODO get property type
            ClassIntrospector ci = config.getClassIntrospector();
            BeanDescription bean = ci.forSerialization((SerializationConfig)config,beanType,config); // TODO CRITICAL XXX suppress @JsonIgnore!
            propDef = findProperty(bean.findProperties(), propDef); // TODO match more precisely?
            JavaType serializedType = propDef.getField().getType(); // TODO miss in find will NPE

            return new Appender(config, declaringClass, propDef, declaredType, serializedType);
        }

        private BeanPropertyDefinition findProperty(List<BeanPropertyDefinition> properties, BeanPropertyDefinition src) {
            String name = src.getName();
            for(BeanPropertyDefinition prop : properties) {
                if(prop.getName().equals(name)) {
                    return prop;
                }
            }

            return src;
        }
    }
}
