package com.kshiitj.poc.fundstransfer.boundry;

import com.kshiitj.poc.fundstransfer.domain.FundsTransferResponse;
import com.kshiitj.poc.fundstransfer.domain.TransferRequest;
import com.kshiitj.poc.fundstransfer.domain.TransferRequestStatus;
import com.kshiitj.poc.fundstransfer.exceptions.AccountNotFoundException;
import com.kshiitj.poc.fundstransfer.exceptions.FundsTransferException;
import com.kshiitj.poc.fundstransfer.exceptions.InsufficientBalanceException;
import com.kshiitj.poc.fundstransfer.exceptions.TransactionNotFoundException;
import com.kshiitj.poc.fundstransfer.service.AccountService;
import com.kshiitj.poc.fundstransfer.store.TransferRequestStore;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.UUID;
/*
The Funds Transfer Boundary deals with all Funds transfer operations.
Transfer Funds between 2 accounts
Status of a transaction

It is available as a Singleton Scoped Bean through the application
 */

@Singleton
public class FundTransfers {

    private final AccountService accountService;
    private final TransferRequestStore transferRequestStore;
    @Inject
    FundTransfers(AccountService accountService,TransferRequestStore transferRequestStore){
        this.accountService=accountService;
        this.transferRequestStore=transferRequestStore;
    }
    public FundsTransferResponse transfer(TransferRequest transferRequest){
        transferRequest.verify();
        this.transferRequestStore.saveRequest(transferRequest);
        try{
            this.accountService.withdraw(transferRequest.getFromAccountId(),transferRequest.getAmount());
            try{
                this.accountService.deposit(transferRequest.getToAccountId(),transferRequest.getAmount());
                transferRequest.setTransferRequestStatus(TransferRequestStatus.SUCCESS);
                return new FundsTransferResponse(transferRequest.getRequestId(), TransferRequestStatus.SUCCESS);
            }catch (IllegalStateException | AccountNotFoundException exc){
                try{
                    this.accountService.deposit(transferRequest.getFromAccountId(),transferRequest.getAmount());
                    transferRequest.setTransferRequestStatus(TransferRequestStatus.CREDIT_FAILED);
                    throw new FundsTransferException(TransferRequestStatus.CREDIT_FAILED,exc.getMessage());
                }catch (IllegalStateException |AccountNotFoundException e){
                    transferRequest.setTransferRequestStatus(TransferRequestStatus.REVERSAL_FAILED);
                    throw new FundsTransferException(TransferRequestStatus.REVERSAL_FAILED,e.getMessage());
                }
            }
        }catch (InsufficientBalanceException | AccountNotFoundException excp){
            //nothing to do
            transferRequest.setTransferRequestStatus(TransferRequestStatus.DEBIT_FAILED);
            throw new FundsTransferException(TransferRequestStatus.DEBIT_FAILED,excp.getMessage());
        }finally {
            this.transferRequestStore.saveRequest(transferRequest);
        }
    }
    public FundsTransferResponse getTransactionStatus(UUID transferRequestId) throws TransactionNotFoundException {
        TransferRequest request= this.transferRequestStore.getRequest(transferRequestId);
        return new FundsTransferResponse(request.getRequestId(),request.getTransferRequestStatus());
    }
    public List<TransferRequest> getAllTransferRequest(){
        return this.transferRequestStore.getAllTransferRequests();
    }
}
