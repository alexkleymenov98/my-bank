package com.example.accounts.service;

import com.example.accounts.dto.AccountOperationRequest;
import com.example.accounts.dto.AccountResponse;
import com.example.accounts.dto.AccountTransferRequest;
import com.example.accounts.dto.AccountUpdate;
import com.example.accounts.exception.AccountNotFoundException;
import com.example.accounts.exception.ValidationBalanceException;
import com.example.accounts.model.AccountEntity;
import com.example.accounts.repository.AccountRepository;
import com.example.accounts.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shared.producer.NotificationProducer;


import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;
    private final NotificationProducer notificationProducer;

    public AccountService(AccountRepository accountRepository , NotificationProducer notificationProducer) {
        this.accountRepository = accountRepository;
        this.notificationProducer = notificationProducer;
    }


    public AccountResponse getUserById(String userId){

        AccountEntity account = accountRepository.findByUserId(userId)
                .orElseThrow(()->new AccountNotFoundException(userId));

        return new AccountResponse(account.getUserId(), account.getName(), account.getBirthdate(), account.getBalance());
    }

    public AccountResponse getUser(){

        String sub = SecurityUtils.getCurrentUserSub();

        return getUserById(sub);
    }

    public List<AccountResponse> getUsers(){
        String sub = SecurityUtils.getCurrentUserSub();

        return accountRepository.findAll().stream().filter(accountEntity -> !Objects.equals(accountEntity.getUserId(), sub)).map(account -> new AccountResponse(account.getUserId(), account.getName(), account.getBirthdate(), account.getBalance())).toList();
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

        notificationProducer.send("UPDATE_INFO", sub, "Обновленные данные: ФИО: %s День рождение: %s".formatted(accountUpdate.name(), accountUpdate.birthdate()) );

        return getUserById(sub);
    }

    public AccountResponse deposit(AccountOperationRequest accountChangeBalance) {
        AccountEntity account = accountRepository.findByUserId(accountChangeBalance.login()).orElse(null);

        if(account == null){
            throw new AccountNotFoundException(accountChangeBalance.login());
        }

        account.setBalance(account.getBalance().add(accountChangeBalance.amount()));

        accountRepository.save(account);

        return getUserById(account.getUserId());

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

        return getUserById(account.getUserId());
    }

    @Transactional
    public AccountResponse transfer(AccountTransferRequest accountTransferRequest) {

        log.info("Log " + accountTransferRequest.toString());

        deposit(new AccountOperationRequest(accountTransferRequest.target(), accountTransferRequest.amount()));

        withDraw(new AccountOperationRequest(accountTransferRequest.login(), accountTransferRequest.amount()));

        return getUserById(accountTransferRequest.login());
    }
}
