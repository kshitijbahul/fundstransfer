package com.kshiitj.poc.fundstransfer;

import com.kshiitj.poc.fundstransfer.exceptionmappers.FundsTransferExceptionMapper;
import com.kshiitj.poc.fundstransfer.exceptionmappers.IllegalArgumentExceptionMapper;
import com.kshiitj.poc.fundstransfer.exceptionmappers.NoAccountAvailableExceptionMapper;
import com.kshiitj.poc.fundstransfer.guice.AccountModule;
import com.kshiitj.poc.fundstransfer.resources.AccountResource;
import com.kshiitj.poc.fundstransfer.resources.FundsTransferResource;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import ru.vyarus.dropwizard.guice.GuiceBundle;

public class FundsTransferApplication extends Application<FundsTransferConfiguration>{
    public static void main(String[] args) throws Exception {
        new FundsTransferApplication().run(args);
    }
    @Override
    public void initialize(Bootstrap<FundsTransferConfiguration> bootstrap){
        bootstrap.addBundle(GuiceBundle.builder()
                .printDiagnosticInfo()
                .enableAutoConfig("com.kshiitj.poc.fundstransfer")
                .modules(new AccountModule())
                .build()
        );
    }

    @Override
    public String getName(){
        return "funds-transfer.yml";
    }
    public void run(FundsTransferConfiguration fundsTransferConfiguration, Environment environment) throws Exception {
        //final AccountService accountService=new AccountService();
        //final FundTransfers fundTransfers=new FundTransfers(accountService);
        //final FundsTransferResource fundsTransfer=new FundsTransferResource();
        //final AccountResource accountResource=new AccountResource(new Accounts(accountService));

        /*
        environment.jersey().register(FundsTransferResource.class);
        environment.jersey().register(AccountResource.class);
        environment.jersey().register(IllegalArgumentExceptionMapper.class);
        environment.jersey().register(NoAccountAvailableExceptionMapper.class);
        environment.jersey().register(FundsTransferExceptionMapper.class);

         */
    }
}
