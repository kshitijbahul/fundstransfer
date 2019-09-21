package com.kshiitj.poc.fundstransfer.resources;


import com.kshiitj.poc.fundstransfer.boundry.FundTransfers;
import com.kshiitj.poc.fundstransfer.domain.Account;
import com.kshiitj.poc.fundstransfer.domain.FundsTransferResponse;
import com.kshiitj.poc.fundstransfer.domain.TransferRequest;
import com.kshiitj.poc.fundstransfer.exceptions.AccountNotFoundException;
import com.kshiitj.poc.fundstransfer.exceptions.FundsTransferException;
import com.kshiitj.poc.fundstransfer.exceptions.InsufficientBalanceException;
import com.kshiitj.poc.fundstransfer.service.AccountService;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Server;
import org.joda.time.DateTime;
import org.junit.*;
import org.mockito.Mock;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class FundsTransferResourceTest {
    private static Server server;
    private static URI serverUri;

    @Mock
    public static FundTransfers fundTransfers=mock(FundTransfers.class);

    Account source;

    @ClassRule
    public static final ResourceTestRule resource = ResourceTestRule.builder().addResource(new FundsTransferResource(fundTransfers)).build();

    @Before
    public void setUpTest(){
        //accountService=new AccountService();
        source=new Account(BigDecimal.valueOf(100));
    }
    @After
    public void cleanUpTest(){
        reset(fundTransfers);
    }
    @Test
    public void testFundsTransfer() throws AccountNotFoundException, InsufficientBalanceException {

        TransferRequest req=new TransferRequest(UUID.randomUUID(),UUID.randomUUID(),BigDecimal.valueOf(25.5));
        given(fundTransfers.transfer(req)).willReturn(new FundsTransferResponse(UUID.randomUUID(),FundsTransferResponse.Status.SUCCESS));
        Response response=resource.target("/funds/transfer").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(req));
        assertThat(response.getStatus(), equalTo(HttpStatus.OK_200));
        System.out.println(response.getEntity().toString());
        FundsTransferResponse resp= response.readEntity(FundsTransferResponse.class);
        assertThat(resp.getTransferId(),instanceOf(UUID.class));
        assertThat(resp.getStatus(),equalTo(FundsTransferResponse.Status.SUCCESS));
        //assertThat(account.getBalance(),equalTo(source.getBalance().subtract(BigDecimal.valueOf(25.5))));
    }
    @Test
    public void test_fundsTransferFailedResponse(){
        TransferRequest req=new TransferRequest(UUID.randomUUID(),UUID.randomUUID(),BigDecimal.valueOf(25.5));
        given(fundTransfers.transfer(req)).willThrow(new FundsTransferException(FundsTransferResponse.Status.DEBIT_FAILED,"Error from Mock"));
        Response resp=resource.target("/funds/transfer").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(req));
        assertThat(resp.getStatus(),equalTo(HttpStatus.INTERNAL_SERVER_ERROR_500));
    }
    /*
    BigDecimal money=BigDecimal.valueOf(25.5);
        given(accountService.getAccount(source.getId())).willReturn(source);
        given(accountService.createAccount(money)).willReturn(new Account(money));
        given(accountService.deposit(source.getId(),money)).willReturn(source);
        Account dest=accountService.createAccount(money);
        given(accountService.getAccount(source.getId())).willReturn(source);
        given(accountService.getAccount(dest.getId())).willReturn(source);
        dest.setBalance(dest.getBalance().subtract(money));
        given(accountService.deposit(dest.getId(),money)).willReturn(dest);
        source.setBalance(source.getBalance().add(money));
        given(accountService.withdraw(source.getId(),money)).willReturn(source);
        System.out.println(source);
     */

}