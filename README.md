# Positivarium API

Positivarium API is coded in Java Spring Boot. Make sure you have Java 17 installed on your machine.

Create a `application.properties` file and specify the followings :
- PostgreSQL database URL
- Hibernate specifications
- Flyway specifications, locations being `classpath:db/migration`
- CORS