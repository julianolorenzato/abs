FROM clojure:temurin-17-tools-deps-jammy as build

COPY . /app

WORKDIR /app

RUN clj -P
RUN clj -T:build uber :main delivery.core

FROM eclipse-temurin:17-jammy

WORKDIR /app

COPY --from=build /app/target/*-standalone.jar ./app.jar

EXPOSE 8080:8080

CMD ["java", "-jar", "app.jar"]