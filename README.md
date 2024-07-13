# Coworking service

Coworking Service - это веб приложение для управления коворкинг-пространством. 
Пользователи могут бронировать рабочие места, конференц-залы, управлять бронированиями и просматривать доступность ресурсов.

## Требования

1. Система сборки `Maven`
2. Версия `java 17` (`graalvm-jdk-17.0.11`)
3. Для Windows/macOS `Docker Desktop`, для Linux `Docker Engine` и `Docker Compose`
4. Сервер `Tomcat 10`

## Установка

1. Клонируйте репозиторий: `git clone git@github.com:13tom13/coworking-service.git`
2. Перейдите в каталог проекта: `cd coworking-service`
3. Соберите проект с помощью Maven: `mvn clean install`
4. Запустите базу данных PostgresSQL в Docker командой: `docker-compose up -d`
5. Запустите приложение на сервере `Tomcat 10`

## Функционал

1. Регистрация и авторизация пользователя.
2. Просмотр списка всех доступных рабочих мест и конференц-залов.
3. Просмотр доступных слотов для бронирования на конкретную дату.
4. Бронирование рабочего места или конференц-зала на определённое время и дату.
5. Отмена бронирования.
6. Добавление новых рабочих мест и конференц-залов, а также управление существующими.
7. Просмотр всех бронирований и их фильтрация по дате, пользователю или ресурсу.

## Техническая реализация

1. Приложение написано на `Java` с использованием фреймворка `Spring MVC`.
2. Взаимодействие осуществляется через отправку `HTTP` запросов.
3. Данные хранятся в базе данных `PostgreSQL`.
4. Реализованы `CRUD` операции для управления бронированиями и ресурсами.
5. Реализована авторизация и аутентификация пользователей.
6. Реализована обработка конфликтов бронирований.
7. При помощи `Spring AOP` реализованны аспекты для замера времени выполнения методов приложения, логирования действий пользователей, включая регистрацию, аутентификацию, бронирование и управление ресурсами.
8. В приложении реализованы механизмы валидации входящих данных с использованием аспектов валидации и кастомных валидаторов.
9. Приложение покрыто unit-тестами с использованием `JUnit 5`, `Mockito`, `AssertJ`, `TestContainers`, `MockMvc`.

### Тестовый пользователь (редактирование профиля, создание и редактирования бронирований):
- Email: test@mail.ru
- Пароль: pass

### Модератор (редактирование коворкингов, бронирований)
- Email: moderator@mail.ru
- Пароль: moderator

### Администратор(плюс редактирование пользователей):
- Email: admin@mail.ru
- Пароль: admin

