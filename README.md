# Le Positivarium - back-end API

**Le Positivarium** is a French-speaking positive-only news web application designed to highlight uplifting stories, scientific progress, and inspiring initiatives. It was developed as part of a Bachelor’s degree project in Web & Application Development.

This is a **RESTful API** built with [Java](https://www.java.com/fr/), [Spring Boot](https://spring.io/projects/spring-boot), and [PostgreSQL](https://www.postgresql.org/).

## Features

- Role-based authentication using JWT: reader, publisher, admin, banned
- Publish, edit, and moderate news articles
- Personalised news feed based on private journal entries
- Private daily journal (only available to regular users)
- Admin interface for user management, moderation, and notifications
- Follow system to track favorite publishers
- Notification system for key events (moderation actions, role updates, etc.)
- API documentation via Swagger UI

## Tech Stack

- Java 17
- Spring Boot
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL
- Docker & Docker Compose
- Swagger / OpenAPI (for API docs)

## Getting Started

### Prerequisites

- [Docker](https://www.docker.com/) and [Docker Compose](https://docs.docker.com/compose/)

### Environment setup

Create a `.env` file at the root of the project using the `.env.example` file provided.

This project uses [Cloudinary](https://cloudinary.com/) for media (image) uploads.  
You need to create a Cloudinary account and get your API credentials (API key, API secret, and cloud name).

Then fill in the following in your `.env` file:

```env
CLOUDINARY_URL=cloudinary://<api_key>:<api_secret>@<cloud_name>
```

### Run the application

```bash
docker-compose up --build
```

This will start the application and its PostgreSQL database using Docker.

Application will be available at http://localhost:8081.

You can access Swagger UI documentation at : http://localhost:8081/swagger-ui.html

## Testing

Unit tests are written using JUnit and Mockito.

Run tests using:

```bash
./mvnw test
```

- Tests cover one of the most critical services
- Test structure serves as a basis for potential test expansion
- Integration with CI pipeline is planned for future versions

## CI/CD Pipeline

The back-end is automatically deployed via Render on each push to the main branch.

- The app is containerised using Docker
- The `.env` file must be configured in the Render dashboard (based on `.env.example`)
- During each deployment :
    - A full build is triggered
    - Unit tests are executed automatically during the build process
    - If any test fails, the build is aborted and the application is not deployed

This ensures that only valid and tested code is pushed to production, reducing the risk of deploying broken features.

## API Documentation

The API is documented using Swagger / OpenAPI.
Swagger UI is available at : http://localhost:8081/swagger-ui.html or http://localhost:8081/v3/api-docs for JSON.

## Notes

- The back-end communicates with a Next.js front-end (see [positivarium-frontend](https://github.com/clairesbaffe/positivarium-front))
- JWT-based auth is used, with role-based access control

## License

This project is licensed under the [MIT License](https://opensource.org/licenses/MIT).

You are free to use, copy, modify, and distribute the code. However, contributions to this repository must follow its guidelines and remain respectful of the original work.