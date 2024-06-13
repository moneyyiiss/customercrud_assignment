
# Sunbase customercrud Assignment

This project is a Customer CRUD (Create, Read, Update, Delete) application designed to manage customer data efficiently. It provides a user-friendly interface for performing various operations on customer data and includes features such as searching and synchronization with a remote API. The application is built using Spring Boot for the backend and HTML, CSS, and JavaScript for the frontend. It employs JWT for secure user authentication and MySQL as the database.

## Features

- **Add New Customer**: Add customer details including name, address, city, state, email, and phone.
- **Update Customer**: Modify existing customer information.
- **Delete Customer**: Remove customer entries from the system.
- **Search Customers**: Search for customers by first name, city, email, or phone.
- **Sync Customers**: Fetch and update customer data from a remote API.
- **Pagination**: Navigate through customer data with pagination support.

## Technologies Used

### Backend
- **Java Spring Boot**: Provides the RESTful APIs and handles business logic.
- **Spring Security**: Manages authentication and authorization.
- **Hibernate (JPA)**: ORM tool for database interaction.
- **JWT (JSON Web Tokens)**: Used for secure authentication.
- **PostgreSQL**: The database management system.

### Frontend
- **HTML**: Markup language for creating web pages.
- **CSS**: Stylesheet language for designing the look and feel of the application.
- **JavaScript**: Programming language for implementing dynamic behavior on the web page.

### Third-Party APIs
- **Sunbase Data API**: Used to fetch and sync customer data.

## Getting Started

### Prerequisites
- **Java 11** or higher
- **Maven**
- **MySql**

### Installation

#### Backend Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/moneyyiiss/customercrud_assignment.git
   cd customercrud_assignment

2. **Configure MySql:**
   - Ensure MySql is installed and running.
   - Create a database named `customer_db`.
   - Update the `application.properties` file with your MySql configuration.
     ```properties
     spring.datasource.url=jdbc:jdbc:mysql://localhost:3306/customer_db
     spring.datasource.username=your-username
     spring.datasource.password=your-password

3. **Build and run the Spring Boot application:**
   ```bash
   mvn clean install
   mvn spring-boot:run

#### Frontend Setup

1. **Access the application:**
   Open your browser and navigate to `http://localhost:8080/customers.html` to access the customer management system.

## API Endpoints

### Authentication

- **POST `/api/signup`**: Registers a new user.
  ```json
  {
    "username": "newuser",
    "password": "password"
  }

- **POST `/api/authenticate`**: Authenticates the user and returns a JWT token.
  ```json
  {
    "username": "testuser",
    "password": "password"
  }

### Customers
- **GET `/api/customers`**: Retrieves a list of customers with optional pagination and search parameters.
  - Query Parameters:
    - `page`: Page number (default is 0)
    - `size`: Number of records per page (default is 10)
    - `sortBy`: Sort by field (default is id)
    - `searchBy`: Search by field (e.g., firstName, city, email, phone)
    - `searchValue`: Value to search for

- **POST `/api/customers`**: Creates a new customer.
  ```json
  {
    "firstName": "John",
    "lastName": "Doe",
    "street": "123 Main St",
    "address": "Apt 4",
    "city": "New York",
    "state": "NY",
    "email": "john.doe@example.com",
    "phone": "1234567890"
  }

- **PUT `/api/customers/{id}`**: Updates an existing customer by ID.
  - Request Body: Same as create customer

- **DELETE `/api/customers/{id}`**: Deletes a customer by ID.

- **POST `/api/customers/sync`**: Syncs customers from a remote API.
  ```json
  {
    "username": "test@sunbasedata.com",
    "password": "Test@123"
  }


## Configuration and Setup

### MySql Setup
1. **Install MySql**:
   Follow the instructions for your operating system to install MySql.

2. **Create a Database**:
   - Connect to the MySql database server.
   - Create a new database named `customer_db`:
     ```sql
     CREATE DATABASE customer_db;

3. **Update Application Properties**:
   - In the `application.properties` file, update the database connection details as shown above.

## How to Run the Project Locally

1. **Clone the repository:**
   ```bash
   git clone https://github.com/moneyyiiss/customercrud_assignment.git
   cd customercrud_assignment

2. **Backend Setup**:
   - Configure MySql as described above.
   - Build and run the backend:
     ```bash
     mvn clean install
     mvn spring-boot:run

3. **Access the application**:
   - Open your browser and navigate to `http://localhost:8080/customers.html`.


## Screenshots

### Register Screen
![image](https://github.com/moneyyiiss/customercrud_assignment/assets/48843148/4949749a-7037-4c23-9fc9-7321b62e594b)

### Login Screen
![image](https://github.com/moneyyiiss/customercrud_assignment/assets/48843148/375e2f51-81b3-4081-ac5b-182f338e2d8e)

### Customer List
![image](https://github.com/moneyyiiss/customercrud_assignment/assets/48843148/06d76413-85d1-4635-8259-6aa0f293900b)

### Add a New Customer
![image](https://github.com/moneyyiiss/customercrud_assignment/assets/48843148/b13161e8-a3ef-4e1f-831b-f069e576f357)

### Sorting and Searching
![Screenshot (26)](https://github.com/moneyyiiss/customercrud_assignment/assets/48843148/e887c593-ea4e-413b-93a2-c96b781a6796)

### Sync
![Screenshot (27)](https://github.com/moneyyiiss/customercrud_assignment/assets/48843148/eaaac614-dda6-4038-b6f3-638dc5c7da73)

![Screenshot (28)](https://github.com/moneyyiiss/customercrud_assignment/assets/48843148/87b62878-2f2c-4295-b722-b840094a44fa)






## Contributing

Contributions are welcome! Please create a pull request with your changes.

## Contact

Author: Manish Dinkar
Email: manish.dinkar13@gmail.com
GitHub: moneyyiiss
