package com.kshiitj.poc.fundstransfer.resources;


import com.kshiitj.poc.fundstransfer.boundry.FundTransfers;
import com.kshiitj.poc.fundstransfer.domain.Account;
import com.kshiitj.poc.fundstransfer.domain.FundsTransferResponse;
import com.kshiitj.poc.fundstransfer.domain.TransferRequest;
import com.kshiitj.poc.fundstransfer.domain.TransferRequestStatus;
import com.kshiitj.poc.fundstransfer.exceptions.FundsTransferException;
import com.kshiitj.poc.fundstransfer.exceptions.TransactionNotFoundException;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mock;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.net.URI;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;

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
        source=new Account().deposit(BigDecimal.valueOf(100));
    }
    @After
    public void cleanUpTest(){
        reset(fundTransfers);
    }
    @Test
    public void testFundsTransfer()  {

        TransferRequest req=new TransferRequest(UUID.randomUUID(),UUID.randomUUID(),BigDecimal.valueOf(25.5));
        given(fundTransfers.transfer(req)).willReturn(new FundsTransferResponse(UUID.randomUUID(), TransferRequestStatus.SUCCESS));
        Response response=resource.target("/funds/transfer").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(req));
        assertThat(response.getStatus(), equalTo(HttpStatus.OK_200));
        System.out.println(response.getEntity().toString());
        FundsTransferResponse resp= response.readEntity(FundsTransferResponse.class);
        assertThat(resp.getTransferId(),instanceOf(UUID.class));
        assertThat(resp.getStatus(),equalTo(TransferRequestStatus.SUCCESS));
        //assertThat(account.getBalance(),equalTo(source.getBalance().subtract(BigDecimal.valueOf(25.5))));
    }
    @Test
    public void test_fundsTransferFailedResponse(){
        TransferRequest req=new TransferRequest(UUID.randomUUID(),UUID.randomUUID(),BigDecimal.valueOf(25.5));
        given(fundTransfers.transfer(req)).willThrow(new FundsTransferException(TransferRequestStatus.DEBIT_FAILED,"Error from Mock"));
        Response resp=resource.target("/funds/transfer").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(req));
        assertThat(resp.getStatus(),equalTo(HttpStatus.INTERNAL_SERVER_ERROR_500));
    }
    @Test
    public void test_getTransactionStatus_TransactionNotFound() throws TransactionNotFoundException{
        TransferRequest req=new TransferRequest(UUID.randomUUID(),UUID.randomUUID(),BigDecimal.valueOf(25.5));
        given(fundTransfers.getTransactionStatus(req.getRequestId())).willThrow(new TransactionNotFoundException("Error from Mock"));
        Response resp=resource.target(String.format("/funds/transfer/%s",req.getRequestId())).request(MediaType.APPLICATION_JSON_TYPE).get();
        assertThat(resp.getStatus(),equalTo(HttpStatus.INTERNAL_SERVER_ERROR_500));
    }
    @Test
    public void test_getTransactionStatus() throws TransactionNotFoundException{
        TransferRequest req=new TransferRequest(UUID.randomUUID(),UUID.randomUUID(),BigDecimal.valueOf(25.5));
        given(fundTransfers.getTransactionStatus(req.getRequestId())).willReturn(new FundsTransferResponse(req.getRequestId(),TransferRequestStatus.SUCCESS));
        Response resp=resource.target(String.format("/funds/transfer/%s",req.getRequestId())).request(MediaType.APPLICATION_JSON_TYPE).get();
        assertThat(resp.getStatus(),equalTo(HttpStatus.OK_200));
        assertThat(resp.readEntity(FundsTransferResponse.class).getStatus(),equalTo(TransferRequestStatus.SUCCESS));
    }

}