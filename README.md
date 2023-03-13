# Animals chipization
Сервис, разработанный для проведения многолетних экспериментов, связанных с миграциями животных, а также для
отслеживания изменения сред обитания и ведения истории.

## Технологический стек
Java, Spring Boot, Spring Data JPA, PostgreSQL, etc.

## Установка и запуск
Склонируйте данный репозиторий. Убедитесь, что у вас установлен [Docker](https://www.docker.com/). Далее, создайте
`.env` файл с конфигурацией сервиса. Ниже находится пример содержания файла:
```text
SERVER_PORT=8080
SPRING_DATASOURCE_URL=jdbc:postgresql://database:5432/animals-chipization

POSTGRES_DB=animals-chipization
POSTGRES_USER=user
POSTGRES_PASSWORD=12345678
```

Для запуска используйте следующую команду:
```shell
docker compose up --build -d
```

Для завершения работы сервиса воспользуйтесь следующей командой:
```shell
docker compose down
```
