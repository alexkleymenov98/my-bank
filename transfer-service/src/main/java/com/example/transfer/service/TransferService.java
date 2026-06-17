package com.example.transfer.service;

import com.example.transfer.client.AccountsClient;
import com.example.transfer.client.NotificationClient;
import com.example.transfer.dto.AccountDto;
import com.example.transfer.dto.NotificationRequest;
import com.example.transfer.dto.TransferRequest;
import com.example.transfer.dto.TransferRequestAccounts;
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

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new SecurityException("Пользователь не аутентифицирован");
        }

        TransferRequestAccounts accountRequest = new TransferRequestAccounts(auth.getName(), request.target(),  request.amount());

        result = accountsClient.transfer(accountRequest);

        notificationClient.send(new NotificationRequest(auth.getName(), "Перевод на сумму " + request.amount() + " руб на пользователя " + request.target() + " выполнен"));

        return result;
    }

}
