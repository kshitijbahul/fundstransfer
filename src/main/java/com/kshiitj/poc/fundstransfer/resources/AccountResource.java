package com.kshiitj.poc.fundstransfer.resources;

import com.kshiitj.poc.fundstransfer.boundry.Accounts;
import com.kshiitj.poc.fundstransfer.domain.Account;
import com.kshiitj.poc.fundstransfer.domain.AccountCreationRequest;
import com.kshiitj.poc.fundstransfer.domain.AccountCreationResponse;
import com.kshiitj.poc.fundstransfer.exceptions.AccountNotFoundException;
import lombok.Getter;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource {
    private Accounts accounts;

    public AccountResource(Accounts accounts){
        this.accounts=accounts;
    }
    @POST
    public Response createAccount(AccountCreationRequest accountCreationRequest){
            return Response.status(Response.Status.CREATED).entity(new AccountCreationResponse(accounts.createAccount(accountCreationRequest.getInitialBalance()))).build();
    }
    @GET
    @Path("/all")
    public Response getAllAccounts(){
        return Response.status(Response.Status.OK).entity(this.accounts.getAccounts()).build();
    }
    @GET
    @Path("/{accountId}")
    public Response getAccount(@PathParam("accountId") UUID accountId) throws AccountNotFoundException {
        return Response.status(Response.Status.OK).entity(this.accounts.getAccount(accountId)).build();
    }
}
