package com.kshiitj.poc.fundstransfer;

import com.kshiitj.poc.fundstransfer.guice.AccountModule;
import com.kshiitj.poc.fundstransfer.guice.TransactionModule;
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
                .enableAutoConfig(getClass().getPackage().getName())
                .modules(new AccountModule(),new TransactionModule())
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
