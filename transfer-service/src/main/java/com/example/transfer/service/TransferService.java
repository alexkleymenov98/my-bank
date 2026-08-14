package com.example.transfer.service;

import com.example.transfer.client.AccountsClient;
import com.example.transfer.dto.AccountDto;
import com.example.transfer.dto.TransferRequest;
import com.example.transfer.dto.TransferRequestAccounts;
import com.example.transfer.metrics.TransferMetrics;
import com.example.transfer.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shared.producer.NotificationProducer;

@Slf4j
@Service
public class TransferService {

    private final AccountsClient accountsClient;
    private final NotificationProducer notificationProducer;
    private final TransferMetrics transferMetrics;

    public TransferService(AccountsClient accountsClient, NotificationProducer notificationProducer, TransferMetrics transferMetrics) {
        this.accountsClient = accountsClient;
        this.notificationProducer = notificationProducer;
        this.transferMetrics = transferMetrics;
    }

    public AccountDto transfer(TransferRequest request){

        AccountDto result;

        String sub = SecurityUtils.getCurrentUserSub();

        TransferRequestAccounts accountRequest = new TransferRequestAccounts(sub, request.target(),  request.amount());

        try {
            result = accountsClient.transfer(accountRequest);

            notificationProducer.send("TRANSFER", sub, "Перевод на сумму " + request.amount() + " руб на пользователя " + request.target() + " выполнен");
        } catch (Exception e) {
            transferMetrics.recordTransferFailed(sub, request.target());
            throw e;
        }


        return result;
    }

}
