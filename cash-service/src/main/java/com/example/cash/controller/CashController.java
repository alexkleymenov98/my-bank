package com.example.cash.controller;

import com.example.cash.dto.AccountDto;
import com.example.cash.dto.CashOperationRequest;
import com.example.cash.service.CashService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/cash")
public class CashController {

    private final CashService cashService;

    public CashController(CashService cashService) {
        this.cashService = cashService;
    }

    @PostMapping("/balance")
    public AccountDto operationWithBalance(@RequestBody CashOperationRequest request) {
        log.info("request: {}", request);

        return cashService.operationCash(request);
    }
}
