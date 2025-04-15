# 🏋️‍♂️ The Coding Crew Gym Management App

A simple Java-based application for managing gym members, tracking workout classes, and providing basic administrative features. Built with Java and Maven.

---

## 📦 Features

- Add, update, and remove gym members
- Track workout history and attendance
- Generate reports for gym usage
- Command-line based interface
- Lightweight and easy to run

---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven

---

### 🔐 Environment Configuration

This project requires an `.env` file in the root directory to configure the database connection.

#### Example `.env` File

```env
# DATABASE CONFIG 
DB_URL=jdbc:postgresql://<your-repo-name>:<port>/<database_name>
DB_USER=youruser
DB_PASSWORD=yourpassword
```

---

### 🔧 Installation & Running

#### Clone the Repository

```bash
git clone https://github.com/007snoop/GymManApp.git
cd GymManApp
```

#### Add Your .env
```env
# DATABASE CONFIG 
DB_URL=jdbc:postgresql://<your-repo-name>:<port>/<database_name>
DB_USER=youruser
DB_PASSWORD=yourpassword
```

#### On Windows
```bash
run.bat
```

#### On Linux/Mac
```bash
chmod +x run.sh
./run.sh
```

#### Or via Maven

```bash
mvn clean compile exec:java
```

---

## ⚙️ Technologies

- Java 17
- Maven
- PostgreSQL
- dotenv (for environment variables)
- IntelliJ IDEA (recommended)

---

## ✍️ Author

- GitHub: [007snoop](https://github.com/007snoop)
- Github: [CodyC1998](https://github.com/CodyC1998)
- Github: [JaowadH](https://github.com/JaowadH)