FROM openjdk:11-jre-slim

RUN mkdir -p /app/conf

COPY ./build/libs/api-0.0.1-SNAPSHOT.jar /app

WORKDIR /app

EXPOSE 8081

ARG activeProfile='dev'

ENV SPRING_PROFILES_ACTIVER=$activeProfile

CMD ["java", "-jar", "api-0.0.1-SNAPSHOT.jar"]