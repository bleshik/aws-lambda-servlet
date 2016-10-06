package util;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
@SuppressWarnings("unchecked")
public class JerseyRequestHandlerSpec {

    @Path("/")
    public static class TestResource {
        @GET
        @Path("test")
        public String test() {
            return "It works!";
        }
    }

    @Test
    public void handleRequest() throws Exception {
        assertEquals(
            new TestResource().test(),
            new JerseyRequestHandler(TestResource.class).handleRequest(new HashMap<String, Object>() {{
                put("input", new HashMap<String, Object>() {{
                    put("body", null);
                    put("path", "/test");
                    put("httpMethod", "GET");
                    put("headers", new HashMap<String, String>());
                    put("queryStringParameters", new HashMap<String, String>());
                    put("requestContext", new HashMap<String, String>(){{
                        put("sourceIp", "test-invoke-source-ip");
                    }});
                }});
            }}, null).get("body")
        );
    }
}
