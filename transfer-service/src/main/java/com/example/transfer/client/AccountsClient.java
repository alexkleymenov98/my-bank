package com.example.transfer.client;

import com.example.transfer.dto.AccountDto;
import com.example.transfer.dto.TransferRequestAccounts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


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

    public AccountDto transfer(TransferRequestAccounts request) {
        log.info("call transfer ");
        return restClient.post()
                .uri(accountsUrl + "/accounts/transfer")  // baseUrl уже установлен в бине
                .body(request)
                .retrieve()
                .body(AccountDto.class);
    }
}
