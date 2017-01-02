FROM openjdk:8

ARG fatjar

COPY /maven/$fatjar /app/app.jar

WORKDIR /app

CMD ["java", "-jar", "app.jar", "mongo-host"]