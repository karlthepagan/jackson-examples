package karl.codes.jackson;

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

import java.util.List;

/**
 * VirtualBeanProperty which is retrieved from a nested getter
 *
 * @author karl
 * @since 8/15/2016
 */
public class NestedProp extends VirtualBeanPropertyWriter {
    private AnnotatedMember _namespaceAccessor;
    private BeanPropertyDefinition _propDef;

    public NestedProp() {
        super();
    }

    public NestedProp(MapperConfig<?> config, AnnotatedClass declaringClass, BeanPropertyDefinition propDef,
                    JavaType declaredType, JavaType serializedType,
                      AnnotatedMember namespaceAccessor) {
        super(propDef, declaringClass.getAnnotations(), declaredType, /*JsonSerializer*/null, /*TypeSerializer*/ null, serializedType, /*JsonInclude.Value*/null);

        this._propDef = propDef;
        this._namespaceAccessor = namespaceAccessor;
    }

    @Override
    protected Object value(Object bean, JsonGenerator gen, SerializerProvider prov) throws Exception {
        if(_namespaceAccessor.getDeclaringClass().isAssignableFrom(bean.getClass())) {
            bean =_namespaceAccessor.getValue(bean);
            AnnotatedMember accessor = _propDef.getAccessor();
            if (accessor.getDeclaringClass().isAssignableFrom(bean.getClass())) {
                return accessor.getValue(bean);
            }
        }

        return null; // TODO empty value node?
    }

    @Override
    public VirtualBeanPropertyWriter withConfig(MapperConfig<?> config, AnnotatedClass declaringClass, BeanPropertyDefinition propDef, JavaType type) {
        ClassIntrospector ci = config.getClassIntrospector();

        // overloading annotation definition of "namespace" - it determines our container accessor and serialized type
        BeanDescription parentBean = ci.forSerialization((SerializationConfig)config,declaringClass.getType(),config);
        BeanPropertyDefinition namespaceDef = findProperty(parentBean.findProperties(), propDef.getFullName().getNamespace());
        // TODO null -> bad namespace
        JavaType beanType = namespaceDef.getAccessor().getType();
        // TODO provide additional constraint using type? assert type == namespaceAccessor.getAccessor().getType()

        // get property type
        BeanDescription bean = ci.forSerialization((SerializationConfig)config,beanType,config); // TODO CRITICAL XXX suppress @JsonIgnore!
        propDef = findProperty(bean.findProperties(), propDef.getName()); // TODO match more precisely?
        // TODO miss in find will NPE, better error return
        JavaType serializedType = propDef.getField().getType();

        // TODO declared vs serialized type mistakes?

        return new NestedProp(config, declaringClass, propDef, serializedType, serializedType, namespaceDef.getAccessor());
    }

    private BeanPropertyDefinition findProperty(List<BeanPropertyDefinition> properties, String name) {
        for(BeanPropertyDefinition prop : properties) {
            if(prop.getName().equals(name)) {
                return prop;
            }
        }

        return null;
    }
}
