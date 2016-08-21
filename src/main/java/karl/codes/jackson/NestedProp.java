package karl.codes.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
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
    private static AnnotationIntrospector MASK_IGNORE = new FilteredAnnotationIntrospector(JsonIgnore.class);

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

        SerializationConfig serConfig = ((SerializationConfig)config).with(MASK_IGNORE);

        // overloading annotation definition of "namespace" - it determines our container accessor and serialized type
        BeanDescription parentBean = ci.forSerialization(serConfig, declaringClass.getType(), config);
        String namespace = propDef.getFullName().getNamespace();
        BeanPropertyDefinition namespaceDef = findProperty(parentBean.findProperties(), namespace);
        if(namespaceDef == null) {
            // TODO describe primaryMixin
            throw new IllegalArgumentException(parentBean.getClassInfo().getType() + " does not contain " + namespace);
        }
        JavaType beanType = namespaceDef.getAccessor().getType();
        // TODO provide additional constraint using type? assert type == namespaceAccessor.getAccessor().getType()

        // get property type
        BeanDescription bean = ci.forSerialization(serConfig, beanType, config);
        String propName = propDef.getName();
        propDef = findProperty(bean.findProperties(), propName); // TODO match more precisely?
        if(propDef == null) {
            // TODO describe primaryMixin
            throw new IllegalArgumentException(bean.getClassInfo().getType() + " does not contain " + propName);
        }
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
