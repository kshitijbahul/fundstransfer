package com.kshiitj.poc.fundstransfer.resources;

import com.kshiitj.poc.fundstransfer.boundry.Accounts;
import com.kshiitj.poc.fundstransfer.domain.AccountCreationRequest;
import com.kshiitj.poc.fundstransfer.domain.AccountCreationResponse;
import com.kshiitj.poc.fundstransfer.exceptions.AccountNotFoundException;
import lombok.extern.slf4j.Slf4j;


import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.IntStream;

@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class AccountResource {
    private final Accounts accounts;
    @Inject
    private AccountResource(Accounts accounts){
        this.accounts=accounts;
    }
    @POST
    public Response createAccount(AccountCreationRequest accountCreationRequest) throws AccountNotFoundException{
        accountCreationRequest.verify();
        return Response.status(Response.Status.CREATED).entity(new AccountCreationResponse(this.accounts.createAccount(accountCreationRequest.getInitialBalance()))).build();
    }
    @GET
    public Response getAllAccounts(){
        return Response.status(Response.Status.OK).entity(this.accounts.getAccounts()).build();
    }
    @GET
    @Path("/{accountId}")
    public Response getAccount(@PathParam("accountId") UUID accountId) throws AccountNotFoundException {
        return Response.status(Response.Status.OK).entity(this.accounts.getAccount(accountId)).build();
    }
    @GET
    @Path("/{accountId}/transactions")
    public Response getAccountTransactions(@PathParam("accountId") UUID accountId){
        return Response.status(Response.Status.OK).entity(this.accounts.getAccountTransactions(accountId)).build();
    }
    @POST
    @Path("/quickSetup/{noOfAccounts}")
    public Response quickSetupAccounts(@PathParam("noOfAccounts") Integer noOfAccounts){
        IntStream.range(0,noOfAccounts).forEach((index)->{
            try {
                this.accounts.createAccount(BigDecimal.valueOf(100));
            } catch (AccountNotFoundException e) {
                e.printStackTrace();
            }
        });
        return Response.status(Response.Status.OK).entity(this.accounts.getAccounts()).build();
    }
}
