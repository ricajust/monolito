# Imagem base com o Java 21 instalado.
FROM openjdk:21-jdk-slim

# Defina o diretório de trabalho dentro do container
WORKDIR /app

# Copie o arquivo JAR construído para dentro do container. Assumindo que o JAR está na pasta target.
COPY target/*.jar app.jar

# Exponha a porta em que a sua aplicação Java está rodando (geralmente 8080 para Spring Boot)
EXPOSE 8080

# Execute a aplicação Java
ENTRYPOINT ["java", "-jar", "app.jar"]
FROM openjdk:21-jdk-slim

# Defina o diretório de trabalho dentro do container
WORKDIR /app

# Copie o arquivo JAR construído para dentro do container. Assumindo que o JAR está na pasta target.
COPY target/*.jar app.jar

# Exponha a porta em que a sua aplicação Java está rodando (geralmente 8080 para Spring Boot)
EXPOSE 8080

# Execute a aplicação Java
ENTRYPOINT ["java", "-jar", "app.jar"]