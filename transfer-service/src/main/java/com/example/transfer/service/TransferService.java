package com.example.transfer.service;

import com.example.transfer.client.AccountsClient;
import com.example.transfer.dto.AccountDto;
import com.example.transfer.dto.TransferRequest;
import com.example.transfer.dto.TransferRequestAccounts;
import com.example.transfer.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import shared.producer.NotificationProducer;

@Service
public class TransferService {

    private final AccountsClient accountsClient;
    private final NotificationProducer notificationProducer;

    public TransferService(AccountsClient accountsClient, NotificationProducer notificationProducer) {
        this.accountsClient = accountsClient;
        this.notificationProducer = notificationProducer;
    }

    public AccountDto transfer(TransferRequest request){

        AccountDto result;

        String sub = SecurityUtils.getCurrentUserSub();

        TransferRequestAccounts accountRequest = new TransferRequestAccounts(sub, request.target(),  request.amount());

        result = accountsClient.transfer(accountRequest);

        notificationProducer.send("TRANSFER", sub, "Перевод на сумму " + request.amount() + " руб на пользователя " + request.target() + " выполнен");

        return result;
    }

}
