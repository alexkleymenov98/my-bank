package com.example.transfer.service;

import com.example.transfer.client.AccountsClient;
import com.example.transfer.client.NotificationClient;
import com.example.transfer.dto.AccountDto;
import com.example.transfer.dto.NotificationRequest;
import com.example.transfer.dto.TransferRequest;
import com.example.transfer.dto.TransferRequestAccounts;
import com.example.transfer.utils.SecurityUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class TransferService {

    private final AccountsClient accountsClient;
    private final NotificationClient notificationClient;

    public TransferService(AccountsClient accountsClient, NotificationClient notificationClient) {
        this.accountsClient = accountsClient;
        this.notificationClient = notificationClient;
    }

    public AccountDto transfer(TransferRequest request){

        AccountDto result;

        String sub = SecurityUtils.getCurrentUserSub();

        TransferRequestAccounts accountRequest = new TransferRequestAccounts(sub, request.target(),  request.amount());

        result = accountsClient.transfer(accountRequest);

        notificationClient.send(new NotificationRequest(sub, "Перевод на сумму " + request.amount() + " руб на пользователя " + request.target() + " выполнен"));

        return result;
    }

}
