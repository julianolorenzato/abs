FROM clojure:temurin-17-tools-deps-jammy as build

COPY . /app

WORKDIR /app

RUN clj -P
RUN clj -T:build uber :main upload.core

FROM eclipse-temurin:17-jammy

RUN apt-get update && apt-get install -y ffmpeg

WORKDIR /app

COPY --from=build /app/target/*-standalone.jar ./app.jar

CMD ["java", "-jar", "app.jar"]