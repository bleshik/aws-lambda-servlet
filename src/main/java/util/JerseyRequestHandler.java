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

    public JerseyRequestHandler(String contextPath, Class<?>... classes) {
        this(contextPath, new HashSet<Class<?>>(Arrays.asList(classes)));
    }

    public JerseyRequestHandler(String contextPath, Set<Class<?>> classes) {
        this(contextPath, new ResourceConfig(classes));
    }

    public JerseyRequestHandler(String contextPath, ResourceConfig rs) {
        this(contextPath, rs, Optional.empty(), Collections.emptyList());
    }

    public JerseyRequestHandler(String contextPath, ResourceConfig rs, Optional<? extends SessionManager> sm, List<Filter> filters) {
        super(contextPath, new ServletContainer(rs), sm, filters);
    }

    public JerseyRequestHandler(String contextPath, ResourceConfig rs, Optional<? extends SessionManager> sm, Filter... filters) {
        super(contextPath, new ServletContainer(rs), sm, filters);
    }

}
