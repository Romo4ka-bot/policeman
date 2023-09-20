FROM openjdk:11-ea-jre-slim
WORKDIR .
RUN mkdir /app
COPY /target/policeman-0.0.1.jar /app/application.jar
CMD ["java", "-jar", "/app/application.jar"]