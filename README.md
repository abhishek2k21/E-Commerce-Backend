# E-Commerce Backend API

A comprehensive REST API powering a full-featured e-commerce platform built with Java, Spring Boot, and MySQL.

## 🌐 Live Documentation

View the complete API documentation at: **[https://abhishek2k21.github.io/E-Commerce-Backend/](https://abhishek2k21.github.io/E-Commerce-Backend/)**

## ⚡ Quick Start

### Prerequisites
- Java 8 or higher
- Maven 3.6+
- MySQL 5.7+

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/abhishek2k21/E-Commerce-Backend.git
   cd E-Commerce-Backend
   ```

2. **Configure Database**
   Update `application.properties` with your MySQL credentials:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/ecommercedb
   spring.datasource.username=root
   spring.datasource.password=root
   ```

3. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```

The API will be available at `http://localhost:8080`  
Swagger UI documentation: `http://localhost:8080/swagger-ui/index.html`

## 🏗️ Architecture

The backend consists of 6 core modules:

- **🔐 Auth Module** — Session-based authentication (1-hour token expiry)
- **👤 Customer Module** — User profile, address, and account management
- **🏪 Seller Module** — Seller admin and product catalog control
- **📦 Product Module** — Full CRUD operations for product catalog
- **🛒 Cart Module** — Persistent shopping cart management
- **📋 Order Module** — Complete order lifecycle management

## 📡 API Endpoints

### 30+ RESTful Endpoints

**Login & Logout (6 endpoints)**
- `POST /register/customer` — Register new customer
- `POST /login/customer` — Customer login
- `POST /logout/customer` — Customer logout
- `POST /register/seller` — Register new seller
- `POST /login/seller` — Seller login
- `POST /logout/seller` — Seller logout

**Customer (10 endpoints)**
- `GET /customer/current` — Current logged-in customer
- `GET /customer/orders` — Customer order history
- `PUT /customer` — Update profile
- `DELETE /customer` — Delete account
- ...and more

**Seller (8 endpoints)**  
**Product (8 endpoints)**  
**Cart (4 endpoints)**  
**Order (6 endpoints)**

See the [Live Documentation](https://abhishek2k21.github.io/E-Commerce-Backend/) for complete endpoint reference.

## 🔒 Security Features

✅ **Session-Based Authentication** — 1-hour token expiry  
✅ **Role-Based Access Control** — Customer vs Seller separation  
✅ **Input Validation** — Email, mobile, password strength checks  
✅ **Secure Password Hashing** — Industry-standard encryption  
✅ **SQL Injection Protection** — JPA/Hibernate parameterized queries  

## 🛠️ Tech Stack

- **Language:** Java 8
- **Framework:** Spring Boot
- **ORM:** Hibernate / Spring Data JPA
- **Database:** MySQL 5.7+
- **API Documentation:** Swagger / OpenAPI
- **Testing:** Postman
- **Build:** Maven
- **Utilities:** Lombok

## 📊 Project Stats

- **6** Core Modules
- **30+** REST Endpoints
- **28** Git Commits
- **100%** CRUD Coverage

## 📁 Project Structure

```
E-Commerce-Backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/yourcompany/ecommerce/
│   │   │       ├── model/
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── repository/
│   │   │       └── config/
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml
└── README.md
```

## 📝 Sample Request/Response

**POST /login/customer**

Request:
```json
{
  "mobileId": "9999999999",
  "password": "abhi8080"
}
```

Response (200 OK):
```json
{
  "sessionId": 23,
  "token": "customer_0ad57094",
  "userId": 19,
  "userType": "customer",
  "sessionStartTime": "2022-06-10T10:48:20",
  "sessionEndTime": "2022-06-10T11:48:20"
}
```

## 🔗 Links

- 📖 [API Documentation](https://abhishek2k21.github.io/E-Commerce-Backend/)
- 🐙 [GitHub Repository](https://github.com/abhishek2k21/E-Commerce-Backend)
- 👤 [Author Profile](https://github.com/abhishek2k21)
- 🔍 [Other Projects](https://github.com/abhishek2k21)

## 📄 License

This project is open source and available under the MIT License.

## 👨‍💻 Author

**Abhishek Kumar**  
GitHub: [@abhishek2k21](https://github.com/abhishek2k21)

---

**Built with ☕ Java & Spring Boot**  
*A comprehensive REST API for modern e-commerce platforms*
