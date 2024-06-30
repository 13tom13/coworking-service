# Coworking service

Coworking Service - это консольное приложение для управления коворкинг-пространством. 
Пользователи могут бронировать рабочие места, конференц-залы, управлять бронированиями и просматривать доступность ресурсов.

## Требования

1. Система сборки `Maven`
2. Версия `java 17` (`graalvm-jdk-17.0.11`)
3. Для Windows/macOS `Docker Desktop`, для Linux `Docker Engine` и `Docker Compose`

## Установка

1. Клонируйте репозиторий: `git clone git@github.com:13tom13/coworking-service.git`
2. Перейдите в каталог проекта: `cd coworking-service`
3. Соберите проект с помощью Maven: `mvn clean install`
4. Запустите базу данных PostgresSQL в Docker командой: `docker-compose up -d`
5. Запустите приложение: `java -jar target/coworking-service-1.0-SNAPSHOT.jar`

## Функционал

1. Регистрация и авторизация пользователя.
2. Просмотр списка всех доступных рабочих мест и конференц-залов.
3. Просмотр доступных слотов для бронирования на конкретную дату.
4. Бронирование рабочего места или конференц-зала на определённое время и дату.
5. Отмена бронирования.
6. Добавление новых рабочих мест и конференц-залов, а также управление существующими.
7. Просмотр всех бронирований и их фильтрация по дате, пользователю или ресурсу.

## Техническая реализация

1. Приложение написано на `Java`.
2. Приложение является консольным.
3. Данные хранятся в базе данных `PostgreSQL`.
4. Реализованы `CRUD` операции для управления бронированиями и ресурсами.
5. Реализована авторизация и аутентификация пользователей.
6. Реализована обработка конфликтов бронирований.
7. Приложение покрыто unit-тестами с использованием `JUnit 5`, `Mockito`, `AssertJ`, `TestContainers`.

### Тестовый пользователь (редактирование профиля, создание и редактирования бронирований):
- Email: test@mail.ru
- Пароль: pass

### Модератор (редактирование коворкингов, бронирований)
- Email: moderator
- Пароль: moderator

### Администратор(плюс редактирование пользователей):
- Email: admin
- Пароль: admin

