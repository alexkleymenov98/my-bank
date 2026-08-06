package com.example.cash.service;

import com.example.cash.client.AccountsClient;
import com.example.cash.dto.*;
import com.example.cash.producer.NotificationProducer;
import com.example.cash.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CashService {

    private final AccountsClient accountsClient;
    private final NotificationProducer notificationProducer;

    public CashService(AccountsClient accountsClient, NotificationProducer notificationProducer) {
        this.accountsClient = accountsClient;
        this.notificationProducer = notificationProducer;
    }

    public AccountDto operationCash(CashOperationRequest request){

        AccountDto result;

        String sub = SecurityUtils.getCurrentUserSub();

        AccountOperationRequest accountRequest = new AccountOperationRequest(sub, request.amount());

        log.info("Отправляем в account-service");
        if(request.action().equals(CashAction.PUT)){
            result =  accountsClient.deposit(accountRequest);
        } else {
            result = accountsClient.withdraw(accountRequest);
        }

        String eventType = request.action() == CashAction.PUT ? "CASH_DEPOSIT" : "CASH_WITHDRAW";
        String verb = request.action() == CashAction.PUT ? "Пополнение" : "Снятие";

        log.info("Отправляем в kafka");
        notificationProducer.send(eventType, sub, "%s на сумму %s руб выполнено".formatted(verb, request.amount()));

        return result;
    }

}
