package com.example.cash.client;

import com.example.cash.dto.AccountDto;
import com.example.cash.dto.AccountOperationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

@Slf4j
@Component
public class AccountsClient {
    private final RestClient restClient;

    @Value("${services.accounts.url}")
    private String accountsUrl;


    public AccountsClient(@Qualifier("customRestClient") RestClient restClient) {
        this.restClient = restClient;
        log.info("BankApiClient initialized with RestClient: {}", restClient.getClass());
    }

    public AccountDto deposit(AccountOperationRequest request) {
        log.info("call deposit ");
        return restClient.post()
                .uri(accountsUrl + "/accounts/deposit")  // baseUrl уже установлен в бине
                .body(request)
                .retrieve()
                .body(AccountDto.class);
    }

    public AccountDto withdraw(AccountOperationRequest request) {
        log.info("call withdraw ");
        return restClient.post()
                .uri(accountsUrl + "/accounts/withdraw")  // baseUrl уже установлен в бине
                .body(request)
                .retrieve()
                .body(AccountDto.class);
    }
}
