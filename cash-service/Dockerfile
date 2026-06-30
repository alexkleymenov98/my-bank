FROM gradle:8.10-jdk21 AS build
WORKDIR /workspace

# Копируем только gradle файлы для кэширования зависимостей
COPY settings.gradle .
COPY build.gradle .
COPY gradlew .
COPY gradle gradle/

# Копируем все модули
COPY eureka-service eureka-service/
COPY config-service config-service/
COPY gateway-service gateway-service/
COPY accounts-service accounts-service/
COPY cash-service cash-service/
COPY transfer-service transfer-service/
COPY notification-service notification-service/
COPY front front/

# Собираем только eureka-server с кэшированием зависимостей
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew :cash-service:build -x test -x contractTest

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/cash-service/build/libs/*.jar app.jar
EXPOSE 3002
ENTRYPOINT ["java", "-jar", "/app/app.jar"]