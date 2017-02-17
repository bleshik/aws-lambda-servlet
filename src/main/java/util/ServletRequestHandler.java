package util;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lambda handler implementation delegating the request to the given Servlet instance.
 */
public class ServletRequestHandler<T extends Servlet> implements RequestHandler<Map<String, Object>, Object> {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected final T servlet;
    protected final Optional<? extends SessionManager> sm;
    protected final List<Filter> filters;

    public ServletRequestHandler(T servlet) {
        this(servlet, Optional.empty(), Collections.emptyList());
    }

    public ServletRequestHandler(T servlet, Optional<? extends SessionManager> sm, Filter... filters) {
        this(servlet, sm, Arrays.asList(filters));
    }

    public ServletRequestHandler(T servlet, Optional<? extends SessionManager> sm, List<Filter> filters) {
        try {
            this.filters = filters;
            this.sm      = sm;
            this.servlet = servlet;
            this.servlet.init(new ServletConfig() {
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

    protected FilterChain createFilterChain(List<Filter> filters) {
        return new FilterChain() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
                if (filters.isEmpty()) {
                    servlet.service(request, response);
                } else {
                    filters.get(0).doFilter(
                        request,
                        response,
                        createFilterChain(
                            filters.size() > 1 ? filters.subList(1, filters.size()) : Collections.emptyList()
                        )
                    );
                }
            }
        };
    }

    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        try {
            LambdaHttpServletRequest request = new LambdaHttpServletRequest(input, sm);
            InMemoryHttpServletResponse response = new InMemoryHttpServletResponse();
            createFilterChain(filters).doFilter(request, response);
            return new HashMap<String, Object>() {{
                put("statusCode", response.getStatus());
                put("headers", new HashMap<String, String>(){{
                    for(String h : response.getHeaderNames()) {
                        put(h, response.getHeader(h));
                    }
                }});
                put("body", response.toString());
            }};
        } catch (Exception e) {
            logger.error("Failed to handle the request", e);
            return new HashMap<String, Object>() {{
                put("statusCode", 500);
                put("headers", new HashMap<String, String>(){{
                    put("Content-Type", "text/plain");
                }});
                put("body", "Internal Server Error: " + e.getMessage());
            }};
        }
    }


}
