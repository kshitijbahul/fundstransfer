package com.kshiitj.poc.fundstransfer.boundry;

import com.google.inject.Guice;
import com.kshiitj.poc.fundstransfer.FundsTransferApplication;
import com.kshiitj.poc.fundstransfer.FundsTransferConfiguration;
import com.kshiitj.poc.fundstransfer.domain.Account;
import com.kshiitj.poc.fundstransfer.domain.FundsTransferResponse;
import com.kshiitj.poc.fundstransfer.domain.TransferRequest;
import com.kshiitj.poc.fundstransfer.exceptions.AccountNotFoundException;
import com.kshiitj.poc.fundstransfer.exceptions.FundsTransferException;
import com.kshiitj.poc.fundstransfer.guice.AccountModule;
import com.kshiitj.poc.fundstransfer.service.AccountService;
import com.kshiitj.poc.fundstransfer.store.AccountStore;
import com.kshiitj.poc.fundstransfer.store.InMemoryAccountStore;
import com.kshiitj.poc.fundstransfer.store.InMemoryTransactionsStore;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import ru.vyarus.dropwizard.guice.test.GuiceyAppRule;
import ru.vyarus.dropwizard.guice.test.spock.UseGuiceyApp;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
@UseGuiceyApp(FundsTransferApplication.class)
public class FundTransfersTest {
    @ClassRule
    public static GuiceyAppRule<FundsTransferConfiguration> RULE = new GuiceyAppRule<>(FundsTransferApplication.class, "funds-transfer.yml");
    @Inject
    private static FundTransfers fundTransfers;
    @Inject
    private static AccountService accountService;
    private static final Account a1=new Account();
    private static final Account a2=new Account().deposit(BigDecimal.TEN);
    private static final Account a3=new Account().deposit(BigDecimal.TEN);
    private static final Account a4=new Account();
    @BeforeClass
    public static void setTestBed(){
        /*
        GuiceyAppRule<MyConfiguration> RULE = new GuiceyAppRule<>(MyApplication.class, "path/to/configuration.yaml");
        AccountStore accountStore=new InMemoryAccountStore();
        accountStore.saveAccount(a1);
        accountStore.saveAccount(a2);
        accountStore.saveAccount(a3);
        accountStore.saveAccount(a4);
        accountService=new AccountService(accountStore,new InMemoryTransactionsStore());
        fundTransfers=new FundTransfers(accountService);
         */

        //Guice.createInjector().injectMembers(this);
        accountService=RULE.getBean(AccountService.class);
        accountService.createAccount(BigDecimal.ZERO);
        accountService.createAccount(BigDecimal.TEN);
    }
    @Test(expected = IllegalArgumentException.class)
    public void test_Fundstransfer_invalidAmount(){
        TransferRequest transferRequest=new TransferRequest(a1.getId(),a2.getId(),BigDecimal.ZERO);
        fundTransfers.transfer(transferRequest);
    }
    @Test(expected = IllegalArgumentException.class)
    public void test_FundsTransfer_invalidFromAccount(){
        TransferRequest transferRequest=new TransferRequest(a1.getId(),UUID.randomUUID(),BigDecimal.ZERO);
        fundTransfers.transfer(transferRequest);
    }
    @Test(expected = IllegalArgumentException.class)
    public void test_FundsTransfer_invalidToAccount() throws AccountNotFoundException {
        BigDecimal firstBalance=a1.getBalance();
        BigDecimal secondBalance=a2.getBalance();
        TransferRequest transferRequest=new TransferRequest(UUID.randomUUID(),a2.getId(),BigDecimal.ZERO);
        fundTransfers.transfer(transferRequest);
        assertThat(accountService.getAccount(a1.getId()).getBalance(),equalTo(firstBalance.subtract(BigDecimal.valueOf(5.0))));
        assertThat(accountService.getAccount(a2.getId()).getBalance(),equalTo(secondBalance.add(BigDecimal.valueOf(5.0))));
    }
    @Test
    public void test_FundsTransfer_success() throws AccountNotFoundException {
        BigDecimal toBalance=a1.getBalance();
        BigDecimal fromBalance=a2.getBalance();
        TransferRequest transferRequest=new TransferRequest(a2.getId(),a1.getId(),BigDecimal.valueOf(5.0));
        FundsTransferResponse resp=fundTransfers.transfer(transferRequest);
        assertThat(resp.getTransferId(),notNullValue());
        assertThat(resp.getStatus(),equalTo(FundsTransferResponse.Status.SUCCESS));
        assertThat(accountService.getAccount(a2.getId()).getBalance(),equalTo(fromBalance.subtract(BigDecimal.valueOf(5.0))));
        assertThat(accountService.getAccount(a1.getId()).getBalance(),equalTo(toBalance.add(BigDecimal.valueOf(5.0))));
    }
    @Test(expected = FundsTransferException.class)
    public void test_FundsTransfer_failedDebit() throws AccountNotFoundException {
        BigDecimal toBalance=a2.getBalance();
        BigDecimal fromBalance=a1.getBalance();
        TransferRequest transferRequest=new TransferRequest(a1.getId(),a2.getId(),BigDecimal.valueOf(20.0));
        FundsTransferResponse resp=fundTransfers.transfer(transferRequest);
        //assertThat(resp.getTransferId(),notNullValue());
        assertThat(resp,equalTo(FundsTransferResponse.Status.DEBIT_FAILED));
        assertThat(accountService.getAccount(a1.getId()).getBalance(),equalTo(fromBalance));
        assertThat(accountService.getAccount(a2.getId()).getBalance(),equalTo(toBalance));
    }
    @Test
    public void test_FundsTransfer_drainAccount() throws AccountNotFoundException {
        BigDecimal toBalance=a1.getBalance();
        BigDecimal fromBalance=a2.getBalance();
        TransferRequest transferRequest=new TransferRequest(a2.getId(),a1.getId(),fromBalance);
        FundsTransferResponse resp=fundTransfers.transfer(transferRequest);
        assertThat(resp.getTransferId(),notNullValue());
        assertThat(resp.getStatus(),equalTo(FundsTransferResponse.Status.SUCCESS));
        assertThat(accountService.getAccount(a2.getId()).getBalance(),equalTo(BigDecimal.valueOf(0.0)));
        assertThat(accountService.getAccount(a1.getId()).getBalance(),equalTo(toBalance.add(fromBalance)));
    }
    @Test
    public void test_FundsTransfer_failedCredit(){
        BigDecimal toBalance=a1.getBalance();
        BigDecimal fromBalance=a2.getBalance();
        TransferRequest transferRequest1=new TransferRequest(a2.getId(),a1.getId(),BigDecimal.valueOf(10.0));
        TransferRequest transferRequest2=new TransferRequest(a2.getId(),a1.getId(),BigDecimal.valueOf(10.0));
        //FundsTransferResponse resp=fundTransfers.transfer(transferRequest);
        ExecutorService executorService= Executors.newFixedThreadPool(2);
        TransferRequest t1=new TransferRequest(a2.getId(),a1.getId(),BigDecimal.valueOf(.5));
        TransferRequest t2=new TransferRequest(a1.getId(),a2.getId(),BigDecimal.valueOf(.5));
        //TransferRequest t3=new TransferRequest(a3.getId(),a4.getId(),BigDecimal.valueOf(10));
        //TransferRequest t4=new TransferRequest(a2.getId(),a1.getId(),BigDecimal.valueOf(10));
        List<Callable<FundsTransferResponse>> tasks= Arrays.asList(
                ()->{return fundTransfers.transfer(t1);},
                ()->{return fundTransfers.transfer(t2);}
                //()->{return fundTransfers.transfer(t3);},
                //()->{return fundTransfers.transfer(t4);}
        );
        try {
            List<Future<FundsTransferResponse>> futures=executorService.invokeAll(tasks);
            executorService.shutdown();

            while(!executorService.isShutdown()){
                Thread.sleep(1000);
            }
            if(executorService.isShutdown()){
                FundsTransferResponse resp1=futures.get(0).get();
                assertThat(resp1.getTransferId(),notNullValue());
                assertThat(resp1.getStatus(),equalTo(FundsTransferResponse.Status.SUCCESS));
                FundsTransferResponse resp2=futures.get(1).get();
                assertThat(resp2.getStatus(),equalTo(FundsTransferResponse.Status.SUCCESS));
                //assertThat(accountService.getAccount(a1.getId()).getBalance(),equalTo(toBalance.add(BigDecimal.valueOf(0.5))));
                //assertThat(accountService.getAccount(a2.getId()).getBalance(),equalTo(fromBalance.subtract(BigDecimal.valueOf(0.5))));
                System.out.println(resp2);
                assertThat(resp2.getTransferId(),notNullValue());
                assertThat(resp2.getStatus(),equalTo(FundsTransferResponse.Status.SUCCESS));
                //assertThat(resp2.getStatus(),equalTo(FundsTransferResponse.Status.DEBIT_FAILED));
                assertThat(accountService.getAccount(a1.getId()).getBalance(),equalTo(toBalance));
                assertThat(accountService.getAccount(a2.getId()).getBalance(),equalTo(fromBalance));
                //FundsTransferResponse resp3=futures.get(2).get();
                //assertThat(resp3.getTransferId(),notNullValue());
                //FundsTransferResponse resp4=futures.get(3).get();
                //assertThat(resp4.getTransferId(),notNullValue());
            }
        } catch (InterruptedException | ExecutionException | AccountNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void test_creditFailed_Transaction_reversal(){
        BigDecimal fromBalance=a2.getBalance();
        TransferRequest transferRequest=new TransferRequest(a2.getId(),UUID.randomUUID(),fromBalance);
        try{
            FundsTransferResponse resp=fundTransfers.transfer(transferRequest);
        }catch (FundsTransferException e){
            try {
                assertThat(accountService.getAccount(a2.getId()).getBalance(),equalTo(fromBalance));
            } catch (AccountNotFoundException ex) {
                e.printStackTrace();
            }
        }
    }
}
