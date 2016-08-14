package util;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * Lambda handler implementation delegating the request to the Jersey's {@link ServletContainer}.
 * The handler creates a dummy HTTP request from the pass-through lambda parameters, then it uses a dummy HTTP response
 * object. Also the handler adds a filter to capture the response entity, and deletes it from the response, so that
 * Jersey does not waste time on serializing it, because this is something AWS already does.
 */
public class JerseyRequestHandler implements RequestHandler<Map<String, Object>, Object> {

    private final ServletContainer jersey;

    public JerseyRequestHandler(Class<?>... classes) {
        this(new HashSet<Class<?>>(Arrays.asList(classes)));
    }

    public JerseyRequestHandler(Set<Class<?>> classes) {
        this(new ResourceConfig(classes));
    }

    public JerseyRequestHandler(ResourceConfig rs) {
        try {
            rs.registerClasses(CapturingContainerResponseFilter.class);
            jersey = new ServletContainer(rs);
            jersey.init(new ServletConfig() {
                public String getServletName() {
                    return null;
                }
                public ServletContext getServletContext() {
                    return new DummyServletContext();
                }
                public String getInitParameter(String name) {
                    return null;
                }
                public Enumeration<String> getInitParameterNames() {
                    return Collections.enumeration(Collections.emptySet());
                }
            });
        } catch (ServletException e) {
            throw new AssertionError("Failed to initialize lambda handler", e);
        }
    }

    @Override
    public Object handleRequest(Map<String, Object> input, Context context) {
        try {
            LambdaHttpServletRequest request = new LambdaHttpServletRequest(input);
            InMemoryHttpServletResponse response = new InMemoryHttpServletResponse();
            jersey.service(request, response);
            return CapturingContainerResponseFilter.getEntity() != null ?
                CapturingContainerResponseFilter.getEntity() :
                (response.getStatus() + ": " + response.toString());
        } catch (Exception e) {
            return "500: " + e.getMessage();
        }
    }
}
