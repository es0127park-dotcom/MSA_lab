FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew bootJar -x test
RUN cp build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]