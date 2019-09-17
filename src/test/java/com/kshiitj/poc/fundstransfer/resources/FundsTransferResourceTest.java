package com.kshiitj.poc.fundstransfer.resources;


import com.kshiitj.poc.fundstransfer.domain.Account;
import com.kshiitj.poc.fundstransfer.domain.TransferRequest;
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
    public static AccountService accountService=mock(AccountService.class);

    Account source;

    @ClassRule
    public static final ResourceTestRule resource = ResourceTestRule.builder().addResource(new FundsTransferResource(accountService)).build();

    @Before
    public void setUpTest(){
        //accountService=new AccountService();
        source=new Account(UUID.randomUUID(), BigDecimal.valueOf(100));
    }
    @After
    public void cleanUpTest(){
        reset(accountService);
    }
    @Test
    public void testFundsTransfer() throws IOException {
        given(accountService.getAccount(source.getId())).willReturn(source);
        Account dest=new Account(UUID.randomUUID());
        System.out.println(source);
        TransferRequest req=new TransferRequest(dest.getId(),source.getId(),BigDecimal.valueOf(25.5),DateTime.now(),"SomeReference");
        Response response=resource.target("/funds/transfer").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(req));
        assertThat(response.getStatus(), equalTo(HttpStatus.OK_200));
        System.out.println(response.getEntity().toString());
        Account account= response.readEntity(Account.class);
        assertThat(account.getId(),equalTo(source.getId()));
        assertThat(account.getBalance(),notNullValue());
    }
}