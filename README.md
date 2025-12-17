# 🚀 SupplyChainX – Integrated Supply Chain Management System

## 🧩 Overview

**SupplyChainX** is a complete **monolithic system** built with **Spring Boot 3** for managing all operations of a supply chain — from procurement of raw materials to production and final delivery to customers.

It provides a **centralized, automated, and traceable** platform to streamline the entire supply chain process.

---

## 🏗️ Architecture

- **Type:** Monolithic Application (MVC Pattern)
- **Backend:** Spring Boot 3.x
- **Database:** PostgreSQL (or MySQL alternative)
- **ORM:** Hibernate / JPA
- **Build Tool:** Maven
- **Containerization:** Docker & Docker Compose
- **API Documentation:** Swagger / OpenAPI

---

## 🧱 Main Modules

### 1️⃣ Procurement (Approvisionnement)
Manage suppliers, raw materials, and purchase orders.

#### Features:
- Add / Edit / Delete Suppliers (restricted by active orders)
- Manage Raw Materials with critical stock alerts
- Create / Track / Delete Supply Orders
- View supplier and material lists with pagination and filtering

---

### 2️⃣ Production
Handle production planning, product catalog, and production orders.

#### Features:
- Manage finished products (add, edit, delete)
- Link products with their **Bill of Materials (BOM)**
- Create and follow up production orders
- Status tracking: `EN_ATTENTE`, `EN_PRODUCTION`, `TERMINE`, `BLOQUE`
- Check material availability before launching production

---

### 3️⃣ Delivery & Distribution
Manage customers, sales orders, and deliveries.

#### Features:
- Manage customer data (add, edit, delete)
- Create and monitor customer orders
- Create deliveries, assign vehicles & drivers
- Compute total delivery cost
- Track delivery status: `PLANIFIEE`, `EN_COURS`, `LIVREE`

---

## 🧮 Global Business Rules

| Domain | Rule |
|--------|------|
| **Procurement** | A supplier cannot be deleted if active orders exist |
| **Raw Material** | Deletion only if not used in any order |
| **Product** | Deletion only if not linked to a production order |
| **Production Order** | Must verify material stock before start |
| **Delivery** | Only possible if product stock is available |

---


## 👥 User Roles

| Module | Roles |
|--------|--------|
| Procurement | `GESTIONNAIRE_APPROVISIONNEMENT`, `RESPONSABLE_ACHATS`, `SUPERVISEUR_LOGISTIQUE` |
| Production | `CHEF_PRODUCTION`, `PLANIFICATEUR`, `SUPERVISEUR_PRODUCTION` |
| Delivery | `GESTIONNAIRE_COMMERCIAL`, `RESPONSABLE_LOGISTIQUE`, `SUPERVISEUR_LIVRAISONS` |
| Administration | `ADMIN` (full access) |

---

## ⚙️ Technical Stack

| Layer | Technology |
|-------|-------------|
| Framework | Spring Boot 3.x |
| ORM | Hibernate / Spring Data JPA |
| Database | PostgreSQL 16 (Dockerized) |
| Migrations | Liquibase |
| Validation | Bean Validation (`@Valid`) |
| Mapping | DTO + MapStruct |
| Documentation | Swagger UI |
| Tests | JUnit 5, Mockito, TestContainers (optional) |
| Containerization | Docker, Docker Compose |

---

## 📂 Directory Structure
```
Directory structure:
└── mustapha-moutaki-supplychainix/
    ├── README.md
    ├── docker-compose.yml
    ├── Dockerfile
    ├── Jenkinsfile
    ├── mvnw
    ├── mvnw.cmd
    ├── pom.xml
    ├── qodana.yaml
    ├── docker/
    │   └── README.md
    ├── docs/
    │   └── README.md
    ├── src/
    │   ├── main/
    │   │   ├── java/
    │   │   │   └── org/
    │   │   │       └── supplychain/
    │   │   │           └── supplychain/
    │   │   │               ├── SupplychainApplication.java
    │   │   │               ├── config/
    │   │   │               │   ├── DataInitializer.java
    │   │   │               │   ├── JpaAuditingConfig.java
    │   │   │               │   └── SwaggerConfig.java
    │   │   │               ├── constants/
    │   │   │               │   └── supplierConstants/
    │   │   │               │       ├── ApiConstants.java
    │   │   │               │       └── OrderContants.java
    │   │   │               ├── controller/
    │   │   │               │   ├── approvisionnement/
    │   │   │               │   │   ├── OrderController.java
    │   │   │               │   │   ├── RawMaterialController.java
    │   │   │               │   │   ├── SupplierController.java
    │   │   │               │   │   └── SupplierOrderController.java
    │   │   │               │   ├── modelDelivery/
    │   │   │               │   │   └── CustomerController.java
    │   │   │               │   └── Production/
    │   │   │               │       ├── ProductController.java
    │   │   │               │       └── ProductionOrderController.java
    │   │   │               ├── dto/
    │   │   │               │   ├── BOM/
    │   │   │               │   │   └── BillOfMaterialDTO.java
    │   │   │               │   ├── modelDelivery/
    │   │   │               │   │   ├── CustomerDto.java
    │   │   │               │   │   └── DeliveryDto.java
    │   │   │               │   ├── order/
    │   │   │               │   │   ├── OrderDTO.java
    │   │   │               │   │   └── ProductOrderDTO.java
    │   │   │               │   ├── product/
    │   │   │               │   │   └── ProductDTO.java
    │   │   │               │   ├── productionorder/
    │   │   │               │   │   └── ProductionOrderDTO.java
    │   │   │               │   ├── rawmaterial/
    │   │   │               │   │   ├── README.md
    │   │   │               │   │   ├── ErrorResponse.java
    │   │   │               │   │   ├── PageResponseDTO.java
    │   │   │               │   │   ├── RawMaterialRequestDTO.java
    │   │   │               │   │   ├── RawMaterialResponseDTO.java
    │   │   │               │   │   ├── RawMaterialSummaryDTO.java
    │   │   │               │   │   └── SupplierSummaryDTO.java
    │   │   │               │   ├── supplier/
    │   │   │               │   │   └── SupplierDTO.java
    │   │   │               │   └── supplyOrder/
    │   │   │               │       ├── SupplyOrderDTO.java
    │   │   │               │       └── SupplyOrderLineDTO.java
    │   │   │               ├── enums/
    │   │   │               │   ├── README.md
    │   │   │               │   ├── DeliveryStatus.java
    │   │   │               │   ├── OrderStatus.java
    │   │   │               │   ├── Priority.java
    │   │   │               │   ├── ProductionOrderStatus.java
    │   │   │               │   ├── Role.java
    │   │   │               │   └── SupplyOrderStatus.java
    │   │   │               ├── exception/
    │   │   │               │   ├── DuplicateResourceException.java
    │   │   │               │   ├── GlobalExceptionHandler.java
    │   │   │               │   ├── ResourceInUseException.java
    │   │   │               │   └── ResourceNotFoundException.java
    │   │   │               ├── mapper/
    │   │   │               │   ├── README.md
    │   │   │               │   ├── RawMaterialMapper.java
    │   │   │               │   ├── modelDelivery/
    │   │   │               │   │   ├── CustomerMapper.java
    │   │   │               │   │   └── DeliveryMapper.java
    │   │   │               │   ├── modelSupplier/
    │   │   │               │   │   ├── OrderMapper.java
    │   │   │               │   │   ├── SupplierMapper.java
    │   │   │               │   │   ├── SupplierOrderMapper.java
    │   │   │               │   │   └── SupplyOrderLineMapper.java
    │   │   │               │   └── Production/
    │   │   │               │       ├── BillOfMaterialMapper.java
    │   │   │               │       ├── ProductionOrderMapper.java
    │   │   │               │       ├── ProductMapper.java
    │   │   │               │       └── ProductOrderMapper.java
    │   │   │               ├── model/
    │   │   │               │   ├── BaseEntity.java
    │   │   │               │   ├── BillOfMaterial.java
    │   │   │               │   ├── Customer.java
    │   │   │               │   ├── Delivery.java
    │   │   │               │   ├── Order.java
    │   │   │               │   ├── Product.java
    │   │   │               │   ├── ProductionOrder.java
    │   │   │               │   ├── ProductOrder.java
    │   │   │               │   ├── RawMaterial.java
    │   │   │               │   ├── Supplier.java
    │   │   │               │   ├── SupplyOrder.java
    │   │   │               │   ├── SupplyOrderLine.java
    │   │   │               │   └── User.java
    │   │   │               ├── repository/
    │   │   │               │   ├── approvisionnement/
    │   │   │               │   │   ├── OrderRepository.java
    │   │   │               │   │   ├── RawMaterialRepository.java
    │   │   │               │   │   ├── SupplierOrderRepository.java
    │   │   │               │   │   ├── SupplierRepository.java
    │   │   │               │   │   └── SupplyOrderLineRepository.java
    │   │   │               │   ├── modelDelivery/
    │   │   │               │   │   ├── CustomerRepository.java
    │   │   │               │   │   └── DeliveryRepository.java
    │   │   │               │   └── Production/
    │   │   │               │       ├── BillOfMaterialRepository.java
    │   │   │               │       ├── ProductionOrderRepository.java
    │   │   │               │       └── ProductRepository.java
    │   │   │               ├── response/
    │   │   │               │   ├── ErrorResponse.java
    │   │   │               │   └── SuccessResponse.java
    │   │   │               └── service/
    │   │   │                   ├── approvisionnement/
    │   │   │                   │   ├── RawMaterialService.java
    │   │   │                   │   └── RawMaterialServiceImpl.java
    │   │   │                   ├── modelDelivery/
    │   │   │                   │   ├── impl/
    │   │   │                   │   │   ├── CustomerServiceImpl.java
    │   │   │                   │   │   └── DeliveryServiceImpl.java
    │   │   │                   │   └── interfaces/
    │   │   │                   │       ├── ICustomerService.java
    │   │   │                   │       └── IDeliveryService.java
    │   │   │                   ├── modelSupplier/
    │   │   │                   │   ├── OrderServiec.java
    │   │   │                   │   ├── SupplierOrderService.java
    │   │   │                   │   ├── SupplierService.java
    │   │   │                   │   ├── SupplyOrderLineService.java
    │   │   │                   │   └── impl/
    │   │   │                   │       ├── OrderServiceImpl.java
    │   │   │                   │       ├── SupplierServiceImpl.java
    │   │   │                   │       └── SupplyOrderServiceImpl.java
    │   │   │                   └── Production/
    │   │   │                       ├── Product/
    │   │   │                       │   ├── ProductService.java
    │   │   │                       │   └── ProductServiceImpl.java
    │   │   │                       └── ProductionOrder/
    │   │   │                           ├── ProductionOrderService.java
    │   │   │                           └── ProductionOrderServiceImpl.java
    │   │   └── resources/
    │   │       ├── application.yml
    │   │       └── db/
    │   │           └── changelog/
    │   │               ├── README.md
    │   │               ├── db.changelog-master.yaml
    │   │               └── changes/
    │   │                   └── README.md
    │   └── test/
    │       ├── java/
    │       │   └── org/
    │       │       └── supplychain/
    │       │           └── supplychain/
    │       │               ├── SupplierRepositoryTest.java
    │       │               ├── SupplychainApplicationTests.java
    │       │               ├── controller/
    │       │               │   └── approvisionnement/
    │       │               │       └── SupplierControllerTest.java
    │       │               ├── integration/
    │       │               │   ├── OrderIntegrationTest.java
    │       │               │   └── SupplierIntegrationTest.java
    │       │               └── service/
    │       │                   └── approvisionnement/
    │       │                       └── impl/
    │       │                           └── SupplierServiceImplTest.java
    │       └── resources/
    │           └── application-test.yml
    ├── .github/
    │   └── workflows/
    │       ├── ci.yml
    │       └── qodana_code_quality.yml
    └── .mvn/
        └── wrapper/
            └── maven-wrapper.properties


```


---

## 🐳 Docker Setup

### Start the application

```bash
  docker-compose up --build -d
```
Access:

Backend API: http://localhost:8080

pgAdmin: http://localhost:5050

Swagger UI: http://localhost:8080/swagger-ui.html

## [  Fast SupplyChain API Documentation ]

## Raw Materials API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/raw-materials/{id}` | Get a raw material by ID |
| PUT | `/api/raw-materials/{id}` | Update a raw material by ID |
| DELETE | `/api/raw-materials/{id}` | Delete a raw material by ID |
| GET | `/api/raw-materials` | Get all raw materials |
| POST | `/api/raw-materials` | Create a new raw material |
| GET | `/api/raw-materials/{id}/is-used` | Check if a raw material is used in any product |
| GET | `/api/raw-materials/search` | Search raw materials by name |
| GET | `/api/raw-materials/critical-stock` | Get raw materials with critical stock |
| GET | `/api/raw-materials/critical-stock/paginated` | Get raw materials with critical stock (paginated) |

---

## Orders API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/orders/{id}` | Get an order by ID |
| PUT | `/api/orders/{id}` | Update an order by ID |
| DELETE | `/api/orders/{id}` | Delete an order by ID |
| GET | `/api/orders` | Get all orders |
| POST | `/api/orders` | Create a new order |

---

## Suppliers API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/suppliers/{id}` | Get a supplier by ID |
| PUT | `/api/suppliers/{id}` | Update an existing supplier by ID |
| DELETE | `/api/suppliers/{id}` | Delete a supplier by ID |
| GET | `/api/suppliers` | Get all suppliers with pagination |
| POST | `/api/suppliers` | Create a new supplier |
| GET | `/api/suppliers/search` | Search suppliers by name |

---

## Supplier Orders API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/supplier-orders/{id}` | Get a supplier order by ID |
| PUT | `/api/supplier-orders/{id}` | Update a supplier order |
| DELETE | `/api/supplier-orders/{id}` | Delete a supplier order |
| GET | `/api/supplier-orders` | Get all supplier orders |
| POST | `/api/supplier-orders` | Create a new supplier order |

---

## Products API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/products/{id}` | Get a product by ID |
| PUT | `/api/products/{id}` | Update a product by ID |
| DELETE | `/api/products/{id}` | Delete a product by ID |
| GET | `/api/products` | Get all products with pagination and sorting |
| POST | `/api/products` | Create a new product |

---

## Production Orders API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/production-orders/{id}` | Get a production order by ID |
| PUT | `/api/production-orders/{id}` | Update a production order by ID |
| DELETE | `/api/production-orders/{id}` | Cancel a production order by ID |
| PUT | `/api/production-orders/production/{id}` | Start production for a given production order by ID |
| GET | `/api/production-orders` | Get all production orders with pagination and sorting |
| POST | `/api/production-orders` | Create a new production order |
| GET | `/api/production-orders/status/{status}` | Get production orders filtered by status with pagination |

---

## Deliveries API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/deliveries/{id}` | Retrieve a delivery by ID |
| PUT | `/api/deliveries/{id}` | Update a delivery by ID |
| DELETE | `/api/deliveries/{id}` | Delete a delivery by ID |
| GET | `/api/deliveries` | Retrieve all deliveries |
| POST | `/api/deliveries` | Create a new delivery |

---

## Customers API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/customers/{id}` | Retrieve a customer by ID |
| PUT | `/api/customers/{id}` | Update a customer by ID |
| DELETE | `/api/customers/{id}` | Delete a customer by ID |
| GET | `/api/customers` | Retrieve all customers with pagination and optional filter |
| POST | `/api/customers` | Create a new customer |

## 🧾 Example User Stories

Procurement

US3: Add a supplier

US12: View materials with stock below critical level

US17: Track status of supply orders

Production

US18: Add finished product

US23: Create a production order

US28: Verify material availability before starting

## Delivery

US30: Add a client

US35: Create a customer order

US40: Create a delivery and calculate total cost

## 🌍 Future Enhancements

Email notifications for critical stock (SMTP)

Integration with external ERP systems

Dashboard with real-time KPIs (using React + Chart.js)

AI-based demand forecasting (bonus)

# UML Diagrams:

## Class diagram
![Architecture Diagram](/UML/supply-chaine-Class-Diagram.webp)


## Use-Case diagram
![Architecture Diagram](/UML/supply-chaine-UseCase.webp)

# The Jenkinsfile 
```

pipeline {
    agent any

    tools {
        jdk 'jdk17'
        maven 'Maven 3.9.0'
    }

    environment {
        IMAGE_NAME = "supplychainx-app"
        CONTAINER_NAME = "supplychainx-container"
        SPRING_PROFILES_ACTIVE = "test"
    }

    stages {
        stage('Checkout') {
            steps {
                echo '-> Checking out the code...'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo '-> Building the application...'

                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                echo '-> Running unit & integration tests...'
                sh 'mvn test'
            }
        }

        stage('Docker Build') {
            steps {
                echo ' Building Docker image...'
                sh "docker build -t $IMAGE_NAME ."
            }
        }

        stage('Docker Run') {
            steps {
                echo ' Running Docker container...'
                sh """
                    docker stop $CONTAINER_NAME || true
                    docker rm $CONTAINER_NAME || true
                    docker run -d --name $CONTAINER_NAME -p 8080:8080 $IMAGE_NAME
                """
            }
        }

        stage('Clean') {
            steps {
                echo '-> Cleaning old Docker containers/images (optional)...'

            }
        }
    }

    post {
        success {
            echo '[success] Pipeline finished successfully!'
        }
        failure {
            echo '[failed] Pipeline failed. Check logs!'
        }
    }
}


```

# The GithubActions Pipline
```
name: CI Pipeline - Maven & Docker

on:
  push:
    branches: [ "ImplTests" ]
  pull_request:
    branches: [ "ImplTests" ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      # 1️⃣ Checkout the repository
      - name: Checkout code
        uses: actions/checkout@v4
        with:
         fetch-depth: 0
        # 2️⃣ Set up JDK 17
      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: maven

      # 3️⃣ Build the project with Maven
      - name: Build with Maven
        run: mvn -B clean package --file pom.xml

      # 4️⃣ Run tests with Maven and fail workflow if tests fail
      - name: Run Maven tests
        run: mvn test

      # 5️⃣ Build Docker image locally
      - name: Build Docker image
        run: docker build -t supplychainx:latest .

      # 6️⃣ Run Docker container locally
      - name: Run Docker container
        run: |
          # Stop old container if exists
          docker stop supplychainx-container || true
          docker rm supplychainx-container || true
          # Run container in detached mode
          docker run -d --name supplychainx-container -p 8080:8080 supplychainx:latest

      # 7️⃣ Optional: Clean up Docker container after workflow finishes
      - name: Clean up Docker container
        if: always()
        run: |
          docker stop supplychainx-container || true
          docker rm supplychainx-container || true

```







## jenkins pipline
![CICD](/cicd/jenkinsPipline.png)
## github actions pipline
![CICD](/cicd/githubActionPipline.png)
## qodana jetbrains pipline
![CICD](/cicd/githubActionJetbrainPipline.png)
## github webhook
![CICD](/cicd/githubWebhook.png)
## jenkins console
![CICD](/cicd/console.png)
![CICD](/cicd/Screenshot%20from%202025-11-13%2019-57-53.png)
## SonarQube -code Quality
![CICD](/cicd/sonarQube.png)
