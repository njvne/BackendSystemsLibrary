package adapters.in.api.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.EntityTag;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;


@Provider
public class CacheFilter implements ContainerResponseFilter
{

    @Inject
    ObjectMapper objectMapper;

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
    {
        if(!"GET".equalsIgnoreCase(requestContext.getMethod()) || responseContext.getStatus() != Response.Status.OK.getStatusCode())
        {
            return;
        }

        Object ent = responseContext.getEntity();
        if(ent == null){return;}

        EntityTag etag = new EntityTag(generateEtag(ent), true);
        responseContext.getHeaders().putSingle("ETag", etag);

        String ifNoneMatch = requestContext.getHeaderString("If-None-Match");
        if (ifNoneMatch != null && etag.equals(new EntityTag(ifNoneMatch, true))) {
            responseContext.setStatusInfo(Response.Status.NOT_MODIFIED);
            responseContext.setEntity(null);
            return;
        }

        String path = requestContext.getUriInfo().getPath();
        if (path.contains("/users")) {
            responseContext.getHeaders().putSingle("Cache-Control", "private, max-age=60");
        } else {
            responseContext.getHeaders().putSingle("Cache-Control", "public, max-age=300");
        }

    }


    private String generateEtag(Object obj)
    {
        try
        {
            String jsonformat = objectMapper.writeValueAsString(obj);
            MessageDigest mess = MessageDigest.getInstance("MD5");
            byte[] digested = mess.digest(jsonformat.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(digested);
        }
        catch(Exception e)
        {
            return String.valueOf(obj.hashCode());
        }

    }
}