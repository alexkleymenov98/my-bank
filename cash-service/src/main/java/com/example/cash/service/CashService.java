package com.example.cash.service;

import com.example.cash.client.AccountsClient;
import com.example.cash.dto.*;
import com.example.cash.metrics.CashMetrics;
import com.example.cash.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shared.producer.NotificationProducer;

@Slf4j
@Service
public class CashService {

    private final AccountsClient accountsClient;
    private final NotificationProducer notificationProducer;
    private final CashMetrics cashMetrics;

    public CashService(AccountsClient accountsClient, NotificationProducer notificationProducer, CashMetrics cashMetrics) {
        this.accountsClient = accountsClient;
        this.notificationProducer = notificationProducer;
        this.cashMetrics = cashMetrics;
    }

    public AccountDto operationCash(CashOperationRequest request){

        AccountDto result;

        String sub = SecurityUtils.getCurrentUserSub();

        AccountOperationRequest accountRequest = new AccountOperationRequest(sub, request.amount());

        log.info("Отправляем в account-service");
        if(request.action().equals(CashAction.PUT)){
            result =  accountsClient.deposit(accountRequest);
        } else {
            try {
                result = accountsClient.withdraw(accountRequest);
            } catch (Exception e){
                cashMetrics.recordWithdrawFailed(sub);
                throw e;
            }
        }

        String eventType = request.action() == CashAction.PUT ? "CASH_DEPOSIT" : "CASH_WITHDRAW";
        String verb = request.action() == CashAction.PUT ? "Пополнение" : "Снятие";

        log.info("Отправляем в kafka");
        notificationProducer.send(eventType, sub, "%s на сумму %s руб выполнено".formatted(verb, request.amount()));

        return result;
    }

}
