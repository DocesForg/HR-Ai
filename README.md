# HR-Ai — генератор сценариев собеседований

Веб-приложение на React + TypeScript и API на Java 21 / Spring Boot для создания вопросов к собеседованию на основе описания вакансии с помощью агента Yandex AI Studio.

## Возможности

- Создание плана интервью на русском языке с группировкой вопросов по компетенциям и подсказками для оценки сильных ответов.
- Настройка контекста вакансии: должность, уровень специалиста, описание и фокус интервью.
- Просмотр результата в формате Markdown, копирование и скачивание.
- Примеры описаний вакансий, состояния загрузки и ошибки, адаптивный интерфейс.
- Слоистая архитектура backend: Controller → Service → AiGateway → LlmClient → YandexAgentClient.
- Валидация, тайм-ауты HTTP-запросов, повторные попытки, автоматический выключатель, метрики Actuator, Prometheus, OpenAPI и модульные тесты.

## Настройка и секреты

Необходимы Java 21, Maven, Node.js 20+ и npm. Для запуска всего приложения можно использовать Docker Engine/Desktop.

Скопируйте файл `.env.example` в локальный файл `.env` и задайте `YANDEX_API_KEY`. Не добавляйте настоящий ключ в Git. При необходимости можно переопределить `YANDEX_PROJECT_ID` и `YANDEX_AGENT_ID`. Значения по умолчанию соответствуют предоставленной конфигурации проекта и агента.

- `YANDEX_API_KEY` — секретный API-ключ Yandex AI Studio.
- `YANDEX_PROJECT_ID` — значение, передаваемое в заголовке `OpenAI-Project`.
- `YANDEX_AGENT_ID` — идентификатор промпта/агента для Responses API.
- `SERVER_PORT` — порт backend, по умолчанию 8080.
- `VITE_API_BASE_URL` — необязательная настройка для отдельного адреса frontend; не храните секреты в переменных с префиксом `VITE_`.

## Локальный запуск

Укажите `YANDEX_API_KEY` в переменных окружения и запустите backend из корня репозитория:

    mvn spring-boot:run

В другом терминале запустите frontend:

    cd frontend
    npm install
    npm run dev

Откройте http://localhost:5173. Vite перенаправляет запросы к `/api` на http://localhost:8080. Swagger UI доступен по адресу http://localhost:8080/swagger-ui.html.

## Запуск в Docker

Задайте `YANDEX_API_KEY` в переменных окружения и выполните:

    docker compose up --build

Откройте http://localhost:8080. Для запуска Compose необходимо задать `YANDEX_API_KEY`.

## API

`POST /api/interviews/generate`

Поля JSON-запроса:

- `vacancyDescription` — описание вакансии (обязательно, не более 12 000 символов).
- `position` — название должности (необязательно, не более 120 символов).
- `seniority` — уровень специалиста (необязательно, не более 80 символов).
- `questionFocus` — фокус интервью (необязательно, не более 80 символов).

Ответ содержит поля `questions`, `provider` и `requestId`.

- Swagger UI: `/swagger-ui.html`
- JSON OpenAPI: `/v3/api-docs`
- Состояние приложения: `/actuator/health`
- Метрики: `/actuator/metrics`
- Prometheus: `/actuator/prometheus`

## Проверки

    mvn test
    cd frontend && npm run build

Для проверки реального обращения к агенту необходим действующий API-ключ и доступ к настроенному проекту/агенту Yandex. Не считайте интеграцию проверенной, пока такой тест не выполнен.
