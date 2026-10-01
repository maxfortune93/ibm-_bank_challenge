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
