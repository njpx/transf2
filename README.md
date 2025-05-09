# 🏦 ___ System

This is a **___ System**, built with:

* **Angular 19 (SPA)**
* **Spring Boot API**
* **PostgreSQL Database**
* **Docker & Docker Compose**

---

## ⚙️ System Architecture

```
[ Angular 19 SPA ]  -->  [ Spring Boot API ]  -->  [ PostgreSQL Database ]
          |                    |                        |
        Docker               Docker                   Docker
```

All components are containerized and managed through Docker Compose.

---

## 🚀 How to Run the System

1. **Clone the Repository**

   ```bash
   git clone https://github.com/njpx/transf2.git
   cd <your-repository-directory>
   ```

2. **Start the System with Docker Compose**

   ```bash
   docker-compose up --build
   ```

3. **Access the System**

   * **Frontend (Angular)**: [http://localhost](http://localhost)
   * **Backend (API)**: [http://localhost:8080/api](http://localhost:8080/api)

---

## 🧑‍💼 Sample Users

| **Role**    | **Username**     | **Password** | **PIN**  |
|------------|------------------|--------------|---------|
| Teller     | teller@teller     | teller1234   | N/A     |
| Customer 1 | cust1@cust1       | abc12345     | 999999  |
| Customer 2 | cust2@cust2       | abc12345     | 999999  |


---

## 📝 Notes

* **Default Ports**

  * Angular SPA: **80**
  * Spring Boot API: **8080**
  * PostgreSQL: **5432**


---

