FROM node as front-build

COPY ./front /src

WORKDIR /src

RUN npm ci \
    && npx @angular/cli build --optimization

FROM gradle:jdk17 as back-build

COPY ./back /src

WORKDIR /src

RUN chmod +x ./gradlew

RUN ./gradlew build

FROM alpine:3.19 as front

COPY --from=front-build /src/dist/microcrm/browser /app/front
COPY misc/docker/Caddyfile /app/Caddyfile

RUN apk add caddy

WORKDIR /app

EXPOSE 80
EXPOSE 443

CMD ["/usr/sbin/caddy", "run"]

FROM alpine:3.19 as back

COPY --from=back-build /src/build/libs /tmp/build-libs

RUN apk add openjdk21-jre-headless
RUN mkdir -p /app/back \
    && find /tmp/build-libs -maxdepth 1 -name '*.jar' ! -name '*-plain.jar' -exec cp {} /app/back/microcrm.jar \;

WORKDIR /app

EXPOSE 8080

CMD ["java", "-jar", "/app/back/microcrm.jar"]

FROM alpine:3.19 as standalone

COPY --from=front / /
COPY --from=back / /
COPY misc/docker/supervisor.ini /app/supervisor.ini

RUN apk add supervisor

WORKDIR /app

CMD ["/usr/bin/supervisord", "-c", "/app/supervisor.ini"]



