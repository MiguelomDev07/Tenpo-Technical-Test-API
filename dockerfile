# Usa una imagen oficial de Java
FROM openjdk:21-jdk

# Establece el directorio de trabajo
WORKDIR /app

# Copia el JAR generado al contenedor
COPY target/*.jar app.jar

# Expone el puerto 8080
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]