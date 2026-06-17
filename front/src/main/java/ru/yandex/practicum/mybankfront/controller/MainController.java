package ru.yandex.practicum.mybankfront.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.mybankfront.client.BankApiClient;
import ru.yandex.practicum.mybankfront.dto.AccountDto;
import ru.yandex.practicum.mybankfront.controller.dto.CashAction;
import ru.yandex.practicum.mybankfront.controller.stub.AccountStub;
import ru.yandex.practicum.mybankfront.dto.AccountUpdate;
import ru.yandex.practicum.mybankfront.dto.CashRequest;
import ru.yandex.practicum.mybankfront.dto.TransferRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

/**
 * Контроллер main.html.
 *
 * Используемая модель для main.html:
 *      model.addAttribute("name", name);
 *      model.addAttribute("birthdate", birthdate.format(DateTimeFormatter.ISO_DATE));
 *      model.addAttribute("sum", sum);
 *      model.addAttribute("accounts", accounts);
 *      model.addAttribute("errors", errors);
 *      model.addAttribute("info", info);
 *
 * Поля модели:
 *      name - Фамилия Имя текущего пользователя, String (обязательное)
 *      birthdate - дата рождения текущего пользователя, String в формате 'YYYY-MM-DD' (обязательное)
 *      sum - сумма на счету текущего пользователя, Integer (обязательное)
 *      accounts - список аккаунтов, которым можно перевести деньги, List<AccountDto> (обязательное)
 *      errors - список ошибок после выполнения действий, List<String> (не обязательное)
 *      info - строка успешности после выполнения действия, String (не обязательное)
 *
 * С примерами использования можно ознакомиться в тестовом классе заглушке AccountStub
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class MainController {
    // TODO: Удалить заглушку, так как используется только для ознакомительных целей
    @Autowired
    private AccountStub accountStub;

    private final BankApiClient bankApiClient;

    /**
     * GET /.
     * Редирект на GET /account
     */
    @GetMapping
    public String index() {
        return "redirect:/account";
    }

    /**
     * GET /account.
     * Что нужно сделать:
     * 1. Сходить в сервис accounts через Gateway API для получения данных аккаунта по REST
     * 2. Заполнить модель main.html полученными из ответа данными
     * 3. Текущего пользователя можно получить из контекста Security
     */
    @GetMapping("/account")
    public String getAccount(Model model) {

        fillModel(model);

        return "main";
    }

    /**
     * POST /account.
     * Что нужно сделать:
     * 1. Сходить в сервис accounts через Gateway API для изменения данных текущего пользователя по REST
     * 2. Заполнить модель main.html полученными из ответа данными
     * 3. Текущего пользователя можно получить из контекста Security
     *
     * Изменяемые данные:
     * 1. name - Фамилия Имя
     * 2. birthdate - дата рождения в формате YYYY-DD-MM
     */
    @PostMapping("/account")
    public String editAccount(
            Model model,
            @RequestParam("name") String name,
            @RequestParam("birthdate") LocalDate birthdate
    ) {
        try {
            AccountDto account = bankApiClient.updateAccount(new AccountUpdate(name, birthdate));
            model.addAttribute("name", account.name());
            model.addAttribute("birthdate", account.birthdate());
            fillModel(model);
        } catch (Exception e) {
            fillDegradedModel(model, "Ошибка обновления пользователя");
        }

        return "main";
    }

    /**
     * POST /cash.
     * Что нужно сделать:
     * 1. Сходить в сервис cash через Gateway API для снятия/пополнения счета текущего аккаунта по REST
     * 2. Заполнить модель main.html полученными из ответа данными
     * 3. Текущего пользователя можно получить из контекста Security
     *
     * Параметры:
     * 1. value - сумма списания
     * 2. action - GET (снять), PUT (пополнить)
     */
    @PostMapping("/cash")
    public String editCash(
            Model model,
            @RequestParam("value") int value,
            @RequestParam("action") CashAction action
            ) {

        try {
            CashRequest  request = new CashRequest(value, action);
            AccountDto account = bankApiClient.updateCash(request);
            model.addAttribute("name", account.name());
            model.addAttribute("birthdate", account.birthdate());
            model.addAttribute("sum", account.balance());
            fillModel(model);
        } catch (Exception e) {
            fillDegradedModel(model, "Ошибка изменения счета");
        }


        return "main";
    }

    /**
     * POST /transfer.
     * Что нужно сделать:
     * 1. Сходить в сервис accounts через Gateway API для перевода со счета текущего аккаунта на счет другого аккаунта по REST
     * 2. Заполнить модель main.html полученными из ответа данными
     * 3. Текущего пользователя можно получить из контекста Security
     *
     * Параметры:
     * 1. value - сумма списания
     * 2. login - логин пользователя получателя
     */
    @PostMapping("/transfer")
    public String transfer(
            Model model,
            @RequestParam("value") int value,
            @RequestParam("login") String login
    ) {
        try {
            TransferRequest request = new TransferRequest(login, BigDecimal.valueOf(value));
            AccountDto account = bankApiClient.transfer(request);
            model.addAttribute("name", account.name());
            model.addAttribute("birthdate", account.birthdate());
            model.addAttribute("sum", account.balance());
            fillModel(model);
        } catch (Exception e) {
            fillDegradedModel(model, "Ошибка выполнения перевода");
        }

        return "main";
    }

    private void fillModel(Model model){
        AccountDto account;
        List<AccountDto> accounts;

        try {
            log.info("controller get account fillModel");
            account = bankApiClient.getAccount();
            accounts = bankApiClient.getAccounts();

        } catch (Exception e) {
            log.error("Не удалось получить данные аккаунта: {}", e.getMessage());
            fillDegradedModel(model, "Ошибка получения информации о пользователе");
            return;
        }

        if(account == null) {
            fillDegradedModel(model, "Сервисы не доступны");
            return;
        };

        model.addAttribute("name", account.name() != null ? account.name() : "ass");
        model.addAttribute("birthdate",
                account.birthdate() != null ? account.birthdate().format(DateTimeFormatter.ISO_DATE) : "");
        model.addAttribute("sum",account.balance() != null ? account.balance() : BigDecimal.ZERO);
        model.addAttribute("accounts", accounts);

    }

    private void fillDegradedModel(Model model, String errorMessage) {
        model.addAttribute("name", "");
        model.addAttribute("birthdate", LocalDate.now().format(DateTimeFormatter.ISO_DATE));
        model.addAttribute("sum", BigDecimal.ZERO);
        model.addAttribute("accounts", Collections.emptyList());
        model.addAttribute("errors", List.of(errorMessage));
        model.addAttribute("info", null);
    }
}
