# My Project
| Модуль                  | Порт |
|-------------------------|------|
| `accounts-service`      | 8080 | 
| `cash-service`          | 8080 |
| `transfer-service`      | 8080 | 
| `notifications-service` | 8080 |
| `front`                 | 8080 | 
| `postgres`              | 5432 | 
| `keycloak`              | 8080 | 


## Локальный запуск (без Docker)

```bash
docker compose up -d postgres keycloak
gradle :accounts-service:bootRun
gradle :notifications-service:bootRun
gradle :cash-service:bootRun
gradle :transfer-service:bootRun
gradle :front:bootRun
```

## Подготовка образов docker

```bash
docker build -t bank/accounts-service:1.1.0   -f   ./accounts-service/Dockerfile .
docker build -t bank/cash-service:1.1.0   -f       ./cash-service/Dockerfile .
docker build -t bank/transfer-service:1.1.0   -f   ./transfer-service/Dockerfile .
docker build -t bank/notifications-service:1.1.0   -f   ./notification-service/Dockerfile .
docker build -t bank/front:1.1.0   -f   ./front/Dockerfile .
```


### Установка ingress-nginx

```bash
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo update

helm upgrade --install ingress-nginx ingress-nginx/ingress-nginx \
  --namespace ingress-nginx --create-namespace \
  --wait
```

### Прописать hostnames

```bash
# macOS / Linux:
echo "127.0.0.1 bank.local auth.bank.local" | sudo tee -a /etc/hosts
```

## Развёртывание

```bash
cd deploy/helm/bank

# Подтянуть transitive dependencies (Bitnami Keycloak с charts.bitnami.com)
helm dependency update

# Узнать IP ingress controller для hostAliases
INGRESS_IP=$(kubectl -n ingress-nginx get svc ingress-nginx-controller \
  -o jsonpath='{.spec.clusterIP}')

# Создать namespace и поставить релиз
kubectl create namespace bank-dev

helm install bank . \
  --values values.yaml \
  --set "global.hostAliases[0].ip=${INGRESS_IP}" \
  --set "global.hostAliases[0].hostnames[0]=auth.bank.local" \
  --wait --timeout 5m

# Проверить состояние
kubectl -n bank-dev get pods,svc,ingress,statefulsets,configmaps,secrets
```



### Тестовые пользователи (пароль `12345` у всех)
- `user`
- `test`