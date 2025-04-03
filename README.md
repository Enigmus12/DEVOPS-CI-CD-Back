# Sistema de Reservas de Laboratorios

## Objetivo del Proyecto
El objetivo de este proyecto es desarrollar un sistema de reservas de laboratorios para la decanatura de Ingeniería de Sistemas en la Escuela Colombiana de Ingeniería Julio Garavito. El sistema permitirá gestionar la disponibilidad de los laboratorios, facilitar la reserva de espacios y mejorar la organización de los recursos.

## Tecnologías Utilizadas
- **Java 17**
- **Spring Boot**
- **Maven**
- **Docker**
- **MongoDB Cloud**
- **Azure DevOps**
- **SonarQube**
- **JaCoCo**

## Modelo de Arquitectura Cliente-Servidor
El sistema sigue el modelo de arquitectura cliente-servidor. El backend está desarrollado con **Spring Boot**, proporcionando una API REST para gestionar las reservas. El frontend se desarrollará con **HTML, JavaScript y CSS**, consumiendo los servicios REST del backend. La base de datos utilizada es **MongoDB Cloud**, donde se almacenan las reservas y la información relevante.
![image](https://github.com/user-attachments/assets/d51df5c9-800a-4930-a247-d2e061f9b646)

## Scaffolding del Proyecto
El proyecto fue generado utilizando **Spring Initializr**, configurado con las siguientes dependencias:

### Dependencias Utilizadas
```xml
<dependencies>
    <!-- Spring Boot Starter Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- Spring Boot Starter Data MongoDB -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-mongodb</artifactId>
    </dependency>
    
    <!-- Spring Boot Starter Security -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    
    <!-- Spring Boot Starter Test -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    
    <!-- JaCoCo for Code Coverage -->
    <dependency>
        <groupId>org.jacoco</groupId>
        <artifactId>jacoco-maven-plugin</artifactId>
    </dependency>
</dependencies>
```

## Conexión con la Base de Datos (MongoDB)
El sistema está configurado para conectarse a **MongoDB Cloud**. La configuración de la conexión se encuentra en el archivo `application.properties`:

```properties
spring.data.mongodb.uri=mongodb+srv://usuario:contraseña@cluster.mongodb.net/laboratorios
spring.data.mongodb.database=laboratorios
```

## Diagrama de Clases

![image](https://github.com/user-attachments/assets/3066fd8f-6bd3-4028-a3b5-dfef80df56f5)
![image](https://github.com/user-attachments/assets/b93197aa-092e-4347-90fa-3e6d4c93d599)
![image](https://github.com/user-attachments/assets/99902ea8-1eea-4ef6-8c18-15af3146fe2d)



## Pruebas y Cobertura de Código (JaCoCo)
Para ejecutar las pruebas y generar el reporte de cobertura con **JaCoCo**, usa el siguiente comando:

```sh
mvn test
```
![image](https://github.com/user-attachments/assets/f791215e-d6b3-48e6-8ef6-051a813487d9)

![image](https://github.com/user-attachments/assets/a5d31cab-a284-4bc5-ade2-b86a63e29df9)


El reporte de cobertura se generará en:
```
target/site/jacoco/index.html
```

## Sonar


Ejecutamos el comando para crear el docker en donde se guardara el token
docker run -d --name sonarqube -e SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true -p 9000:9000 sonarqube:latest

Compilamos el proyecto con el token generado en la pagina
![image](https://github.com/user-attachments/assets/88c30117-0c6c-4c58-9560-92ee79560465)


Miramos la cobertura


![image](https://github.com/user-attachments/assets/e7685c2b-5959-4682-8313-5f7f1b624576)



![image](https://github.com/user-attachments/assets/fc447250-66ad-44e1-8c21-76f39fb2d06e)

## Integrantes del Proyecto
### Juan David Rodriguez
### Esteban Aguilera Contreras

---



