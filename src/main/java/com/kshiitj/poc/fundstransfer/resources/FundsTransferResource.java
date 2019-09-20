package com.kshiitj.poc.fundstransfer.resources;

import com.kshiitj.poc.fundstransfer.boundry.FundTransfers;
import com.kshiitj.poc.fundstransfer.domain.FundsTransferResponse;
import com.kshiitj.poc.fundstransfer.domain.TransferRequest;
import com.kshiitj.poc.fundstransfer.exceptions.AccountNotFoundException;
import com.kshiitj.poc.fundstransfer.exceptions.InsufficientBalanceException;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/funds/transfer")
@Produces(MediaType.APPLICATION_JSON)
//@Log4j2
public class FundsTransferResource {

    private FundTransfers fundTransfers;
    @Inject
    public FundsTransferResource(FundTransfers fundTransfers){
        this.fundTransfers=fundTransfers;
    }

    @POST
    public FundsTransferResponse transferMoney(TransferRequest request) throws AccountNotFoundException, InsufficientBalanceException {
        return fundTransfers.transfer(request);
    }
}