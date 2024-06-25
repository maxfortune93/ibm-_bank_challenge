# IBM Bank Challenge

## Description

This project is a sample application for managing banking transactions. It was developed as part of the IBM Bank Challenge and uses Spring Boot for the backend layer and H2 as an in-memory database to facilitate development and testing.


## Technologies Used

- Java 17
- Spring Boot
- *Spring Data JPA*
- *H2 Database* (for development)
- *PostgreSQL* (for dev-local environment)


## Features

- *Customer Management:* CRUD operations for bank customers.
- *Transactions:* Management of bank transactions between accounts, including deposit, withdrawal, and transfer.
- *Filters:* Filtering transactions by month and year.


## Prerequisites

Before you begin, ensure you have the following tools installed on your machine:

- [Java 17+](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
- [Maven](https://maven.apache.org/)
- [Git](https://git-scm.com/)


## How to Run the Project

### Step 1: Clone the Repository

```bash
   git clone https://github.com/maxfortune93/ibm-_bank_challenge.git

   cd back-ibm-bank
```

### Step 2: Configure the Database

- FOR H2 Database: No additional configuration is needed as H2 is in-memory and auto-configured.

- For PostgreSQL: Ensure that PostgreSQL is installed and configured properly. Update the application-dev-local.properties with your PostgreSQL settings.
### Step 3: Run the Application

You can run the application using Maven. Specify the profile with the -Dspring.profiles.active flag:

For development using H2:

```bash
mvn spring-boot:run -Dspring.profiles.active=dev
```
For local development using PostgreSQL:

```bash
mvn spring-boot:run -Dspring.profiles.active=dev-local
```

Or you can build the project and run the generated JAR file:

```bash
mvn clean package
java -jar -Dspring.profiles.active=dev target/your-project.jar
java -jar -Dspring.profiles.active=dev-local target/your-project.jar
```

### Step 4: Access the Application

After starting the application, you can access the H2 console to verify the data:

- *URL:* http://localhost:8080/h2-console
- *JDBC URL:* jdbc:h2:mem:testdb
- *User:* sa
- *Password:* password


## API Endpoints

### Customers

- *GET /api/customers*: List all customers.
- *POST /api/customers*: Create a new customer.
- *GET /api/customers/{id}*: Get a customer by ID.


### Transactions

- *POST /api/transactions*: Create a new transaction.
- *GET /api/transactions/{customerId}*: List transactions by customer ID with optional month and year filters.

## Contribution

Contributions are welcome! If you have any suggestions, improvements, or feedback about the project, feel free to share them. Your input is valuable and will help enhance the project.



