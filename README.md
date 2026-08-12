# Arquitetura Hexagonal MBA

Aplicação Spring Boot (Java 17, Gradle) construída com arquitetura hexagonal, dividida em três módulos:

- `domain` — entidades e regras de negócio
- `application` — casos de uso
- `infrastructure` — entrega (REST/GraphQL), persistência e integração com o framework

## Pré-requisitos

- JDK 17
- Docker (para executar o banco de dados MySQL)

## Executando o banco de dados

Inicie o MySQL com Docker Compose:

```sh
docker compose up -d
```

Isso cria o banco de dados `events` e expõe o MySQL na porta `3306`.

## Executando a aplicação

```sh
./gradlew :infrastructure:bootRun
```

A aplicação inicia na porta `8080`.

## Executando os testes

Execute os testes de todos os módulos:

```sh
./gradlew :domain:test :application:test :infrastructure:test
```

Execute os testes de um módulo específico:

```sh
./gradlew :domain:test
```

## Cascata de cancelamento

Ao cancelar um evento, o `CancelEventUseCase` chama `Event.cancel()`, que muda o status do agregado e registra o evento de domínio `EventCancelled` (tipo `event-ticket.cancelled`). O `EventDatabaseRepository` persiste esse evento na tabela de outbox na mesma transação, e o job `OutboxRelay` o publica no `QueueGateway`. O `ConsumerQueueGateway` consome a mensagem de forma assíncrona e, ao identificar o tipo `event-ticket.cancelled`, executa o `CancelEventTicketsUseCase`, que cancela todos os tickets do evento. Esse fluxo é coberto pelo teste de integração `infrastructure/src/test/java/br/com/fullcycle/e2e/CancelEventTicketTestIT.java`.
