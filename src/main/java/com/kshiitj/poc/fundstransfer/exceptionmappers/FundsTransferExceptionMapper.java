package com.kshiitj.poc.fundstransfer.exceptionmappers;

import com.kshiitj.poc.fundstransfer.domain.FundsTransferFailedResponse;
import com.kshiitj.poc.fundstransfer.exceptions.FundsTransferException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class FundsTransferExceptionMapper implements ExceptionMapper<FundsTransferException> {
    @Override
    public Response toResponse(FundsTransferException e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new FundsTransferFailedResponse(e.getStatus(),e.getMessage())).build();
    }
}
