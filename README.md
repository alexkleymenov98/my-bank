# My Project
| Модуль                  | Порт |
|-------------------------|------| 
| `eureka-service`        | 8761 |
| `config-service`        | 8088 | 
| `gateway-service`       | 8080 | 
| `accounts-service`      | 3000 | 
| `cash-service`          | 3002 |
| `transfer-service`      | 3001 | 
| `notifications-service` | 3003 |
| `front`                 | 8000 | 
| `postgres`              | 5432 | 
| `keycloak`              | 9000 | 


## Запуск

### Через Docker Compose

```bash
docker compose up -d --build
```

## Локальный запуск (без Docker)

```bash
docker compose up -d postgres keycloak
# В отдельных терминалах:
gradle :eureka-service:bootRun
gradle :config-service:bootRun
gradle :accounts-service:bootRun
gradle :notifications-service:bootRun
gradle :cash-service:bootRun
gradle :transfer-service:bootRun
gradle :gateway-service:bootRun
gradle :front:bootRun
```



## Тестирование

```bash
                                # все тесты
gradle :accounts-service:contractTest              
gradle :cash-service:test
gradle :transfer-service:test

```

### Тестовые пользователи (пароль `12345` у всех)
- `user`
- `test`