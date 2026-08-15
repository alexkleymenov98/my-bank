FROM gradle:8.10-jdk21 AS build
WORKDIR /workspace

COPY settings.gradle .
COPY build.gradle .
COPY gradlew .
COPY gradle gradle/

# Копируем все модули
COPY shared shared/
COPY accounts-service accounts-service/
COPY cash-service cash-service/
COPY transfer-service transfer-service/
COPY notification-service notification-service/
COPY front front/

RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew :shared:build -x test

RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew :front:build -x test -x contractTest

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/front/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]