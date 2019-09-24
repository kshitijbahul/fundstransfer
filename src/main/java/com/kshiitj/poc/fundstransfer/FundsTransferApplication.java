package com.kshiitj.poc.fundstransfer;

import com.kshiitj.poc.fundstransfer.guice.AccountModule;
import com.kshiitj.poc.fundstransfer.guice.TransactionModule;
import com.kshiitj.poc.fundstransfer.guice.TransferRequestModule;
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
                .modules(new AccountModule(),new TransactionModule(),new TransferRequestModule())
                .build()
        );
    }

    @Override
    public String getName(){
        return "funds-transfer.yml";
    }
    public void run(FundsTransferConfiguration fundsTransferConfiguration, Environment environment) throws Exception {

    }
}
