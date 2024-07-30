# Usa una imagen base de Windows con Gradle y JDK 17 instalado
FROM mcr.microsoft.com/openjdk/jdk:17-windowsservercore-ltsc2019 AS builder

# Establece el directorio de trabajo en el contenedor
WORKDIR /app

# Copia los archivos de configuración de Gradle y el código fuente
COPY build.gradle settings.gradle ./
COPY src ./src

# Construye el proyecto utilizando Gradle
RUN gradle bootJar

# Usa una imagen base de Windows con JDK para la etapa final
FROM mcr.microsoft.com/openjdk/jdk:17-windowsservercore-ltsc2019

# Establece el directorio de trabajo en el contenedor
WORKDIR /app

# Copia el archivo JAR generado desde la etapa de construcción
COPY --from=builder /app/build/libs/*.jar app.jar

# Expone el puerto 8080 para la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
