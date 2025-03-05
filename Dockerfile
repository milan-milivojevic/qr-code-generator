FROM openjdk:17-jdk-slim
EXPOSE 8080
ARG JAR_FILE="target/qr-code-generator-0.0.3.jar"
ADD ${JAR_FILE} qr-code-generator.jar
ENTRYPOINT [ "java", "-jar", "/qr-code-generator.jar" ]