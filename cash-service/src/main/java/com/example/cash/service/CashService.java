package com.example.cash.service;

import com.example.cash.client.AccountsClient;
import com.example.cash.dto.AccountDto;
import com.example.cash.dto.AccountOperationRequest;
import com.example.cash.dto.CashAction;
import com.example.cash.dto.CashOperationRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CashService {

    private final AccountsClient accountsClient;

    public CashService(AccountsClient accountsClient) {
        this.accountsClient = accountsClient;
    }

    public AccountDto operationCash(CashOperationRequest request){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new SecurityException("Пользователь не аутентифицирован");
        }

        AccountOperationRequest accountRequest = new AccountOperationRequest(auth.getName(), request.amount());

        if(request.action().equals(CashAction.PUT)){
            return accountsClient.deposit(accountRequest);
        }

        return accountsClient.withdraw(accountRequest);
    }

}
