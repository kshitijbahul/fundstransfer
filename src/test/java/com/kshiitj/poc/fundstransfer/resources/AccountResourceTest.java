package com.kshiitj.poc.fundstransfer.resources;

import com.kshiitj.poc.fundstransfer.boundry.Accounts;
import com.kshiitj.poc.fundstransfer.domain.Account;
import com.kshiitj.poc.fundstransfer.domain.AccountCreationRequest;
import com.kshiitj.poc.fundstransfer.domain.AccountCreationResponse;
import com.kshiitj.poc.fundstransfer.exceptionmappers.IllegalArgumentExceptionMapper;
import com.kshiitj.poc.fundstransfer.exceptions.NoAccountAvailableException;
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
    public static final ResourceTestRule resource = ResourceTestRule.builder().addResource(new AccountResource(accounts)).addProvider(IllegalArgumentExceptionMapper.class).addProvider(NoAccountAvailableException.class).build();

    @Test
    public void test_resourceAvailable(){
        given(accounts.createAccount(BigDecimal.valueOf(1))).willReturn(UUID.randomUUID());
        Response resp=resource.target("/account").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(new AccountCreationRequest(BigDecimal.valueOf(1))));
        assertThat(resp.getStatus(),equalTo(HttpStatus.CREATED_201));
        //assertThat(resp.readEntity(AccountCreationResponse.class),instanceOf(AccountCreationResponse.class));
        AccountCreationResponse creationResponse= resp.readEntity(AccountCreationResponse.class);
        assertThat(creationResponse.getAccountId(),instanceOf(UUID.class));
    }
    @Test
    public void test_accountCreation_invalidBalance(){
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

        given(accounts.getAccounts()).willReturn(Arrays.asList(new Account(BigDecimal.ZERO),new Account(BigDecimal.ZERO)));
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
}