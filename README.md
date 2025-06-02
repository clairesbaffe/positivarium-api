# Positivarium API

Positivarium API is coded in Java Spring Boot. Make sure you have Java 17 installed on your machine.

Create a `application.properties` file and specify the followings :
- PostgreSQL database URL
- Hibernate specifications
- Flyway specifications, locations being `classpath:db/migration`
- CORS

Environment needs to have a CLOUDINARY_URL. Please create a [Cloudinary account](https://cloudinary.com/) to get your own API keys.
