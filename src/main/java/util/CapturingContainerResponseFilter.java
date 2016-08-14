package util;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 * Stores response entity in a {@link ThreadLocal} object, and deletes the entity from the response. Thus, the handler
 * will be able to return the entity, and Jersey will only have to serialize null, so it does not waste time on
 * serializing the entity. This is done, because AWS will serialize the entity for us.
 */
@Provider
public class CapturingContainerResponseFilter implements ContainerResponseFilter {

    private static ThreadLocal<Object> entity = new ThreadLocal<>();

    public static Object getEntity() { return entity.get(); }

    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        if (responseContext.hasEntity()) {
            entity.set(responseContext.getEntity());
            responseContext.setEntity(null);
        }
    }
}
