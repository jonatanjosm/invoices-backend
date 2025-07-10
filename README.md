# Invoice Management System

A Spring Boot application for managing invoices with CRUD operations (excluding deletion).

## Features

- Create new invoices
- Retrieve all invoices
- Retrieve a specific invoice by invoice number
- Update existing invoices
- Pay invoices

## Technologies Used

- Java 11
- Spring Boot 2.7.5
- Spring Data JPA
- H2 Database
- Maven

## Getting Started

### Prerequisites

Choose one of the following options:

#### Option 1: Local Development
- Java 11 or higher
- Maven

#### Option 2: Docker
- Docker
- Docker Compose

### Running the Application

#### Option 1: Local Development

1. Clone the repository
2. Navigate to the project directory
3. Run the application using Maven:

```bash
mvn spring-boot:run
```

#### Option 2: Docker

1. Clone the repository
2. Navigate to the project directory
3. Build and run the application using Docker Compose:

```bash
docker-compose up -d
```

To stop the application:

```bash
docker-compose down
```

The application will start on port 8080 in both options.

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
PUT /api/invoices
```

Request Body:
```json
{
  "invoiceNumber": "INV-004",
  "lineItems": [
    {
      "description": "New Item",
      "price": 75.00,
      "quantity": 2
    }
  ]
}
```

### Pay Invoice

```
POST /api/invoices/pay
```

Request Body:
```json
{
  "invoiceNumber": "INV-001",
  "paymentDate": "2023-01-15",
  "amount": 100.00,
  "paymentMethod": "Credit Card"
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

## Error Handling

The application includes comprehensive error handling:

- 404 Not Found: When an invoice with the specified ID or invoice number doesn't exist
- 400 Bad Request: For validation errors or when trying to create an invoice with a duplicate invoice number
- 500 Internal Server Error: For unexpected server errors

## Docker Configuration

The application can be run in a Docker container using the provided Dockerfile and docker-compose.yml files.

### Dockerfile

The Dockerfile uses a multi-stage build approach:
1. First stage: Uses eclipse-temurin:11-jdk to build the application
2. Second stage: Uses eclipse-temurin:11-jre (smaller image) to run the application

### Docker Compose

The docker-compose.yml file configures:
- Port mapping: Maps host port 8080 to container port 8080
- Environment variables: Sets the Spring profile to 'default'
- Restart policy: Ensures the container restarts if it crashes
- Health check: Monitors the application health by checking the /api/invoices endpoint

### Building and Running with Docker

To build and run the application with Docker:

```bash
# Build the Docker image
docker build -t invoices-backend .

# Run the container
docker run -p 8080:8080 invoices-backend
```

Or using Docker Compose:

```bash
# Build and run with Docker Compose
docker-compose up -d

# View logs
docker-compose logs -f

# Stop the application
docker-compose down
```

## Database Migrations

The application uses Flyway for database schema migrations:

- Migration scripts are located in `src/main/resources/db/migration`
- The initial schema is defined in `V1__Initial_schema.sql`
- To add new database changes, create a new migration script following the naming convention `V{number}__{description}.sql`

## Testing

The application includes comprehensive tests for all layers:

### Unit Tests

- **Controller Tests**: Test the REST endpoints in isolation by mocking the service layer
  - Each endpoint has tests for both success and error scenarios
  - Success tests verify correct response status and data
  - Error tests verify appropriate error handling and status codes

### Integration Tests

- Test the full API flow from HTTP request to database and back
- Covers the entire invoice lifecycle:
  1. Creating an invoice with line items
  2. Retrieving the invoice by invoice number
  3. Updating the invoice with new line items
  4. Paying the invoice

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
mvn test -Dtest=InvoiceControllerTest#testGetAllInvoices_Success
```
