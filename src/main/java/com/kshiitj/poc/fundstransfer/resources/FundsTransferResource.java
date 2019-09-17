package com.kshiitj.poc.fundstransfer.resources;

import com.kshiitj.poc.fundstransfer.domain.Account;
import com.kshiitj.poc.fundstransfer.domain.TransferRequest;
import com.kshiitj.poc.fundstransfer.service.AccountService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/funds/transfer")
@Produces(MediaType.APPLICATION_JSON)
public class FundsTransferResource {

    private AccountService accountService;

    public FundsTransferResource(AccountService accountService){
        this.accountService=accountService;
    }

    @POST
    public Account transferMoney(TransferRequest request){
        //3 steps
        // Debit
        // Create transaction
        // Credit
        //return Debit Account
        return accountService.getAccount(request.getFromAccountId());

    }

}
