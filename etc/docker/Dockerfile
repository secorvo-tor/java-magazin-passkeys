FROM maven:3.9.9-eclipse-temurin-21-alpine AS build

RUN apk add chromium
ENV CHROME_BIN=/usr/bin/chromium-browser

RUN mkdir /build
COPY ./ /build/

WORKDIR /build
RUN ./mvnw -P backend -Dskip.tests clean package
RUN ./mvnw -P frontend -Dskip.tests clean package
RUN ./mvnw -P build-app package

FROM eclipse-temurin:21-alpine

COPY --from=build passkey-backend/target/passkey-backend*.jar /passkey-app.jar

CMD java -jar -Dspring.profiles.active=$ACTIVE_PROFILE \
    $JAVA_OPTS \
    passkey-app.jar