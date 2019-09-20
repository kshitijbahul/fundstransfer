package com.kshiitj.poc.fundstransfer.boundry;

import com.kshiitj.poc.fundstransfer.domain.FundsTransferResponse;
import com.kshiitj.poc.fundstransfer.domain.TransferRequest;
import com.kshiitj.poc.fundstransfer.service.AccountService;

public class FundTransfers {

    private AccountService accountService;

    public FundTransfers(AccountService accountService){
        this.accountService=accountService;
    }
    public FundsTransferResponse transfer(TransferRequest transferRequest){
        transferRequest.verify();
        try{
            accountService.withdraw(transferRequest.getFromAccountId(),transferRequest.getAmount());
            try{
                accountService.deposit(transferRequest.getToAccountId(),transferRequest.getAmount());
                return new FundsTransferResponse(transferRequest.getRequestId(), FundsTransferResponse.Status.SUCCESS,null);
            }catch (RuntimeException exc){
                try{
                    accountService.deposit(transferRequest.getFromAccountId(),transferRequest.getAmount());
                    return new FundsTransferResponse(transferRequest.getRequestId(),FundsTransferResponse.Status.CREDIT_FAILED,exc.getMessage());
                }catch (RuntimeException e){
                    return new FundsTransferResponse(transferRequest.getRequestId(),FundsTransferResponse.Status.REVERSAL_FAILED,e.getMessage());
                }

            }
        }catch (RuntimeException excp){
            //nothing to do
            return new FundsTransferResponse(transferRequest.getRequestId(),FundsTransferResponse.Status.DEBIT_FAILED,excp.getMessage());
        }
        //log.debug(String.format("Got the debit account as %d",debitAccount ));

        //log.debug(String.format("Got the credit account  as %d",creditAccount ));
    }

}
