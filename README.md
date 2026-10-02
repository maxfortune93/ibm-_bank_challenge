# IBM Challenge

The challenge involves developing a web application that emulates the process of conducting financial transactions within a banking system.

## Application Requirements

1. **Customer Registration**
   - Fields: name, age, email address, and account number.

2. **Debit and Credit Registration**
   - Records of debits and credits in the customer's account.

3. **Account Statement**
   - Display of the customer's account statement with the total balance (at the bottom or top of the page).

## Technologies Used

- **FrontEnd**: Angular
- **BackEnd**: Java (Spring Boot)
- **Database**: MySQL, SQL Server, MongoDB, or PostgreSQL, or text file (JSON)

## Architecture

```
front-ibm-bank/   Angular 17 + Material (customers, transactions, statement)
back-ibm-bank/    Spring Boot 3 / Java 17 REST API (controllers -> services -> JPA repositories)
```

Business rules worth knowing:

- Balance changes run in a single DB transaction and lock the customer rows (`SELECT ... FOR UPDATE`), so concurrent withdrawals/transfers cannot overdraw an account. Transfers lock both accounts in a fixed order to avoid deadlocks.
- Requests are validated (Bean Validation); errors are returned as `{ "message": "...", "errors": [...] }` with the proper HTTP status.
- Schema: Hibernate `update` in `dev` (H2 in memory) and `dev-local` (Postgres); Flyway + `ddl-auto=validate` in `prod`.
- Demo data: `V2__seed_demo_data.sql` (6 customers, 12 transactions, balances consistent with the statement) is applied by Flyway in `prod`. It is idempotent and skips rows that already exist. To get the same data locally, run the API with `--spring.flyway.enabled=true --spring.jpa.hibernate.ddl-auto=validate`, or delete the `V2` file to start empty.

### Main endpoints

| Method | Path | Description |
| ------ | ---- | ----------- |
| GET | `/api/customers?page&size&searchTerm` | Paginated customer list |
| POST | `/api/customers` | Register a customer |
| GET | `/api/customers/{id}` | Customer with balance |
| GET | `/api/customers/autocomplete?query&limit` | Name autocomplete |
| POST | `/api/transactions` | `DEPOSIT`, `WITHDRAWAL` or `TRANSFER` |
| GET | `/api/transactions/{customerId}?page&size&sort&month&year` | Account statement |
| GET | `/actuator/health` | Health check |

Swagger UI: `/swagger-ui.html`.

## Steps to Run the Project

Fastest way (Postgres + API in Docker, then the Angular dev server):

```bash
docker compose up --build
cd front-ibm-bank && npm ci && npm start   # http://localhost:4200, proxies /api to :8080
```

Or run each part on its own (details in each folder's README):

```bash
cd back-ibm-bank && ./mvnw spring-boot:run   # H2 in memory, http://localhost:8080
cd front-ibm-bank && npm ci && npm start
```

Environment variables for the `prod` profile are listed in `back-ibm-bank/.env.example`.

## Tests

```bash
cd back-ibm-bank && ./mvnw verify
cd front-ibm-bank && npx ng test --watch=false
```

CI (`.github/workflows/ci.yml`) runs both suites and the production build on every push and PR.

## Deploy (Render)

`.github/workflows/deploy.yml` runs after the **CI** workflow succeeds on `main` (or manually via *Run workflow*). It syncs the environment variables to the Render service, triggers a deploy, waits until it is `live` and optionally calls the health endpoint.

Configure in **GitHub → Settings → Secrets and variables → Actions**:

| Type | Name | Description |
| ---- | ---- | ----------- |
| Secret | `RENDER_API_KEY` | Render API key (Account Settings → API Keys) |
| Secret | `RENDER_SERVICE_ID` | Service id (`srv-...`, shown in the service URL/settings) |
| Secret | `DATABASE_URL` | JDBC url, e.g. `jdbc:postgresql://host:5432/db` |
| Secret | `DATABASE_USERNAME` | Database user |
| Secret | `DATABASE_PASSWORD` | Database password |
| Variable | `CORS_ALLOWED_ORIGINS` | Optional. Comma separated front-end origins |
| Variable | `RENDER_HEALTH_URL` | Optional. e.g. `https://<service>.onrender.com/actuator/health` |

On Render, turn **Auto-Deploy off** for the service so the workflow is the only thing that deploys. The workflow overwrites *all* env vars of the service with the list above (`SPRING_PROFILES_ACTIVE=prod` is set by the Dockerfile), so add any new variable to `deploy.yml` too.
