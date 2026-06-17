package com.example.transfer.service;

import com.example.transfer.client.AccountsClient;
import com.example.transfer.dto.AccountDto;
import com.example.transfer.dto.TransferRequest;
import com.example.transfer.dto.TransferRequestAccounts;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class TransferService {

    private final AccountsClient accountsClient;

    public TransferService(AccountsClient accountsClient) {
        this.accountsClient = accountsClient;
    }

    public AccountDto transfer(TransferRequest request){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new SecurityException("Пользователь не аутентифицирован");
        }

        TransferRequestAccounts accountRequest = new TransferRequestAccounts(auth.getName(), request.target(),  request.amount());


        return accountsClient.transfer(accountRequest);
    }

}
