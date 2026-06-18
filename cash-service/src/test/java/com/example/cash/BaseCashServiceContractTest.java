package com.example.cash;

import com.example.cash.controller.CashController;
import com.example.cash.dto.AccountDto;
import com.example.cash.dto.CashOperationRequest;
import com.example.cash.service.CashService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.when;

@WebMvcTest(CashController.class)
@AutoConfigureMockMvc(addFilters = false)
public abstract class BaseCashServiceContractTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockitoBean
    protected CashService cashService;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);

        when(cashService.operationCash(any(CashOperationRequest.class)))
                .thenReturn(new AccountDto("test", "Иван Иванов", LocalDate.of(1998, 5, 20), BigDecimal.valueOf(2000)));
    }
}