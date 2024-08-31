FROM openjdk:8-jdk-alpine
VOLUME /tmp
VOLUME /log
ADD target/*.jar app.jar

ARG SPRING_PROFILES_ACTIVE="dev"

ENTRYPOINT exec java $PANDORA \
 -Djava.security.egd=file:/dev/./urandom \
 -Duser.timezone=Brazil/East \
 -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE \
 -jar app.jar --info
