# Digital Banking API — Endpoint Guide

Base URL (local): `http://localhost:8080/api`

Swagger UI and OpenAPI JSON are the executable/reference source for request and response schemas. Examples below are based on the current controller and DTO mappings and should be validated against a running local instance.

## Authentication

### Register
- **Method/path:** `POST /v1/auth/register`
- **Authentication:** Public
- **Body:** Registration fields as defined by `RegistrationRequest`
- **Success:** `201 Created`

### Login
- **Method/path:** `POST /v1/auth/login`
- **Authentication:** Public
- **Body:**
```json
{
  "username": "demo.customer",
  "password": "ChangeMe123!"
}
```
- **Success response data shape:**
```json
{
  "accessToken": "<JWT>",
  "tokenType": "Bearer",
  "expiration": 900000
}
```
Do not publish or commit real credentials or tokens.

## Accounts

### Create account
- **Method/path:** `POST /v1/accounts`
- **Roles:** CUSTOMER, EMPLOYEE, ADMIN
- **Body:**
```json
{
  "customerId": "10001"
}
```
- **Success:** `201 Created`

### List accounts
- **Method/path:** `GET /v1/accounts`
- **Roles:** EMPLOYEE, ADMIN

### Get account by ID
- **Method/path:** `GET /v1/accounts/{id}`
- **Roles:** CUSTOMER, EMPLOYEE, ADMIN
- **Path parameter:** `id` — account identifier

### Get by account number
- **Method/path:** `GET /v1/accounts/account-number/{accountNumber}`
- **Roles:** CUSTOMER, EMPLOYEE, ADMIN

### Update account
- **Method/path:** `PUT /v1/accounts/{id}`
- **Roles:** EMPLOYEE, ADMIN
- **Body:** `UpdateAccountRequest`

### Deposit
- **Method/path:** `POST /v1/accounts/{id}/deposit`
- **Roles:** CUSTOMER, EMPLOYEE, ADMIN
- **Body:** `DepositRequest`

### Withdraw
- **Method/path:** `POST /v1/accounts/{id}/withdraw`
- **Roles:** CUSTOMER, EMPLOYEE, ADMIN
- **Body:** `WithdrawRequest`

### Freeze / unfreeze
- **Methods/paths:** `POST /v1/accounts/{id}/freeze`, `POST /v1/accounts/{id}/unfreeze`
- **Roles:** EMPLOYEE, ADMIN

## Transfers

### Transfer money
- **Method/path:** `POST /v1/transfers`
- **Roles:** CUSTOMER, EMPLOYEE, ADMIN
- **Required header:** `Idempotency-Key: <unique-key>`
- **Body:**
```json
{
  "sourceAccountId": 1001,
  "destinationAccountId": 2002,
  "amount": 250.00,
  "currency": "INR"
}
```
- **Success response data shape:**
```json
{
  "paymentReference": "PAYMENT-REFERENCE",
  "sourceAccountId": 1001,
  "destinationAccountId": 2002,
  "amount": 250.00,
  "currency": "INR",
  "status": "COMPLETED"
}
```
The payment reference and final status above are illustrative, not a guaranteed runtime value. Verify actual enum values and error responses using the local API.

## Payment history

### Read paginated payment history
- **Method/path:** `GET /v1/payments/history?accountId=1001&page=0&size=20`
- **Roles:** CUSTOMER, EMPLOYEE, ADMIN
- **Query parameters:** `accountId` required; `page` defaults to `0`; `size` defaults to `20`

## Scheduled payments

- `POST /v1/scheduled-payments` — create; JSON body is `CreateScheduledPaymentRequest`
- `GET /v1/scheduled-payments/{id}` — retrieve one scheduled payment
- `DELETE /v1/scheduled-payments/{id}` — cancel
- **Roles:** CUSTOMER, EMPLOYEE, ADMIN

## Transactions

- `GET /v1/transactions/accounts/{accountId}` — transaction history for an account
- `GET /v1/transactions/{id}` — retrieve by internal transaction ID
- `GET /v1/transactions/reference/{transactionReference}` — retrieve by transaction reference
- **Roles:** CUSTOMER, EMPLOYEE, ADMIN

## Audit

- `GET /v1/audits/{resourceType}/{resourceId}` — audit history for a resource
- **Roles:** EMPLOYEE, ADMIN

## Security note

The current method-level annotations show which roles are allowed, but role permission alone does not prove that a customer owns the account identifier in the request. Verify ownership checks in the service layer and cover them with integration tests before treating these endpoints as production-safe.
