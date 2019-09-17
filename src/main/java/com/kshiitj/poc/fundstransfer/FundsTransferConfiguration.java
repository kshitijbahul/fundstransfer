package com.kshiitj.poc.fundstransfer;

import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

public class FundsTransferConfiguration extends Configuration {

    @NotEmpty
    private Integer portNumber;

}
