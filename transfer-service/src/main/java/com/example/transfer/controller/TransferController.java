package com.example.transfer.controller;

import com.example.transfer.dto.AccountDto;
import com.example.transfer.dto.TransferRequest;
import com.example.transfer.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transfer")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping()
    public AccountDto transfer(@Valid @RequestBody TransferRequest request) {
        return transferService.transfer(request);

    }
}