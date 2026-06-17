package com.example.cash.controller;

import com.example.cash.dto.AccountOperationRequest;
import com.example.cash.dto.AccountResponse;
import com.example.cash.dto.AccountTransferRequest;
import com.example.cash.dto.AccountUpdate;
import com.example.cash.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/user")
    public AccountResponse getUser() {
        return accountService.getUser();
    }

    @GetMapping("/users")
    public List<AccountResponse> getUsers() {
        return accountService.getUsers();
    }

    @PostMapping("/user")
    public AccountResponse updateUser(@RequestBody AccountUpdate accountUpdate) {
        return accountService.updateUser(accountUpdate);
    }

    @PostMapping("/deposit")
    public AccountResponse deposit(@RequestBody AccountOperationRequest request) {
        log.info("deposit request: {}", request);

       return accountService.deposit(request);
    }

    @PostMapping("/withdraw")
    public AccountResponse withdraw(@RequestBody AccountOperationRequest request) {

        return accountService.withDraw(request);
    }

    @PostMapping("/transfer")
    public AccountResponse transfer(@RequestBody AccountTransferRequest request) {

        return accountService.transfer(request);
    }
}
