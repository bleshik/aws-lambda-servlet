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
                put("body-json", new HashMap());
                put("params", new HashMap<String, Object>(){{
                    put("path", new HashMap<String, String>(){{
                        put("path1", "test");
                    }});
                    put("querystring", new HashMap<String, String>());
                    put("header", new HashMap<String, String>());
                }});
                put("context", new HashMap<String, String>(){{
                    put("http-method", "GET");
                    put("source-ip", "test-invoke-source-ip");
                    put("resource-path", "/{path1}");
                }});
            }}, null)
        );
    }
}
