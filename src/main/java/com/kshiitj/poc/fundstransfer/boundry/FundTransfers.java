package com.kshiitj.poc.fundstransfer.boundry;

import com.kshiitj.poc.fundstransfer.domain.FundsTransferResponse;
import com.kshiitj.poc.fundstransfer.domain.TransferRequest;
import com.kshiitj.poc.fundstransfer.exceptions.AccountNotFoundException;
import com.kshiitj.poc.fundstransfer.exceptions.FundsTransferException;
import com.kshiitj.poc.fundstransfer.exceptions.InsufficientBalanceException;
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
                return new FundsTransferResponse(transferRequest.getRequestId(), FundsTransferResponse.Status.SUCCESS);
            }catch (IllegalStateException | AccountNotFoundException exc){
                try{
                    accountService.deposit(transferRequest.getFromAccountId(),transferRequest.getAmount());
                    throw new FundsTransferException(FundsTransferResponse.Status.CREDIT_FAILED,exc.getMessage());
                    //return new FundsTransferResponse(transferRequest.getRequestId(),FundsTransferResponse.Status.CREDIT_FAILED,exc.getMessage());
                }catch (IllegalStateException |AccountNotFoundException e){
                    throw new FundsTransferException(FundsTransferResponse.Status.REVERSAL_FAILED,e.getMessage());
                    //return new FundsTransferResponse(transferRequest.getRequestId(),FundsTransferResponse.Status.REVERSAL_FAILED,e.getMessage());
                }
            }
        }catch (InsufficientBalanceException | AccountNotFoundException excp){
            //nothing to do
            throw new FundsTransferException(FundsTransferResponse.Status.DEBIT_FAILED,excp.getMessage());
            //return new FundsTransferResponse(transferRequest.getRequestId(),FundsTransferResponse.Status.DEBIT_FAILED,excp.getMessage());
        }
    }
}
