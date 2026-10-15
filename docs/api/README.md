# Digital Banking API Documentation

This directory contains the human-written engineering documentation for the Digital Banking API. The live OpenAPI contract is generated from Spring MVC controllers and DTOs by Springdoc.

## Documentation entry points

- **Swagger UI (local):** http://localhost:8080/api/swagger-ui/index.html
- **OpenAPI JSON (local):** http://localhost:8080/api/v3/api-docs
- **OpenAPI YAML (local):** http://localhost:8080/api/v3/api-docs.yaml

These URLs include `/api` because `server.servlet.context-path=/api` is configured in `application.yaml`.

## API groups

| Area | Base path | What it does |
|---|---|---|
| Authentication | `/v1/auth` | Register and log in; login returns a JWT access token |
| Accounts | `/v1/accounts` | Create, fetch, update, deposit, withdraw, freeze, and unfreeze accounts |
| Transfers | `/v1/transfers` | Initiate a transfer with an idempotency key |
| Payment history | `/v1/payments/history` | Read paginated payment history |
| Scheduled payments | `/v1/scheduled-payments` | Create, retrieve, and cancel scheduled payments |
| Transactions | `/v1/transactions` | Query transaction history and individual transactions |
| Audit | `/v1/audits` | Read audit history (employee/admin) |

See the controller classes for the authoritative mappings and role restrictions. Swagger is generated from those mappings; do not manually maintain a duplicate endpoint list as the source of truth.

## Authentication flow

1. Call `POST /api/v1/auth/register` to create a user.
2. Call `POST /api/v1/auth/login` with the registered credentials.
3. Copy `data.accessToken` from the successful response.
4. In Swagger UI, click **Authorize** and enter the token as `Bearer <accessToken>` (Swagger's HTTP bearer configuration may accept just the token; follow the UI prompt).
5. Call protected APIs using the same token while respecting role requirements.

Never commit real tokens, passwords, secrets, or customer information in this repository.

## Transfer safety

- `POST /api/v1/transfers` requires an `Idempotency-Key` header.
- Use a unique key for each logical transfer. Reuse the same key only when retrying the same logical request.
- Do not retry a transfer with a new key merely because the response was lost; that could represent a new operation.
- Document and test insufficient-funds, frozen-account, fraud rejection, duplicate idempotency key, validation, and authentication cases.

## Response and error conventions

Successful controller operations use the project's `ApiResponse<T>` wrapper. The exact `data` schema varies by endpoint. Document errors returned by the current exception handlers; do not assume every error uses one schema until it has been verified by tests.

Each endpoint should specify:
- Purpose and business behavior
- Auth requirement and allowed roles
- Required headers, path parameters, query parameters, and body
- Success status and example response
- Validation and business error responses
- Idempotency/pagination semantics when applicable

## Verify documentation locally

From the repository root:

```bash
./mvnw test
./mvnw spring-boot:run
```

On Windows, use `mvnw.cmd test` and `mvnw.cmd spring-boot:run`.

Open the Swagger UI and confirm that all expected controller mappings appear. Try an unauthenticated protected request (expect 401), then authorize with a real development token and verify role-based authorization. Use only local/dev data.
