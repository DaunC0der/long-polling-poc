package org.acme.getting.started;

import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.container.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

@Provider
@PreMatching
public class Filter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger log = Logger.getLogger(Filter.class);

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        log.info(containerRequestContext.getUriInfo().getAbsolutePath());
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        log.info(containerResponseContext.getEntity());
    }
}
