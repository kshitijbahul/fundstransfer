package com.kshiitj.poc.fundstransfer.resources;

import com.kshiitj.poc.fundstransfer.boundry.FundTransfers;
import com.kshiitj.poc.fundstransfer.domain.TransferRequest;
import com.kshiitj.poc.fundstransfer.exceptions.TransactionNotFoundException;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/funds/transfer")
@Produces(MediaType.APPLICATION_JSON)
public class FundsTransferResource {

    private final FundTransfers fundTransfers;

    @Inject
    FundsTransferResource(FundTransfers fundTransfers){
        this.fundTransfers=fundTransfers;
    }

    @POST
    public Response transferMoney(TransferRequest request)  {
        return Response.ok().entity(this.fundTransfers.transfer(request)).build();
    }
    @GET
    @Path("{transferId}")
    public Response getRequestStatus(@PathParam("transferId")UUID transferId) throws TransactionNotFoundException {
        return Response.ok().entity(this.fundTransfers.getTransactionStatus(transferId)).build();
    }
    @GET
    public Response getAllRequests(){
        return Response.ok().entity(this.fundTransfers.getAllTransferRequest()).build();
    }
}
