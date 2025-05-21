# ShareIt - сервис для шеринга вещей

[![Java](https://img.shields.io/badge/Java-21-blue)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-green)](https://spring.io/projects/spring-boot)
[![Hibernate](https://img.shields.io/badge/Hibernate-6.4-lightgrey)](https://hibernate.org/)
[![Maven](https://img.shields.io/badge/Maven-3.9-red)](https://maven.apache.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)](https://www.postgresql.org/)

Сервис для аренды вещей между пользователями с возможностью бронирования и поиска предметов.

## 📌 Основные возможности

- 📦 Добавление вещей для аренды
- 🗓 Бронирование вещей на определенные даты
- 🔍 Поиск вещей по названию и описанию
- ✉️ Система запросов на новые предметы аренды
- ⭐ Оставление отзывов о арендованных вещах

## 🛠 Технологический стек

### Основные технологии
- **Java 21**
- **Spring Boot 3.2**
- **Spring Data JPA**
- **Hibernate**
- **PostgreSQL 16**
- **Docker**

### Вспомогательные технологии
- **Lombok** (уменьшение boilerplate-кода)
- **MapStruct** (маппинг DTO)

## 🚀 Запуск проекта

#### Требования
Docker 20.10+
Docker Compose 1.29+

##### Клонировать репозиторий:
git clone https://github.com/artem-shavriev/java-shareit.git

##### Настроить базу данных:
Создать БД PostgreSQL
Настроить подключение в application.properties

##### Собрать проект:
mvn clean package

##### Запустить приложение:
java -jar target/shareit-1.0.jar

## 📚 API Документация

#### 🗓 Booking API Endpoints

| Метод  | Эндпоинт | Описание | Заголовки | Параметры |
|--------|----------|----------|-----------|-----------|
| `GET` | `/bookings` | Получить бронирования пользователя | `X-Sharer-User-Id: {userId}` | `state=all` (опционально) |
| `GET` | `/bookings/owner` | Получить бронирования вещей пользователя | `X-Sharer-User-Id: {userId}` | `state=all` (опционально) |
| `POST` | `/bookings` | Создать новое бронирование | `X-Sharer-User-Id: {userId}` | - |
| `GET` | `/bookings/{bookingId}` | Получить бронирование по ID | - | - |
| `PATCH` | `/bookings/{bookingId}` | Обновить статус бронирования | `X-Sharer-User-Id: {userId}` | `approved=true/false` |

#### 📦 Item API Endpoints

| Метод  | Эндпоинт | Описание | Заголовки | Параметры |
|--------|----------|----------|-----------|-----------|
| `GET` | `/items` | Получить все вещи владельца | `X-Sharer-User-Id: {userId}` | - |
| `POST` | `/items` | Добавить новую вещь | `X-Sharer-User-Id: {userId}` | - |
| `GET` | `/items/{itemId}` | Получить вещь по ID | - | - |
| `PATCH` | `/items/{itemId}` | Обновить вещь | `X-Sharer-User-Id: {userId}` | - |
| `GET` | `/items/search` | Поиск вещей по тексту | - | `text={searchQuery}` |
| `POST` | `/items/{itemId}/comment` | Добавить комментарий к вещи | `X-Sharer-User-Id: {userId}` | - |

#### 📦 Item Request API Endpoints

| Метод  | Эндпоинт | Описание | Заголовки | Параметры |
|--------|----------|----------|-----------|-----------|
| `POST` | `/requests` | Создать новый запрос на вещь | `X-Sharer-User-Id: {userId}` | - |
| `GET` | `/requests` | Получить запросы текущего пользователя | `X-Sharer-User-Id: {userId}` | - |
| `GET` | `/requests/all` | Получить все запросы (кроме своих) | `X-Sharer-User-Id: {userId}` | - |
| `GET` | `/requests/{requestId}` | Получить запрос по ID | - | - |

#### 👥 User API Endpoints

| Метод  | Эндпоинт | Описание | Параметры | Тело запроса |
|--------|----------|----------|-----------|--------------|
| `POST` | `/users` | Создать нового пользователя | - | `{"name": "Имя", "email": "email@example.com"}` |
| `GET` | `/users` | Получить всех пользователей | - | - |
| `GET` | `/users/{userId}` | Получить пользователя по ID | `userId` | - |
| `PATCH` | `/users/{userId}` | Обновить данные пользователя | `userId` | `{"name": "Новое имя", "email": "new@example.com"}` |
| `DELETE` | `/users/{userId}` | Удалить пользователя | `userId` | - |

Диаграмма базы данных:
![data base image](/images/share-it-db.png)
