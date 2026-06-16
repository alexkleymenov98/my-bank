package com.example.cash.service;

import com.example.cash.dto.AccountOperationRequest;
import com.example.cash.dto.AccountResponse;
import com.example.cash.dto.AccountUpdate;
import com.example.cash.exception.AccountNotFoundException;
import com.example.cash.exception.ValidationBalanceException;
import com.example.cash.model.AccountEntity;
import com.example.cash.repository.AccountRepository;
import com.example.cash.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;

@Service
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountResponse getUser(){

        String sub = SecurityUtils.getCurrentUserSub();

        AccountEntity account = accountRepository.findByUserId(sub).orElse(null);

        if(account == null){
            return new AccountResponse("", "", null, BigDecimal.valueOf(0));
        }


        return new AccountResponse(account.getUserId(), account.getName(), account.getBirthdate(), account.getBalance());
    }


    @Transactional
    public AccountResponse updateUser (AccountUpdate accountUpdate){
        String sub = SecurityUtils.getCurrentUserSub();

        AccountEntity account = accountRepository.findByUserId(sub).orElse(null);

        if(account == null){
            AccountEntity entity = new AccountEntity();

            entity.setUserId(sub);
            entity.setBirthdate(accountUpdate.birthdate());
            entity.setName(accountUpdate.name());
            entity.setBalance(BigDecimal.valueOf(0.00));

            accountRepository.save(entity);

        } else {
            account.setName(accountUpdate.name());
            account.setBirthdate(accountUpdate.birthdate());

            accountRepository.save(account);
        }

        return getUser();
    }

    public AccountResponse deposit(AccountOperationRequest accountChangeBalance) {

        AccountEntity account = accountRepository.findByUserId(accountChangeBalance.login()).orElse(null);

        if(account == null){
            throw new AccountNotFoundException(accountChangeBalance.login());
        }

        account.setBalance(account.getBalance().add(accountChangeBalance.amount()));

        accountRepository.save(account);

        return getUser();

    }

    public AccountResponse withDraw(AccountOperationRequest accountChangeBalance){

        AccountEntity account = accountRepository.findByUserId(accountChangeBalance.login()).orElse(null);

        if(account == null){
            throw new AccountNotFoundException(accountChangeBalance.login());
        }

        if (account.getBalance().compareTo(accountChangeBalance.amount()) < 0) {
            throw  new ValidationBalanceException();
        }

        account.setBalance(account.getBalance().subtract(accountChangeBalance.amount()));

        accountRepository.save(account);

        return getUser();
    }
}
