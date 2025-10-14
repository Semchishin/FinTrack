FinTrack – Financial Transactions Tracker
English Version
Project Description

FinTrack is a simple financial transaction tracking application.
It allows you to add, update, delete, and view transactions.
The project uses a multi-module Gradle structure with the following modules:

fintrack-core – contains models, repositories, and services.

fintrack-api – Spring Boot application exposing REST endpoints.

flyway – manages database migrations.

Database: PostgreSQL
Migrations: Flyway
Testing: AssertJ, JUnit 5

Features

Add new financial transactions

Update existing transactions

Delete transactions by ID

Retrieve transactions by ID or get all transactions

Technologies Used

Java 21

Spring Boot 3.5.0

Flyway

PostgreSQL

Gradle

AssertJ + JUnit 5

Getting Started
1. Clone the repository
git clone https://github.com/<your-username>/FinTrack.git
cd FinTrack

2. Set up PostgreSQL

Ensure PostgreSQL is installed and running.

Create a database fintrack.

Configure application.yml with correct url, username, and password.

3. Run Flyway migrations

If using Spring Boot with Flyway:

./gradlew bootRun


Migrations will automatically create tables.

4. Run the application
./gradlew :fintrack-api:bootRun


The API will start on http://localhost:8080.

Project Structure
FinTrack/
├── fintrack-core/       # Core module: models, repositories, services
├── fintrack-api/        # API module: Spring Boot application
├── flyway/              # Database migrations
├── build.gradle
└── settings.gradle

Русская версия
Описание проекта

FinTrack — это простое приложение для учёта финансовых транзакций.
Позволяет добавлять, изменять, удалять и просматривать транзакции.
Проект использует многомодульную структуру Gradle:

fintrack-core – модели, репозитории и сервисы.

fintrack-api – Spring Boot приложение с REST API.

flyway – управление миграциями базы данных.

База данных: PostgreSQL
Миграции: Flyway
Тестирование: AssertJ, JUnit 5

Функционал

Добавление новых транзакций

Обновление существующих транзакций

Удаление транзакций по ID

Получение транзакции по ID или всех транзакций

Используемые технологии

Java 21

Spring Boot 3.5.0

Flyway

PostgreSQL

Gradle

AssertJ + JUnit 5

Как запустить проект
1. Клонировать репозиторий
git clone https://github.com/<your-username>/FinTrack.git
cd FinTrack

2. Настроить PostgreSQL

Убедитесь, что PostgreSQL установлен и запущен.

Создайте базу данных fintrack.

Укажите в application.yml правильные url, username и password.

3. Запустить миграции Flyway

Если используете Spring Boot с Flyway:

./gradlew bootRun


Миграции создадут таблицы автоматически.

4. Запустить приложение
./gradlew :fintrack-api:bootRun


API будет доступно по адресу http://localhost:8080.

Структура проекта
FinTrack/
├── fintrack-core/       # Core модуль: модели, репозитории, сервисы
├── fintrack-api/        # API модуль: Spring Boot приложение
├── flyway/              # Миграции базы данных
├── build.gradle
└── settings.gradle
