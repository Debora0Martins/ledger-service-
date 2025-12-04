README.md — Ledger Service (Versão Sênior e Profissional)

⚠️ Você só vai precisar copiar e colar no seu repositório!

<h1 align="center">💰 Ledger Service — Microservice Financeiro com Spring Boot + Kafka + Postgres</h1>

<p align="center">
  <img alt="Java" src="https://img.shields.io/badge/Java-17-007396?style=for-the-badge&logo=java">
  <img alt="Spring Boot" src="https://img.shields.io/badge/SpringBoot-3.3.0-6DB33F?style=for-the-badge&logo=springboot">
  <img alt="Kafka" src="https://img.shields.io/badge/Kafka-3.6.0-231F20?style=for-the-badge&logo=apacheKafka">
  <img alt="Postgres" src="https://img.shields.io/badge/PostgreSQL-16-4169E1?style=for-the-badge&logo=postgresql">
  <img alt="Docker" src="https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker">
</p>

---

## 📌 Sobre o Projeto

**Ledger Service** é um microserviço financeiro responsável por:

✔ Registrar **créditos**  
✔ Registrar **débitos**  
✔ Garantir **idempotência por correlationId**  
✔ Enviar eventos para o **Kafka**  
✔ Persistir saldos no **PostgreSQL**  
✔ Fornecer consultas de lançamentos e saldo do usuário  

Ele segue boas práticas de **Arquitetura Limpa**, **DDD Lite**, **Eventos Assíncronos**, **REST**, e integração com **Kafka Producer e Consumer**.

---

## 🏗 Arquitetura



┌──────────────┐ POST /credit | /debit
│ Controller │──────────────────────────────┐
└──────────────┘ │
▼
┌──────────────┐ Regras de Negócio ┌──────────────┐
│ Service │──────────────────────────▶│ Balance Repo │
└──────────────┘ └──────────────┘
│
│ envia evento
▼
┌──────────────┐ Kafka Topic ┌──────────────┐
│ Producer │──────────────────────▶│ Consumer │
└──────────────┘ └──────────────┘


---

## ⚙️ **Tecnologias Utilizadas**

- Java 17  
- Spring Boot 3 (Web, JPA, Validation)  
- Apache Kafka  
- Spring Kafka  
- PostgreSQL  
- Docker Compose  
- Lombok  
- Maven  

---

## 🚀 **Como Rodar o Projeto**

### ▶️ 1. Subir Kafka + Zookeeper + Postgres

```sh
docker compose up -d

▶️ 2. Rodar a aplicação

Via Maven Wrapper:

./mvnw spring-boot:run

🗄 Estrutura do Projeto
src/main/java/com/fintech/ledger_service/
│
├── application
│   ├── controller
│   └── dto
│
├── domain
│   ├── entity
│   ├── exception
│   └── service
│
├── infrastructure
│   ├── kafka
│   ├── config
│   └── repository

📡 Endpoints
➕ Crédito
POST /ledger/credit


Body:

{
  "accountId": "123",
  "amount": 100.50,
  "correlationId": "abc-123"
}

➖ Débito
POST /ledger/debit


Body:

{
  "accountId": "123",
  "amount": 50,
  "correlationId": "xyz-999"
}

📄 Listar Lançamentos
GET /ledger/entries/{accountId}

💰 Obter Saldo
GET /ledger/balance/{accountId}

📨 Eventos Kafka
Producer — envia:
topic: ledger-events
key: accountId
value: ledgerEntryId

Consumer — recebe e registra:
INFO  - Evento recebido, processando...

🧪 Testes

Testes unitários com JUnit

Testes com Spring Boot Test

Testes isolados do service usando mocks (Mockito)

🏁 Próximos Passos (Roadmap)

✔ Criar README (você já está aqui!)
⬜ Criar Pipeline CI/CD no GitHub Actions
⬜ Adicionar integração com Grafana + Prometheus
⬜ Criar testes de carga com JMeter
⬜ Deploy em Kubernetes

👩‍💻 Autora

Débora Martins — Back-end Developer, Java & DevOps
GitHub: https://github.com/Debora0Martins
