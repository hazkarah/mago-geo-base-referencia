FROM openjdk:17-jdk-alpine
VOLUME /tmp
VOLUME /log

RUN apk update && apk add curl htop

RUN mkdir /app

ADD target/*.jar /app/app.jar
ADD target/lib /app/lib

WORKDIR /app

ARG APP_PARAMS="-Dteste=1"
ARG APP_PROFILE="dev"

ENV APP_PROFILE="$APP_PROFILE"

ENTRYPOINT exec java $APP_PARAMS \
 -Djava.security.egd=file:/dev/./urandom \
 -Duser.timezone=Brazil/East \
 -Dspring.profiles.active=$APP_PROFILE \
 -jar app.jar --info

EXPOSE 8080
