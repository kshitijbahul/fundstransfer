package com.kshiitj.poc.fundstransfer.exceptionmappers;

import com.kshiitj.poc.fundstransfer.domain.ErrorResponse;
import com.kshiitj.poc.fundstransfer.exceptions.TransactionNotFoundException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class TransactionNotFoundExceptionMapper implements ExceptionMapper<TransactionNotFoundException> {

    @Override
    public Response toResponse(TransactionNotFoundException e) {
        return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse(e.getMessage())).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}
