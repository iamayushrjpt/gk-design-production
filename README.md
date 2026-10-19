# gk-design-backend-test

Spring Boot API for the isolated GK Design test frontend. Configuration, including MongoDB, JWT, admin seeding, CORS and Cloudflare R2 settings, is in `src/main/resources/application.yml`.

Start MongoDB and backend from this folder:

```powershell
docker compose up -d --build
```

API: `http://localhost:8081`

Logs:

```powershell
docker compose logs -f backend
```

The backend uses a separate MongoDB container and database (`gkdesign_test`) and does not share the old application's MongoDB volume. Fill in the R2 settings in `application.yml` before testing file uploads. See the parent `README.md` for frontend setup and default local test login.
