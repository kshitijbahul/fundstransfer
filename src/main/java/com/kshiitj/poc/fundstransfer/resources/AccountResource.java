package com.kshiitj.poc.fundstransfer.resources;

import com.kshiitj.poc.fundstransfer.boundry.Accounts;
import com.kshiitj.poc.fundstransfer.domain.Account;
import com.kshiitj.poc.fundstransfer.domain.AccountCreationRequest;
import com.kshiitj.poc.fundstransfer.domain.AccountCreationResponse;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
    public Response getAllAccounts(){
        return Response.status(Response.Status.OK).entity(this.accounts.getAccounts()).build();
    }

}
