package com.example.cash.service;

import com.example.cash.client.AccountsClient;
import com.example.cash.client.NotificationClient;
import com.example.cash.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CashService {

    private final AccountsClient accountsClient;
    private final NotificationClient notificationClient;

    public CashService(AccountsClient accountsClient, NotificationClient notificationClient) {
        this.accountsClient = accountsClient;
        this.notificationClient = notificationClient;
    }

    public AccountDto operationCash(CashOperationRequest request){

        AccountDto result;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new SecurityException("Пользователь не аутентифицирован");
        }

        AccountOperationRequest accountRequest = new AccountOperationRequest(auth.getName(), request.amount());

        if(request.action().equals(CashAction.PUT)){
            result =  accountsClient.deposit(accountRequest);
        } else {
            result = accountsClient.withdraw(accountRequest);
        }

        String verb = request.action() == CashAction.PUT ? "Пополнение" : "Снятие";

        notificationClient.send(new NotificationRequest(auth.getName(), "%s на сумму %s руб выполнено".formatted(verb, request.amount())));

        return result;
    }

}
