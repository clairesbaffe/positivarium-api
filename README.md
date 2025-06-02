# 🌱 Positivarium API

**Positivarium API** is a Java Spring Boot application containerized with Docker.

It connects to a PostgreSQL database and uses [Cloudinary](https://cloudinary.com/) for media storage.

---

## 🚀 Prerequisites

- **Docker** & **Docker Compose**
- (Optional) Java 17+ and Maven if you want to run without Docker
- A [Cloudinary account](https://cloudinary.com/) to get your API key

---

## ⚙️ Configuration

### 🔐 Create a `.env` file at the root of the project:

You can start from `.env.example` and customize it:

```bash
cp .env.example .env
```

In your `.env` file, define:

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/positivarium
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=password

CLOUDINARY_URL=cloudinary://<api_key>:<api_secret>@<cloud_name>
```

## 🐳 Run the API with Docker

Make sure ports `8081` and `55432` are available on your machine.

Then run:

```bash
docker-compose up --build
```

The API will be available at:

👉 http://localhost:8081

## 🧪 Test your API

You can test your endpoints with Postman, Insomnia or simply with `curl`.

## 🛠️ Development without Docker (optional)

If you prefer to run locally without Docker:

1. Make sure PostgreSQL is running locally on port 5432
2. Create a src/main/resources/application.properties with:
    - Database URL
    - Hibernate settings
    - Flyway config (e.g. classpath:db/migration)
    - CORS rules
3. Run with:
```bash
./mvnw spring-boot:run
```

## 📄 License

This project is for educational purposes (student project).