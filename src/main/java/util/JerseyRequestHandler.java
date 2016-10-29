package util;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.servlet.Filter;
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
        this(rs, Optional.empty(), Collections.emptyList());
    }

    public JerseyRequestHandler(ResourceConfig rs, Optional<? extends SessionManager> sm, List<Filter> filters) {
        super(new ServletContainer(rs), sm, filters);
    }

    public JerseyRequestHandler(ResourceConfig rs, Optional<? extends SessionManager> sm, Filter... filters) {
        super(new ServletContainer(rs), sm, filters);
    }

}
