# 🏥 Hospital Management System

A comprehensive RESTful API-based Hospital Management System built with Java Spring Boot for managing patients, doctors, appointments, departments, and insurance policies. Features role-based access control (RBAC), JWT authentication, and sophisticated appointment slot management.

---

## 🎯 Features

### Core Functionality
- **👥 Patient Management**: Registration, profile updates, appointment booking, password reset
- **👨‍⚕️ Doctor Management**: Registration by specialty, availability tracking, appointment management
- **📅 Appointment System**: 
  - 30-minute slots between 09:00-17:00
  - Maximum 10 patients per time slot
  - Real-time availability checking
  - Appointment status tracking (SCHEDULED, COMPLETED, CANCELLED)
- **🏢 Department Management**: Create and manage hospital departments
- **💳 Insurance Management**: Patient insurance policy tracking with expiry validation
- **👤 Role-Based Access Control**: 4 distinct roles (Admin, Doctor, Patient, Reception)
- **🔐 Security**: JWT token-based authentication, Argon2id password hashing, input validation

### Security Features
- JWT token-based authentication (10-minute expiration)
- Role-based endpoint authorization
- Centralized exception handling with 27 standardized error codes
- Input validation with regex patterns
- Transactional data consistency
- TraceId logging for audit trails
- Soft delete implementation

---

## 🛠️ Technology Stack

| Layer | Technology |
|-------|-----------|
| **Language** | Java 21 |
| **Framework** | Spring Boot 3.5.12 |
| **Security** | Spring Security, JWT (JJWT 0.12.5) |
| **Database** | JPA/Hibernate, MySQL 8.0+ |
| **Build Tool** | Maven 3.6+ |
| **Password Encoding** | Argon2id (Spring Security Argon2PasswordEncoder) |
| **Logging** | SLF4J/Logback |
| **Utilities** | Lombok, Jackson |

---

## 📁 Project Structure

```
hospitalsystem/
├── src/main/java/com/project/hospitalsystem/
│   ├── Controller/                 # REST API endpoints
│   │   ├── AdminController.java
│   │   ├── AuthController.java
│   │   ├── DoctorController.java
│   │   ├── PatientController.java
│   │   ├── ReceptionController.java
│   │   └── HospitalController.java
│   │
│   ├── Service/                    # Business logic interfaces
│   │   ├── PatientService.java
│   │   ├── DoctorService.java
│   │   ├── AppointmentService.java
│   │   ├── InsuranceService.java
│   │   └── ReceptionService.java
│   │
│   ├── Service/Implementation/     # Service implementations
│   │   ├── PatientServiceImpl.java
│   │   ├── DoctorServiceImpl.java
│   │   ├── AppointmentServiceImpl.java
│   │   ├── InsuranceServiceImpl.java
│   │   └── ReceptionServiceImpl.java
│   │
│   ├── Entity/                     # JPA entities
│   │   ├── User.java               # Base user entity with roles
│   │   ├── Patient.java            # Patient profile (extends User)
│   │   ├── Doctor.java             # Doctor profile with specialization
│   │   ├── Appointment.java        # Appointment details
│   │   ├── Department.java         # Hospital departments
│   │   ├── Insurance.java          # Patient insurance policies
│   │   ├── Role.java               # User roles enum
│   │   └── Receptionist.java       # Receptionist profile
│   │
│   ├── Repo/                       # Spring Data JPA repositories
│   │   ├── UserRepository.java
│   │   ├── PatientRepository.java
│   │   ├── DoctorRepository.java
│   │   ├── AppointmentRepository.java
│   │   ├── DepartmentRepository.java
│   │   ├── InsuranceRepository.java
│   │   └── ReceptionistRepository.java
│   │
│   ├── Model/                      # Request/Response DTOs
│   │   ├── LoginRequestModel.java
│   │   ├── LoginResponseModel.java
│   │   ├── PatientRequest/Response.java
│   │   ├── DoctorRequest/Response.java
│   │   ├── AppointmentRequest/Response.java
│   │   ├── AppointmentStatusUpdate.java
│   │   └── PasswordResetRequest/Response.java
│   │
│   ├── Exception/                  # Exception handling
│   │   ├── GlobalExceptionHandler.java
│   │   ├── BaseException.java
│   │   ├── ErrorCode.java          # 27 error codes
│   │   └── ErrorResponseModel.java
│   │
│   ├── JwtConfig/                  # JWT authentication
│   │   ├── AuthService.java
│   │   ├── AuthUtil.java
│   │   └── JwtAuthFilter.java
│   │
│   ├── Mapper/                     # Entity to DTO mapping
│   │   ├── HospitalMapper.java
│   │   └── AppointmentMapper.java
│   │
│   ├── Security/                   # Spring Security configuration
│   │   └── AppConfig.java
│   │
│   └── HospitalsystemApplication.java
│
├── src/main/resources/
│   ├── application.properties      # Configuration properties
│   └── META-INF/
│
├── pom.xml                         # Maven dependencies
├── README.md                       # This file
└── mvnw / mvnw.cmd                # Maven wrapper scripts
```

---

## 🗄️ Database Schema

### Core Entities

#### 1. **User** (Base entity)
```
- id (PK)
- name
- email (UNIQUE)
- phoneNumber
- password (Argon2id hashed)
- roles (ElementCollection with @Enumerated)
- isActive (boolean)
- createdAt
```

#### 2. **Patient** (extends User)
```
- dateofbirth
- bloodGroup (ENUM)
- gender (ENUM)
- fullAddress
- city
- pincode
- insurance_id (FK to Insurance)
```

#### 3. **Doctor** (extends User)
```
- specialization
- dateOfBirth
- licenseNumber (UNIQUE)
- bloodGroup (ENUM)
- gender (ENUM)
- qualification
- consultationFee
- department_id (FK to Department)
```

#### 4. **Appointment**
```
- id (PK)
- appointmentDate
- appointmentTime
- status (ENUM: SCHEDULED, COMPLETED, CANCELLED)
- patient_id (FK)
- doctor_id (FK)
- createdAt
```

#### 5. **Department**
```
- id (PK)
- departmentName (UNIQUE)
- isActive (boolean)
```

#### 6. **Insurance**
```
- id (PK)
- policyNumber (UNIQUE)
- insuranceProvider
- expiryDate
```

#### 7. **Role** (ENUM)
```
PATIENT, DOCTOR, RECEPTION, ADMIN
```

### Key Relationships
- **Doctor ↔ Department**: Many-to-One (eager loading)
- **Appointment ↔ Patient**: Many-to-One (lazy loading)
- **Appointment ↔ Doctor**: Many-to-One (lazy loading)
- **Patient ↔ Insurance**: One-to-One Optional (lazy loading)

---

## 🔌 API Endpoints

### Authentication
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/auth/login` | User login with email & password | Public |
| POST | `/api/auth/register` | Patient self-registration | Public |

### Patient Management
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/sys/patient/profile` | Get patient profile | PATIENT |
| GET | `/sys/patient/{id}` | Get patient by ID | PATIENT |
| PUT | `/sys/patient/{id}` | Update patient info | PATIENT |
| DELETE | `/sys/patient/{id}` | Deactivate patient account | PATIENT |
| POST | `/sys/patient/password-reset` | Reset patient password | PATIENT |
| GET | `/sys/patient/appointments` | Get patient's appointments (paginated) | PATIENT |
| POST | `/sys/patient/book-appointment` | Book new appointment | PATIENT |

### Doctor Management
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/sys/doctor/{id}` | Get doctor profile | DOCTOR |
| GET | `/sys/doctor/appointments` | Get doctor's appointments (paginated) | DOCTOR |
| POST | `/sys/doctor/password-reset` | Reset doctor password | DOCTOR |
| GET | `/hospital/doctors` | Get all doctors (active departments only) | Public |
| GET | `/hospital/doctors-summary` | Get doctor summaries for dashboard | Public |

### Appointment Management
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/sys/patient/appointment/{id}` | Get appointment details | PATIENT |
| PUT | `/sys/patient/appointment/{id}/status` | Update appointment status | DOCTOR |
| DELETE | `/sys/patient/appointment/{id}` | Cancel appointment | PATIENT |

### Admin Management
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/sys/secureline/admin/department` | Create department | ADMIN |
| GET | `/sys/secureline/admin/departments` | List all departments | ADMIN |
| POST | `/sys/secureline/admin/register-doctor` | Register new doctor | ADMIN |
| GET | `/sys/secureline/admin/doctors` | Get all doctors | ADMIN |
| GET | `/sys/secureline/admin/patients` | Get all patients | ADMIN |
| POST | `/sys/secureline/admin/password-reset` | Reset any user password | ADMIN |
| DELETE | `/sys/secureline/admin/deactivate/{id}` | Deactivate any user | ADMIN |
| DELETE | `/sys/secureline/admin/patient/{id}` | Deactivate patient | ADMIN |

### Reception Management
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/sys/internal/reception/register-receptionist` | Register receptionist | RECEPTION |
| GET | `/sys/internal/reception/search-patient` | Search patient by phone/name | RECEPTION |
| POST | `/sys/internal/reception/add-patient` | Register new patient | RECEPTION |
| PUT | `/sys/internal/reception/update-patient/{id}` | Update patient info | RECEPTION |
| DELETE | `/sys/internal/reception/deactivate/{id}` | Deactivate patient | RECEPTION |
| POST | `/sys/internal/reception/book-appointment` | Book appointment for patient | RECEPTION |
| POST | `/sys/internal/reception/password-reset` | Reset receptionist password | RECEPTION |
| DELETE | `/sys/internal/reception/deactivate-receptionist/{id}` | Deactivate receptionist | RECEPTION |

---

## 🔐 Authentication & Authorization

### JWT Implementation
```
// Login flow:
1. POST /api/auth/login with email & password
2. AuthService validates credentials
3. JWT token generated (10 min expiration, HMAC-SHA signing)
4. Token structure: Header.Payload.Signature

// Token usage:
1. Add Authorization header: "Bearer <token>"
2. JwtAuthFilter extracts and validates token
3. Request continues if token valid
4. Token refresh: New login required after expiration
```

### Role-Based Access Control (RBAC)
```
PATIENT:
  - View own profile
  - Book/cancel appointments
  - View own appointment history

DOCTOR:
  - View own profile
  - View appointments for today
  - Update appointment status

RECEPTION:
  - Register patients
  - Book appointments on behalf of patients
  - Search and manage patient records

ADMIN:
  - Create departments
  - Register doctors
  - View all users
  - Deactivate accounts
  - Password reset for any user
```

---

## ⚠️ Error Handling

### Error Response Format
```json
{
  "code": "ERR_005",
  "message": "Patient not found.",
  "timestamp": 1714828800000,
  "traceId": "550e8400-e29b-41d4-a716-446655440000"
}
```

### Error Codes (27 Total)
| Code | Error | HTTP Status |
|------|-------|------------|
| ERR_002 | VALIDATION_ERROR | 422 |
| ERR_003 | UNAUTHORIZED | 401 |
| ERR_004 | INTERNAL_SERVER_ERROR | 500 |
| ERR_005 | PATIENT_NOT_FOUND | 422 |
| ERR_006 | PATIENT_ALREADY_REGISTERED | 422 |
| ERR_007 | PATIENT_REACTIVATION_FAILED | 422 |
| ERR_008 | INSURANCE_SAVE_FAILED | 422 |
| ERR_009 | PATIENT_BUILD_FAILED | 422 |
| ERR_010 | DOCTOR_NOT_FOUND | 422 |
| ERR_011 | DOCTOR_LICENSE_EXISTS | 422 |
| ERR_012 | DOCTOR_EMAIL_EXISTS | 422 |
| ERR_013 | DOCTOR_DATA_INCOMPLETE | 422 |
| ERR_014 | DEPARTMENT_NOT_FOUND | 422 |
| ERR_015 | APPOINTMENT_NOT_FOUND | 422 |
| ERR_016 | APPOINTMENT_INVALID_TIME | 422 |
| ERR_017 | APPOINTMENT_INVALID_SLOT | 422 |
| ERR_018 | APPOINTMENT_SLOT_FULL | 422 |
| ERR_019 | APPOINTMENT_CREATE_FAILED | 422 |
| ERR_020 | INSURANCE_NOT_FOUND | 422 |
| ERR_021 | INSURANCE_CREATE_FAILED | 422 |
| ERR_022 | RECEPTIONIST_NOT_FOUND | 422 |
| ERR_023 | RECEPTIONIST_PHONE_EXISTS | 422 |
| ERR_024 | RECEPTIONIST_NULL_REQUEST | 422 |
| ERR_025 | UNAUTHORIZED_ACCESS | 401 |
| ERR_026 | INVALID_INPUT | 422 |
| ERR_027 | OPERATION_FAILED | 422 |

---

## 📋 Appointment System Rules

### Booking Rules
- ✅ Time slots: 09:00 - 17:00 (30-minute intervals)
- ✅ Valid slots: 09:30, 10:00, 10:30, 11:00, ..., 16:30
- ✅ Cannot book past appointments
- ✅ Maximum 10 patients per time slot per doctor
- ✅ Availability checked in real-time based on booked appointments

### Status Workflow
```
SCHEDULED → COMPLETED
SCHEDULED → CANCELLED
```

---

## 🚀 Getting Started

### Prerequisites
- **Java 21+**: [Download JDK 21](https://www.oracle.com/java/technologies/downloads/#java21)
- **Maven 3.6+**: `mvn -v`
- **MySQL 8.0+**: [Download MySQL](https://dev.mysql.com/downloads/mysql/)
- **Git**: For version control

### Installation & Setup

#### 1. Clone the Repository
```bash
git clone <repository-url>
cd hospitalsystem
```

#### 2. Database Configuration
Create MySQL database and user:
```sql
CREATE DATABASE hospitalsystem;
CREATE USER 'hospital'@'localhost' IDENTIFIED BY 'hospital_password';
GRANT ALL PRIVILEGES ON hospitalsystem.* TO 'hospital'@'localhost';
FLUSH PRIVILEGES;
```

#### 3. Configure Application Properties
Edit `src/main/resources/application.properties`:
```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/hospitalsystem
spring.datasource.username=hospital
spring.datasource.password=hospital_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

# JWT Configuration
jwt.key=your-secret-key-min-32-characters-long-for-security
jwt.expiration=600000

# Application Configuration
server.port=8080
spring.application.name=hospitalsystem

# Logging
logging.level.root=INFO
logging.level.com.project.hospitalsystem=DEBUG
```

#### 4. Build the Project
```bash
mvn clean package
```

#### 5. Run the Application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

---

## 📚 API Usage Examples

### 1. Patient Registration
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "phoneNumber": "9876543210",
    "dateofbirth": "1995-05-15",
    "bloodGroup": "O_POSITIVE",
    "gender": "MALE",
    "fullAddress": "123 Main St",
    "city": "New York",
    "pincode": "10001",
    "password": "SecurePass@123"
  }'
```

### 2. Patient Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "SecurePass@123"
  }'

# Response:
{
  "id": 1,
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "message": "Login successful!"
}
```

### 3. Book Appointment
```bash
curl -X POST http://localhost:8080/sys/patient/book-appointment \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "doctorId": 5,
    "appointmentDate": "2026-05-10",
    "appointmentTime": "09:30"
  }'
```

### 4. Get Doctor List (For Patients)
```bash
curl -X GET http://localhost:8080/hospital/doctors \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Returns doctors with real-time availability
```

---

## 🔒 Security Best Practices

### Current Implementation
✅ Argon2id password hashing (Spring Security Argon2PasswordEncoder)  
✅ JWT token-based stateless authentication  
✅ Role-based method-level authorization  
✅ Input validation with regex patterns  
✅ Centralized exception handling  
✅ SQL injection protection via parameterized queries  
✅ Standard HTTP methods validation  

### Recommended Enhancements
🔧 Store JWT secret in environment variables  
🔧 Implement rate limiting on auth endpoints  
🔧 Add HTTPS/TLS enforcement  
🔧 Enable CORS with specific origins  
🔧 Implement audit logging for sensitive operations  
🔧 Add refresh token mechanism  
🔧 Implement request/response encryption for sensitive data  

---

## 🧪 Testing

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=PatientServiceImplTest
```

### Test Coverage
```bash
mvn clean test jacoco:report
# Coverage report: target/site/jacoco/index.html
```

---

## 📊 Project Statistics

| Metric | Count |
|--------|-------|
| **Total Java Files** | 69 |
| **API Endpoints** | 73 |
| **Service Methods** | 50+ |
| **Error Codes** | 27 |
| **Database Tables** | 7 |
| **Maven Dependencies** | 20+ |
| **Code Lines** | 10,000+ |

---

## 🔄 Development Workflow

### Making Changes
1. Create feature branch: `git checkout -b feature/your-feature`
2. Make changes and follow coding standards
3. Commit: `git commit -m "Add feature: description"`
4. Push: `git push origin feature/your-feature`
5. Create Pull Request on GitHub

### Code Quality Standards
- Follow Google's Java Style Guide
- Use meaningful variable/method names
- Add JavaDoc comments for public methods
- Keep methods small and focused (SOLID principles)
- Write unit tests for business logic

---

## 📝 Database Migrations

The application uses Hibernate with `spring.jpa.hibernate.ddl-auto=update`, so schema changes are applied automatically. For production:

```properties
# Use 'validate' in production to prevent accidental schema changes
spring.jpa.hibernate.ddl-auto=validate
```

---

## 🚁 Deployment

### Docker Deployment (Optional)
```dockerfile
FROM eclipse-temurin:21-jdk
COPY target/hospitalsystem-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

Build and run:
```bash
docker build -t hospitalsystem:latest .
docker run -p 8080:8080 -e DATABASE_URL=jdbc:mysql://db:3306/hospitalsystem hospitalsystem:latest
```

---

## 📜 License

This project is licensed under the MIT License - see LICENSE file for details.

---

**Last Updated**: May 4, 2026  
**Version**: 1.0.0  
**Maintainer**: Hospital System Development Team

---

### Quick Command Reference
```bash
# Build
mvn clean package

# Run
mvn spring-boot:run

# Test
mvn test

# Check code style
mvn checkstyle:check

# View dependencies
mvn dependency:tree

# Generate JavaDoc
mvn javadoc:javadoc
```