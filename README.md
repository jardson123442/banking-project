# banking-project

# Customer Account Opening

Projeto de abertura e consulta de solicitações de cadastro de clientes, desenvolvido com **Java 21** e **Spring 4.0.0**, utilizando uma arquitetura composta por um microserviço de negócio e persistência e uma API responsável pela exposição dos endpoints.

## Tecnologias

- Java 21
- Spring 4.0.0
- API REST
- Maven
- PostgreSQL
- Flyway
- JUnit e Mockito para testes automatizados
- Swagger / OpenAPI para documentação dos endpoints

## Arquitetura

O projeto é composto por duas aplicações independentes:

| Aplicação | Porta | Responsabilidade |
|---|---:|---|
| `ms-customer-bms-v1` | 8083 | Gerenciamento das regras de negócio, persistência e operações de abertura e consulta de clientes. |
| `ms-customer-api-v1` | 8082 | Exposição dos endpoints REST e comunicação com o microserviço de negócio. |

Fluxo simplificado da aplicação:

```text
Cliente
   |
   v
API REST (8082)
   |
   v
Microserviço BMS (8083)
   |
   v
Regras de negócio
   |
   v
Persistência / PostgreSQL
```

## Regras de negócio

A aplicação implementa as seguintes regras:

1. **CPF único:** não permitir o cadastro de clientes com CPF já existente.
2. **Maioridade:** permitir o cadastro somente para clientes com idade igual ou superior a 18 anos.
3. **Status inicial:** todo cliente criado deve receber o status `ACTIVE`.
4. **CPF duplicado:** retornar HTTP `409 Conflict` quando o CPF já estiver cadastrado.
5. **Cliente não encontrado:** retornar HTTP `404 Not Found` quando não existir solicitação de abertura para o CPF informado.
6. **Dados inválidos:** retornar HTTP `400 Bad Request` quando o cliente não atender às regras de validação, incluindo a idade mínima.

## Endpoints

### 1. Microserviço BMS

Base URL: `http://localhost:8083`

#### POST — Criar solicitação de abertura de conta

**Endpoint**

```http
POST /bms/v1/customer-account-opening
```

URL completa:

`http://localhost:8083/bms/v1/customer-account-opening`

**Body da requisição**

```json
{
  "name": "Joao",
  "cpf": "00000000000",
  "birthDate": "10/10/2000",
  "email": "joao@gmail.com"
}
```

**Resposta de sucesso — HTTP 201 Created**

```json
{
  "data": {
    "id": "133f4ec8-b342-46b1-8434-e947f6d6dbb6",
    "cpf": "00000000000",
    "name": "Joao",
    "status": "ACTIVE"
  },
  "status": "Created",
  "createdAt": "2026-10-10T10:00:00.0000000",
  "message": "Customer Account Opening created successfully"
}
```

O identificador e a data de criação são gerados pela aplicação conforme sua implementação. Os valores apresentados são exemplos.

#### GET — Consultar solicitação por CPF

**Endpoint**

```http
GET /bms/v1/customer-account-opening/{customerCpf}
```

URL completa de exemplo:

`http://localhost:8083/bms/v1/customer-account-opening/00000000000`

**Resposta de sucesso — HTTP 200 OK**

```json
{
  "data": {
    "id": "133f4ec8-b342-46b1-8434-e947f6d6dbb6",
    "cpf": "00000000000",
    "name": "Joao",
    "status": "ACTIVE"
  },
  "status": "Created",
  "createdAt": "2026-10-10T10:00:00.0000000",
  "message": "Customer Account Opening created successfully"
}
```

### 2. API REST

Base URL: `http://localhost:8082`

#### POST — Criar solicitação de abertura de conta

**Endpoint**

```http
POST /api/v1/customer-account-opening
```

URL completa:

`http://localhost:8082/api/v1/customer-account-opening`

**Body da requisição**

```json
{
  "name": "Joao",
  "cpf": "00000000000",
  "birthDate": "10/10/2000",
  "email": "joao@gmail.com"
}
```

**Resposta de sucesso — HTTP 201 Created**

A API deve retornar apenas o status HTTP `201 Created`, sem corpo de resposta.

#### GET — Consultar solicitação por CPF

**Endpoint**

```http
GET /api/v1/customer-account-opening/{customerCpf}
```

URL completa de exemplo:

`http://localhost:8082/api/v1/customer-account-opening/00000000000`

**Resposta de sucesso — HTTP 200 OK**

```json
{
  "data": {
    "id": "133f4ec8-b342-46b1-8434-e947f6d6dbb6",
    "cpf": "00000000000",
    "name": "Joao",
    "status": "ACTIVE"
  },
  "status": "Created",
  "createdAt": "2026-10-10T10:00:00.0000000",
  "message": "Customer Account Opening created successfully"
}
```

## Tratamento de erros

Os erros de negócio devem ser tratados tanto pelo microserviço BMS quanto pela API, preservando os códigos HTTP e as mensagens definidas para cada situação.

### HTTP 400 — Bad Request

Ocorre quando o cliente possui menos de 18 anos.

```json
{
  "message": "Customer must be at least 18 years old"
}
```

### HTTP 404 — Not Found

Ocorre quando não existe solicitação de abertura de conta para o CPF informado.

```json
{
  "message": "No account opening request found for CPF: 00000000000"
}
```

### HTTP 409 — Conflict

Ocorre quando o CPF já está sendo utilizado em um cadastro existente.

```json
{
  "message": "CPF is already used: 12311122233"
}
```

## Como executar

### Pré-requisitos

- JDK 21 instalado.
- Maven instalado ou Maven Wrapper disponível no projeto.
- PostgreSQL configurado e em execução.
- Configurações de conexão com o banco de dados definidas nos arquivos de configuração da aplicação.

### Executar o microserviço BMS

No diretório do projeto `ms-customer-bms-v1`, execute:

```bash
./mvnw spring-boot:run
```

No Windows, utilizando o Maven Wrapper:

```powershell
.\mvnw.cmd spring-boot:run
```

Verifique se a aplicação iniciou na porta `8083`.

### Executar a API

No diretório do projeto `ms-customer-api-v1`, execute:

```bash
./mvnw spring-boot:run
```

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Verifique se a API iniciou na porta `8082` e se consegue se comunicar com o microserviço BMS.

> Os comandos pressupõem que cada aplicação possua seu próprio `pom.xml` e que as portas estejam configuradas nos respectivos arquivos de propriedades.

## Testes

Para executar os testes automatizados de cada aplicação, utilize:

```bash
./mvnw test
```

No Windows:

```powershell
.\mvnw.cmd test
```

Os testes devem contemplar, entre outros cenários:

- Criação de cliente com sucesso.
- Consulta de cliente por CPF.
- Rejeição de CPF duplicado.
- Rejeição de cadastro de menor de idade.
- Retorno de `404 Not Found` para CPF inexistente.
- Retorno de `409 Conflict` para CPF duplicado.
- Retorno de `400 Bad Request` para idade inválida.
- Retorno de `201 Created` na criação pela API.

## Considerações finais

O projeto tem como objetivo disponibilizar operações de abertura e consulta de clientes por meio de uma API REST, mantendo as regras de negócio e a persistência concentradas no microserviço BMS.

A separação entre API e microserviço favorece a organização das responsabilidades, a manutenção do código e a evolução independente dos componentes.
