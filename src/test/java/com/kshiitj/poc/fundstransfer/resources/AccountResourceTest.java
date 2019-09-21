package com.kshiitj.poc.fundstransfer.resources;

import com.kshiitj.poc.fundstransfer.boundry.Accounts;
import com.kshiitj.poc.fundstransfer.domain.Account;
import com.kshiitj.poc.fundstransfer.domain.AccountCreationRequest;
import com.kshiitj.poc.fundstransfer.domain.AccountCreationResponse;
import com.kshiitj.poc.fundstransfer.exceptionmappers.IllegalArgumentExceptionMapper;
import com.kshiitj.poc.fundstransfer.exceptions.AccountNotFoundException;
import com.kshiitj.poc.fundstransfer.exceptions.NoAccountAvailableException;
import com.kshiitj.poc.fundstransfer.exceptions.NoTransactionsAvailableException;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;


import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;

public class AccountResourceTest {

    private static Accounts accounts=mock(Accounts.class);
    @After
    public void after(){
        reset(accounts);
    }

    @ClassRule
    public static final ResourceTestRule resource = ResourceTestRule.builder().addResource(AccountResource.class).addProvider(IllegalArgumentExceptionMapper.class).addProvider(NoAccountAvailableException.class).build();

    @Test
    public void test_resourceAvailable() throws AccountNotFoundException {
        given(accounts.createAccount(BigDecimal.valueOf(1))).willReturn(UUID.randomUUID());
        Response resp=resource.target("/account").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(new AccountCreationRequest(BigDecimal.valueOf(1))));
        assertThat(resp.getStatus(),equalTo(HttpStatus.CREATED_201));
        //assertThat(resp.readEntity(AccountCreationResponse.class),instanceOf(AccountCreationResponse.class));
        AccountCreationResponse creationResponse= resp.readEntity(AccountCreationResponse.class);
        assertThat(creationResponse.getAccountId(),instanceOf(UUID.class));
    }
    @Test
    public void test_getAccount() throws AccountNotFoundException {
        UUID accountId=UUID.randomUUID();
        given(accounts.getAccount(accountId)).willReturn(new Account());
        Response resp=resource.target(String.format("/account/%s",accountId)).request(MediaType.APPLICATION_JSON_TYPE).get();
        assertThat(resp.getStatus(),equalTo(HttpStatus.OK_200));
        //assertThat(resp.readEntity(AccountCreationResponse.class),instanceOf(AccountCreationResponse.class));
        Account account= resp.readEntity(Account.class);
        assertThat(account.getId(),instanceOf(UUID.class));
        assertThat(account.getBalance(),equalTo(BigDecimal.ZERO));
    }
    @Test
    public void test_accountCreation_invalidBalance() throws AccountNotFoundException {
        given(accounts.createAccount(BigDecimal.valueOf(-5))).willThrow(IllegalArgumentException.class);
        Response resp=resource.target("/account").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(new AccountCreationRequest(BigDecimal.valueOf(-5))));
        System.out.println(resp);
        assertThat(resp.getStatus(),equalTo(HttpStatus.BAD_REQUEST_400));
    }
    @Test
    public void test_accountCreation_novalue(){
        //given(accounts.createAccount(BigDecimal.valueOf(-5))).willThrow(IllegalArgumentException.class);
        String request= "\"initialBalance\":abc";
        Response resp=resource.target("/account").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(request));
        System.out.println(resp);
        assertThat(resp.getStatus(),equalTo(HttpStatus.BAD_REQUEST_400));
    }
    @Test
    public void test_getAllAccounts(){

        given(accounts.getAccounts()).willReturn(Arrays.asList(new Account(),new Account()));
        Response response=resource.target("/account").request().get();
        assertThat(response.getStatus(),equalTo(HttpStatus.OK_200));
        List<Account> accounts= response.readEntity(new GenericType<List<Account>>(){});
        //assertThat(accounts,isNotNull());
        assertThat(accounts,hasSize(2));
    }
    @Test
    public void test_getAllAccounts_expectException(){
        given(accounts.getAccounts()).willThrow(new NoAccountAvailableException());
        Response response=resource.target("/account").request().get();
        //List<Account> accounts= response.readEntity(new GenericType<List<Account>>(){});
        assertThat(response.getStatus(),equalTo(HttpStatus.INTERNAL_SERVER_ERROR_500));
    }
    @Test
    public void test_getAccounts_transactions(){
        UUID accountId=UUID.randomUUID();
        given(accounts.getAccountTransactions(accountId)).willThrow(new NoTransactionsAvailableException());
        Response response=resource.target(String.format("/account/%s/transactions",accountId)).request().get();
        //List<Account> accounts= response.readEntity(new GenericType<List<Account>>(){});
        assertThat(response.getStatus(),equalTo(HttpStatus.INTERNAL_SERVER_ERROR_500));
    }
}