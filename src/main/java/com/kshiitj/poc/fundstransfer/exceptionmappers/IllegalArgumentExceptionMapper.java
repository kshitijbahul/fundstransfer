package com.kshiitj.poc.fundstransfer.exceptionmappers;

import com.kshiitj.poc.fundstransfer.domain.FundsTransferFailedResponse;
import com.kshiitj.poc.fundstransfer.domain.TransferRequestStatus;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {

    @Override
    public Response toResponse(IllegalArgumentException e) {
        return Response.status(Response.Status.BAD_REQUEST).entity(new FundsTransferFailedResponse(TransferRequestStatus.INVALID_TRANSFER_REQUEST,e.getMessage())).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}