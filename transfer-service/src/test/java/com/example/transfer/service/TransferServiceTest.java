package com.example.transfer.service;

import com.example.transfer.client.AccountsClient;
import com.example.transfer.dto.AccountDto;
import com.example.transfer.dto.TransferRequest;
import com.example.transfer.dto.TransferRequestAccounts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TransferServiceTest {

    @Mock
    private AccountsClient accountsClient;

    @Mock
    private NotificationClient notificationClient;

    @InjectMocks
    private TransferService transferService;

    private final AccountDto stub = new AccountDto(
            "test", "Ivan Ivanov", LocalDate.of(1990, 1, 1), BigDecimal.ZERO);


    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();

        Authentication auth = new UsernamePasswordAuthenticationToken(
                "test",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    @WithMockUser(username = "test") // Добавляем mock пользователя
    void transferTest() {

        TransferRequest request = new TransferRequest("petr", new BigDecimal(10000));
        when(accountsClient.transfer(any(TransferRequestAccounts.class))).thenReturn(stub);

        AccountDto result = transferService.transfer(request);

        assertNotNull(result);
        assertEquals("test", result.login());
        assertEquals("Ivan Ivanov", result.name());
        assertEquals(BigDecimal.ZERO, result.balance());
    }
}
