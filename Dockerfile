# Dockerfile para la aplicación Spring Boot
# Este archivo es opcional y permite dockerizar también la aplicación

# Etapa 1: Build con Maven y Java 21
FROM mcr.microsoft.com/devcontainers/java:1-21-bullseye AS build
WORKDIR /app

# Instalar Maven (última versión)
ARG MAVEN_VERSION=3.9.9

# Instalar dependencias y Maven con múltiples reintentos y mirrors alternativos
RUN apt-get update && \
    apt-get install -y wget curl ca-certificates && \
    update-ca-certificates && \
    (wget --timeout=30 --tries=3 --dns-timeout=10 \
        https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
        -P /tmp || \
     wget --timeout=30 --tries=3 --dns-timeout=10 \
        https://dlcdn.apache.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
        -P /tmp || \
     curl -L --max-time 60 --retry 3 --retry-delay 5 \
        https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
        -o /tmp/apache-maven-${MAVEN_VERSION}-bin.tar.gz) && \
    tar xf /tmp/apache-maven-${MAVEN_VERSION}-bin.tar.gz -C /opt && \
    ln -s /opt/apache-maven-${MAVEN_VERSION} /opt/maven && \
    rm -f /tmp/apache-maven-${MAVEN_VERSION}-bin.tar.gz

ENV M2_HOME=/opt/maven
ENV MAVEN_HOME=/opt/maven
ENV PATH=${M2_HOME}/bin:${PATH}

# Copiar archivos de configuración Maven
COPY pom.xml .

# Descargar dependencias (se cachea esta capa)
RUN mvn dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Compilar la aplicación
RUN mvn clean package -DskipTests

# Etapa 2: Runtime con Java 21 + Maven + SonarScanner
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

USER root

# Instalar Maven
ARG MAVEN_VERSION=3.9.9
RUN apt-get update && \
    apt-get install -y wget curl unzip ca-certificates && \
    wget -q https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz -O /tmp/maven.tar.gz && \
    tar xf /tmp/maven.tar.gz -C /opt && \
    ln -s /opt/apache-maven-${MAVEN_VERSION} /opt/maven && \
    rm -f /tmp/maven.tar.gz && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

ENV M2_HOME=/opt/maven
ENV MAVEN_HOME=/opt/maven
ENV PATH=${M2_HOME}/bin:${PATH}

# Crear usuario no-root
RUN groupadd -r spring && useradd -r -g spring spring -m

# Copiar el JAR desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Copiar código fuente y pom.xml para análisis de SonarQube
COPY --from=build /app/src ./src
COPY --from=build /app/pom.xml ./pom.xml
COPY --from=build /app/target ./target

# Crear directorios y dar permisos
RUN mkdir -p /home/spring/.m2 /.m2 /app/uploads && \
    chown -R spring:spring /app /home/spring/.m2 /.m2 /app/uploads

USER spring:spring

# Puerto de la aplicación
EXPOSE 8082

# Variables de entorno (pueden ser sobrescritas)
ENV SPRING_PROFILES_ACTIVE=docker
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Healthcheck
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8082/actuator/health || exit 1

# Punto de entrada
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

