# Invoice Management System

A Spring Boot application for managing invoices with CRUD operations (excluding deletion).

## Features

- Create new invoices
- Retrieve all invoices
- Retrieve a specific invoice by ID or invoice number
- Update existing invoices

## Technologies Used

- Java 11
- Spring Boot 2.7.5
- Spring Data JPA
- H2 Database
- Maven

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven

### Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Run the application using Maven:

```bash
mvn spring-boot:run
```

The application will start on port 8080.

### H2 Database Console

The H2 database console is enabled and can be accessed at:

```
http://localhost:8080/h2-console
```

Use the following credentials:
- JDBC URL: `jdbc:h2:mem:invoicedb`
- Username: `sa`
- Password: (leave empty)

## API Endpoints

### Get All Invoices

```
GET /api/invoices
```

### Get Invoice by ID

```
GET /api/invoices/{id}
```

### Get Invoice by Invoice Number

```
GET /api/invoices/number/{invoiceNumber}
```

### Create New Invoice

```
POST /api/invoices
```

Request Body:
```json
{
  "invoiceNumber": "INV-004",
  "customerName": "Example Customer",
  "invoiceDate": "2023-01-01",
  "amount": 1000.00,
  "description": "Example invoice",
  "status": "PENDING"
}
```

### Update Invoice

```
PUT /api/invoices/{id}
```

Request Body:
```json
{
  "invoiceNumber": "INV-004",
  "customerName": "Updated Customer",
  "invoiceDate": "2023-01-01",
  "amount": 1500.00,
  "description": "Updated invoice",
  "status": "PAID"
}
```

## Postman Collection

A Postman collection is included in the project for easy testing of the API endpoints. The collection includes all the available endpoints with example request bodies.

### Importing the Collection

1. Download [Postman](https://www.postman.com/downloads/)
2. Open Postman
3. Click on "Import" in the top left corner
4. Select the `Invoice_Management_System.postman_collection.json` file from the project root
5. Also import the `Invoice_Management_System.postman_environment.json` file to set up the environment variables

### Using the Collection

1. After importing, select the "Invoice Management System - Local" environment from the environment dropdown in the top right corner
2. The collection uses the variable `{{base_url}}` which is set to `http://localhost:8080` by default
3. You can change this value in the environment settings if your application is running on a different host or port

## Sample Data

The application is pre-loaded with sample data for testing purposes:

1. Invoice Number: INV-001, Customer: Acme Corporation, Status: PENDING
2. Invoice Number: INV-002, Customer: Globex Inc., Status: PAID
3. Invoice Number: INV-003, Customer: Wayne Enterprises, Status: PENDING

## Error Handling

The application includes comprehensive error handling:

- 404 Not Found: When an invoice with the specified ID or invoice number doesn't exist
- 400 Bad Request: For validation errors or when trying to create an invoice with a duplicate invoice number
- 500 Internal Server Error: For unexpected server errors

## Testing

The application includes comprehensive tests for all layers:

### Unit Tests

- **Controller Tests**: Test the REST endpoints in isolation by mocking the service layer
- **Service Tests**: Test the service layer in isolation by mocking the repository layer
- **Repository Tests**: Test the repository layer using an in-memory H2 database

### Integration Tests

- Test the full API flow from HTTP request to database and back

### Running Tests

To run all tests:

```bash
mvn test
```

To run a specific test class:

```bash
mvn test -Dtest=InvoiceControllerTest
```

To run a specific test method:

```bash
mvn test -Dtest=InvoiceControllerTest#getAllInvoices_ShouldReturnAllInvoices
```
