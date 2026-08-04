FROM gradle:8.10-jdk21 AS build
WORKDIR /workspace

# Копируем только gradle файлы для кэширования зависимостей
COPY settings.gradle .
COPY build.gradle .
COPY gradlew .
COPY gradle gradle/

# Копируем все модули
COPY accounts-service accounts-service/
COPY cash-service cash-service/
COPY transfer-service transfer-service/
COPY notification-service notification-service/
COPY front front/

RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew :notification-service:build -x test -x contractTest

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/notification-service/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]