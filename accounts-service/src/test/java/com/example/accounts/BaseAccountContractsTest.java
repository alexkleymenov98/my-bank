package com.example.accounts;

import com.example.accounts.controller.AccountController;
import com.example.accounts.dto.AccountOperationRequest;
import com.example.accounts.dto.AccountResponse;
import com.example.accounts.service.AccountService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.when;

@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
public abstract class BaseAccountContractsTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockitoBean
    protected AccountService accountService;

    @BeforeEach
    void setup() {
        RestAssuredMockMvc.mockMvc(mockMvc);

        when(accountService.deposit(new AccountOperationRequest("test", BigDecimal.valueOf(1000))))
                .thenReturn(new AccountResponse("test", "Иван Иванов", LocalDate.of(1998, 5, 20), BigDecimal.valueOf(2000)));

        when(accountService.withDraw(new AccountOperationRequest("test", BigDecimal.valueOf(1000))))
                .thenReturn(new AccountResponse("test", "Иван Иванов", LocalDate.of(1998, 5, 20), BigDecimal.valueOf(1000)));

        when(accountService.getUser())
                .thenReturn(new AccountResponse("test", "Иван Иванов", LocalDate.of(1998, 5, 20), BigDecimal.valueOf(1500)));
    }
}

