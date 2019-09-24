package com.kshiitj.poc.fundstransfer.guice;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.kshiitj.poc.fundstransfer.domain.TransferRequest;
import com.kshiitj.poc.fundstransfer.store.InMemoryTransferRequestStore;
import com.kshiitj.poc.fundstransfer.store.TransferRequestStore;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TransferRequestModule extends AbstractModule {
    @Override
    protected void configure() {
        Map<UUID, TransferRequest> transferRequests = new ConcurrentHashMap<>();
        bind(new TypeLiteral<Map<UUID, TransferRequest> >(){})
                .annotatedWith(Names.named("transferRequestRepository"))
                .toInstance(transferRequests);
        bind(TransferRequestStore.class).to(InMemoryTransferRequestStore.class);
    }
}
