package com.kshiitj.poc.fundstransfer.exceptionmappers;

import com.kshiitj.poc.fundstransfer.exceptions.NoAccountAvailableException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NoAccountAvailableExceptionMapper implements ExceptionMapper<NoAccountAvailableException> {

    @Override
    public Response toResponse(NoAccountAvailableException e) {
        return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}
