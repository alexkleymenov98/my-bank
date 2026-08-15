package ru.yandex.practicum.mybankfront.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.yandex.practicum.mybankfront.dto.AccountDto;
import ru.yandex.practicum.mybankfront.dto.AccountUpdate;
import ru.yandex.practicum.mybankfront.dto.CashRequest;
import ru.yandex.practicum.mybankfront.dto.TransferRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class BankApiClient {
    private final RestClient restClient;

    @Value("${services.gateway.url}")
    private String gatewayUrl;


    public BankApiClient(@Qualifier("gatewayRestClient") RestClient restClient) {
        this.restClient = restClient;
        log.info("BankApiClient initialized with RestClient: {}", restClient.getClass());
    }

    public AccountDto getAccount() {
        log.info("Get getAccount");
        return restClient.get()
                .uri(gatewayUrl + "/accounts/user")  // baseUrl уже установлен в бине
                .retrieve()
                .body(AccountDto.class);
    }

    public List<AccountDto> getAccounts() {
        log.info("Get getAccounts");
        // Если API возвращает массив
        AccountDto[] accounts = restClient.get()
                .uri(gatewayUrl + "/accounts/users")
                .retrieve()
                .body(AccountDto[].class);

        return accounts != null ? Arrays.asList(accounts) : Collections.emptyList();
    }

    public AccountDto updateAccount(AccountUpdate accountUpdate) {
        log.info("Update account: {}", accountUpdate);
        return restClient.post()
                .uri(gatewayUrl  + "/accounts/user")
                .body(accountUpdate)
                .retrieve()
                .body(AccountDto.class);
    }

    public AccountDto updateCash(CashRequest cashRequest) {
        log.info("change cash: {}", cashRequest);
        return restClient.post()
                .uri(gatewayUrl  + "/cash/balance")
                .body(cashRequest)
                .retrieve()
                .body(AccountDto.class);
    }

    public AccountDto transfer(TransferRequest transferRequest) {
        log.info("change transfer: {}", transferRequest);

        return restClient.post()
                .uri(gatewayUrl  + "/transfer")
                .body(transferRequest)
                .retrieve()
                .body(AccountDto.class);
    }

}
