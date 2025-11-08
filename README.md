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
├── docker-compose.yml
├── Dockerfile
├── mvnw.cmd
├── pom.xml
├── docker/
│   └── README.md
├── docs/
│   └── README.md
├── src/
│   └── main/
│       └── java/
│           └── org/
│               └── supplychain/
│                   └── supplychain/
│                       ├── config/
│                       │   ├── DataInitializer.java
│                       │   ├── JpaAuditingConfig.java
│                       │   └── SwaggerConfig.java
│                       ├── controller/
│                       │   ├── approvisionnement/
│                       │   │   ├── OrderController.java
│                       │   │   ├── RawMaterialController.java
│                       │   │   └── SupplierController.java
│                       │   ├── modelDelivery/
│                       │   │   └── CustomerController.java
│                       │   └── Production/
│                       │       ├── ProductController.java
│                       │       └── ProductionOrderController.java
│                       ├── dto/
│                       │   ├── README.md
│                       │   ├── BOM/
│                       │   │   └── BillOfMaterialDTO.java
│                       │   ├── modelDelivery/
│                       │   │   └── CustomerDto.java
│                       │   ├── order/
│                       │   │   └── OrderDTO.java
│                       │   ├── product/
│                       │   │   └── ProductDTO.java
│                       │   ├── productionorder/
│                       │   │   └── ProductionOrderDTO.java
│                       │   ├── rawmaterial/
│                       │   │   ├── README.md
│                       │   │   ├── ErrorResponse.java
│                       │   │   ├── PageResponseDTO.java
│                       │   │   ├── RawMaterialRequestDTO.java
│                       │   │   ├── RawMaterialResponseDTO.java
│                       │   │   ├── RawMaterialSummaryDTO.java
│                       │   │   └── SupplierSummaryDTO.java
│                       │   ├── supplier/
│                       │   │   └── SupplierDTO.java
│                       │   └── supplyorder/
│                       │       └── README.md
│                       ├── enums/
│                       │   ├── README.md
│                       │   ├── DeliveryStatus.java
│                       │   ├── OrderStatus.java
│                       │   ├── Priority.java
│                       │   ├── ProductionOrderStatus.java
│                       │   ├── Role.java
│                       │   └── SupplyOrderStatus.java
│                       └── model/
│                           ├── BaseEntity.java
│                           ├── BillOfMaterial.java
│                           ├── Customer.java
│                           ├── Delivery.java
│                           ├── Order.java
│                           ├── Product.java
│                           ├── ProductionOrder.java
│                           ├── ProductOrder.java
│                           ├── RawMaterial.java
│                           ├── Supplier.java
│                           ├── SupplyOrder.java
│                           ├── SupplyOrderLine.java
│                           └── User.java
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

![Architecture Diagram](/UML/supply-chaine-Class-Diagram.webp)
![Architecture Diagram](/UML/supply-chaine-UseCase.webp)
