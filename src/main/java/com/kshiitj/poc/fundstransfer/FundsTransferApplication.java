package com.kshiitj.poc.fundstransfer;

import com.kshiitj.poc.fundstransfer.boundry.Accounts;
import com.kshiitj.poc.fundstransfer.boundry.FundTransfers;
import com.kshiitj.poc.fundstransfer.exceptionmappers.IllegalArgumentExceptionMapper;
import com.kshiitj.poc.fundstransfer.exceptionmappers.NoAccountAvailableExceptionMapper;
import com.kshiitj.poc.fundstransfer.resources.AccountResource;
import com.kshiitj.poc.fundstransfer.resources.FundsTransferResource;
import com.kshiitj.poc.fundstransfer.service.AccountService;
import com.kshiitj.poc.fundstransfer.store.InMemoryAccountStore;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class FundsTransferApplication extends Application<FundsTransferConfiguration>{
    public static void main(String[] args) throws Exception {
        new FundsTransferApplication().run(args);
    }

    @Override
    public String getName(){
        return "funds-transfer.yml";
    }
    public void run(FundsTransferConfiguration fundsTransferConfiguration, Environment environment) throws Exception {
        final AccountService accountService=new AccountService(new InMemoryAccountStore());
        final FundTransfers fundTransfers=new FundTransfers(accountService);
        final FundsTransferResource fundsTransfer=new FundsTransferResource(fundTransfers);
        final AccountResource accountResource=new AccountResource(new Accounts(accountService));

        environment.jersey().register(fundsTransfer);
        environment.jersey().register(accountResource);
        environment.jersey().register(IllegalArgumentExceptionMapper.class);
        environment.jersey().register(NoAccountAvailableExceptionMapper.class);
    }
}
