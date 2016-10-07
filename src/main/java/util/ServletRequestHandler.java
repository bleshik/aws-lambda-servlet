package util;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.Servlet;

/**
 * Lambda handler implementation delegating the request to the given Servlet instance.
 */
public class ServletRequestHandler<T extends Servlet> implements RequestHandler<Map<String, Object>, Object> {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected final T servlet;

    public ServletRequestHandler(T servlet) {
        try {
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

    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        try {
            LambdaHttpServletRequest request = new LambdaHttpServletRequest(input);
            InMemoryHttpServletResponse response = new InMemoryHttpServletResponse();
            servlet.service(request, response);
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
                put("body", "Internal Server Error");
            }};
        }
    }


}
