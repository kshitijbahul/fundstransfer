package com.kshiitj.poc.fundstransfer;

import com.kshiitj.poc.fundstransfer.resources.FundsTransferResource;
import com.kshiitj.poc.fundstransfer.service.AccountService;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class FundsTransferApplication extends Application<FundsTransferConfiguration>{
    public static void main(String[] args) throws Exception {
        new FundsTransferApplication().run(args);
    }

    @Override
    public String getName(){
        return "funds-transfer";
    }
    public void run(FundsTransferConfiguration fundsTransferConfiguration, Environment environment) throws Exception {
        final AccountService accountService=new AccountService();
        final FundsTransferResource fundsTransfer=new FundsTransferResource(accountService);
        environment.jersey().register(fundsTransfer);
    }
}
