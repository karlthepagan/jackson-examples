package karl.codes.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by karl on 8/18/16.
 */
public class FilteredAnnotationIntrospector extends JacksonAnnotationIntrospector {
    private final Set<Class<?>> ignored;

    public FilteredAnnotationIntrospector(Class<? extends Annotation> ... ignored) {
        this.ignored = new HashSet<Class<?>>();

        for(Class<? extends Annotation> a : ignored) {
            this.ignored.add(a);
        }
    }

    protected <A extends Annotation> A _findAnnotation(Annotated annotated,
                                                       Class<A> annoClass) {
        if(ignored.contains(annoClass)) {
            return null;
        }

        return annotated.getAnnotation(annoClass);
    }
}
