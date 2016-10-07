package util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * Lambda handler implementation delegating the request to the Jersey's {@link ServletContainer}.
 */
public class JerseyRequestHandler extends ServletRequestHandler<ServletContainer> {
    public JerseyRequestHandler(Class<?>... classes) {
        this(new HashSet<Class<?>>(Arrays.asList(classes)));
    }

    public JerseyRequestHandler(Set<Class<?>> classes) {
        this(new ResourceConfig(classes));
    }

    public JerseyRequestHandler(ResourceConfig rs) {
        super(new ServletContainer(rs));
    }
}
