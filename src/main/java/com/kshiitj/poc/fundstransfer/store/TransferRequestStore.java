package com.kshiitj.poc.fundstransfer.store;

import com.kshiitj.poc.fundstransfer.domain.TransferRequest;
import com.kshiitj.poc.fundstransfer.exceptions.TransactionNotFoundException;

import java.util.List;
import java.util.UUID;

public interface TransferRequestStore {

    TransferRequest getRequest(UUID transferId) throws TransactionNotFoundException;
    void saveRequest(TransferRequest transferRequest);

    List<TransferRequest> getAllTransferRequests();
}
