package com.kshiitj.poc.fundstransfer.store;

import com.kshiitj.poc.fundstransfer.domain.TransferRequest;
import com.kshiitj.poc.fundstransfer.exceptions.TransactionNotFoundException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
public class InMemoryTransferRequestStore implements TransferRequestStore {
    private final Map<UUID,TransferRequest> transferRequests;

    @Inject
    InMemoryTransferRequestStore(@Named("transferRequestRepository") Map<UUID,TransferRequest> transferRequests){
        this.transferRequests=transferRequests;
    }
    @Override
    public TransferRequest getRequest(UUID transferId) throws TransactionNotFoundException {
        if (this.transferRequests.containsKey(transferId)){
            return this.transferRequests.get(transferId);
        }else{
            throw new TransactionNotFoundException(String.format("Transfer Request %s does not exist",transferId));
        }
    }

    @Override
    public void saveRequest(TransferRequest transferRequest) {
        this.transferRequests.put(transferRequest.getRequestId(),transferRequest);
    }

    @Override
    public List<TransferRequest> getAllTransferRequests() {
        return this.transferRequests.values().parallelStream().collect(Collectors.toList());
    }
}
