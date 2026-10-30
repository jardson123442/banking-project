# Análise de acoplamento e oportunidades de melhoria

## Objetivo

Esta análise examina a estrutura atual do `ms-customer-bms-v1` e propõe mudanças graduais para **reduzir o acoplamento** entre API, aplicação, domínio e persistência.

A intenção é facilitar os testes, a evolução das regras de negócio e uma eventual substituição de tecnologias, sem exigir a reescrita de partes não relacionadas do serviço.

## Visão atual

O fluxo principal de abertura de conta está organizado aproximadamente da seguinte forma:

```text
Controller
  → Facade
    → Disassembler / Mapper
    → Service
      → Repository Spring Data
        → Entidade JPA
    → Assembler
  → Response DTO
```

Há uma separação inicial por pacotes (`api`, `application`, `domain` e `persistence`), mas as dependências entre eles ainda atravessam as fronteiras dessas camadas.

## Principais pontos de acoplamento

### 1. Serviço de domínio depende diretamente da persistência

O arquivo `domain/service/CustomerAccountOpeningServiceImpl.java` importa o repositório Spring Data e a entidade JPA. O serviço também constrói a entidade de persistência e converte essa entidade no resultado da operação.

**Impacto:** as regras de negócio ficam vinculadas ao modelo e à tecnologia de banco de dados. Alterar a persistência ou testar o domínio isoladamente torna-se mais difícil.

**Recomendação:** definir uma porta de saída na camada de aplicação ou domínio, por exemplo, `CustomerAccountRepository`, com operações de negócio como `findByCpf` e `save`. Implementar essa porta em `persistence`, utilizando o repositório Spring Data internamente. A entidade JPA e o repositório Spring Data devem permanecer como detalhes do adaptador.

### 2. A camada de aplicação está acoplada aos contratos HTTP

`CustomerAccountOpeningFacade` recebe e devolve `CustomerAccountOpeningCreateRequestDTO` e `CustomerAccountOpeningCreateResponseDTO`.

Esses DTOs pertencem à borda HTTP, mas aparecem na interface da aplicação.

**Impacto:** mudanças no formato da API podem exigir alterações na aplicação, mesmo quando as regras de negócio não mudaram. Também fica mais difícil reutilizar o caso de uso por outro canal, como uma fila ou um processo agendado.

**Recomendação:** utilizar modelos próprios da aplicação, como `CreateCustomerAccountCommand` e `CustomerAccountResult`. O controller converte o DTO HTTP para o comando, chama o caso de uso e transforma o resultado em DTO de resposta.

### 3. O modelo de resultado expõe um tipo da persistência

`CustomerAccountOpeningResultVO` utiliza `CustomerStatus`, que está localizado em `persistence/entity`.

**Impacto:** o modelo que atravessa o caso de uso depende de um tipo localizado na camada de persistência. Uma alteração na entidade ou no enum pode se propagar para outras camadas.

**Recomendação:** mover o status para um tipo neutro do domínio ou da aplicação. O adaptador de persistência deve realizar a conversão entre esse tipo e a representação necessária no banco de dados.

### 4. Exceções de domínio e tratamento HTTP estão misturados

`GlobalExceptionHandler` está localizado no pacote `domain/vo/exceptions` e importa tipos do Spring Web.

Assim, sua localização sugere que pertence ao domínio, mas seu conteúdo representa um adaptador HTTP.

**Impacto:** o domínio fica associado à infraestrutura web, e a organização dificulta a reutilização das exceções de negócio em outros adaptadores.

**Recomendação:** manter as exceções de negócio sem dependências do Spring e mover o `@RestControllerAdvice` para um pacote da API, por exemplo, `api/exception`. O adaptador HTTP é responsável por converter erros de aplicação e domínio em status e respostas HTTP.

### 5. O mapper MapStruct declara integração com Spring, mas é acessado como singleton estático

`CustomerAccountOpeningMapper` declara `componentModel = "spring"`, porém `CustomerDisassembler` utiliza `CustomerAccountOpeningMapper.INSTANCE`.

**Impacto:** o acesso estático ignora a instância gerenciada pelo Spring. Isso reduz a consistência da injeção de dependências e pode dificultar a inclusão futura de dependências ou configurações no mapper.

**Recomendação:** escolher uma única estratégia. Se a intenção for utilizar Spring, injetar o mapper no `CustomerDisassembler` e remover o `INSTANCE`. Alternativamente, se a intenção for não utilizar Spring nesse mapper, remover `componentModel = "spring"`.

### 6. A conversão de API para domínio está localizada em uma classe da API que depende de um mapper da aplicação

`api/disassembler/CustomerDisassembler.java` conhece o DTO HTTP, o VO e o mapper localizado em `application`.

Além disso, recebe os parâmetros `message` e `status`, mas não os utiliza.

**Impacto:** responsabilidades e dependências atravessam camadas, e parâmetros sem utilização deixam a interface mais confusa.

**Recomendação:** manter a conversão do contrato HTTP para o comando da aplicação na borda da API ou em um mapper específico dessa camada. Remover os parâmetros não utilizados e manter o mapper focado em uma conversão explícita.

### 7. A resposta da API utiliza um campo genérico para transportar o resultado

`CustomerAccountOpeningCreateResponseDTO` declara o campo `data` como `Object` e `transient`.

**Impacto:** o contrato não expressa o tipo real do conteúdo e perde garantias de compilação. O formato efetivo da resposta fica mais difícil de compreender e documentar.

**Recomendação:** tipar `data` com um DTO de resposta específico, como `CustomerAccountDataResponseDTO`. Esse DTO deve conter somente os campos públicos que a API pretende expor.

### 8. Utilitário de logging compartilhado espalha dependências transversais

`LogUtils` é utilizado em classes de API, aplicação e domínio. Ele encapsula um logger estático, a leitura de variáveis de ambiente e a formatação com cores ANSI.

**Impacto:** o código de negócio depende de uma implementação global de logging, e a configuração é determinada fora do mecanismo padrão do Spring/SLF4J. A instrumentação também fica misturada à lógica da aplicação.

**Recomendação:** preferir SLF4J diretamente nas classes que precisam registrar eventos, seguindo um padrão consistente de mensagens e dados estruturados. Centralizar a configuração de nível e formato na configuração da aplicação, sem adicionar uma abstração própria sem necessidade concreta.

### 9. Constantes de contextos distintos estão agrupadas em uma classe ampla

`application/ApplicationConstants.java` mistura idade mínima, mensagens de logging e constantes relacionadas a outros fluxos e domínios.

**Impacto:** uma classe global concentra conceitos não relacionados e incentiva dependências indevidas entre funcionalidades.

**Recomendação:** manter as constantes próximas aos conceitos que as utilizam. Para valores que variam por ambiente, utilizar configurações tipadas do Spring. Para regras de negócio, preferir nomes e tipos definidos no domínio ou na aplicação.

### 10. Regras de data dependem de texto e do relógio do sistema

O serviço recebe `birthDate` como `String`, interpreta um formato textual e chama `LocalDate.now()` diretamente.

**Impacto:** o formato de transporte e a regra de negócio ficam unidos. O uso direto do relógio do sistema dificulta a realização de testes determinísticos de idade e de datas-limite.

**Recomendação:** validar e converter a data na borda da API para um tipo `LocalDate`. Se a regra precisar ser testada com datas controladas, utilizar um `Clock` injetável na aplicação ou no domínio.

## Arquitetura-alvo sugerida

Uma evolução incremental pode aproximar o fluxo do seguinte desenho:

```text
API
  ├── Controller
  ├── DTOs
  ├── Validação
  └── Exception Handler (HTTP)

  ↓

Aplicação
  ├── Casos de uso
  ├── Comandos
  ├── Resultados
  └── Portas

  ↓

Domínio
  ├── Regras de negócio
  └── Tipos de negócio


Persistência (Adaptador JPA)
  ├── Implementa as portas da aplicação/domínio
  ├── Utiliza entidades JPA
  └── Utiliza repositórios Spring Data


Configuração Spring
  └── Conecta os casos de uso aos adaptadores
```

A regra de dependência desejada é: os adaptadores dependem das abstrações internas; o domínio e os casos de uso não dependem de Spring MVC, Spring Data ou entidades JPA.

## Plano de evolução recomendado

1. **Separar o contrato HTTP dos casos de uso:** introduzir comandos e resultados da aplicação e ajustar o controller e a facade. Manter o mesmo JSON e os mesmos códigos HTTP.

2. **Criar a porta de persistência:** substituir a dependência direta do serviço no repositório Spring Data por uma interface e implementar essa interface no pacote de persistência.

3. **Isolar modelos e exceções:** retirar tipos da persistência dos resultados utilizados fora do adaptador e mover o tratamento de exceções HTTP para a API.

4. **Consolidar mapeamentos e logging:** escolher uma estratégia de injeção do MapStruct, remover parâmetros não utilizados e padronizar o logging.

5. **Revisar validações e datas:** estabelecer a fronteira entre validação HTTP e regras de domínio, além de tornar o cálculo de idade determinístico nos testes.

Cada etapa deve preservar o comportamento público da API.

Não é necessário introduzir uma arquitetura mais complexa ou criar abstrações para componentes que ainda não possuem mais de uma implementação. O objetivo é inverter as dependências onde atualmente elas atravessam as camadas.

## Testes que devem acompanhar a mudança

- **Testes unitários dos casos de uso:** utilizar implementações simuladas das portas para validar regras como CPF duplicado, idade mínima e busca de clientes.

- **Testes do adaptador de persistência:** validar o mapeamento entre os modelos internos e as entidades JPA, além das restrições do banco de dados.

- **Testes de controller e contrato:** garantir que os DTOs, os status HTTP e o formato das respostas permaneçam compatíveis.

- **Testes para condições de concorrência:** validar o comportamento quando a restrição de unicidade do CPF é violada simultaneamente por operações concorrentes.

Também vale revisar o tratamento de `DataIntegrityViolationException` em `CustomerAccountOpeningServiceImpl`: atualmente, essa exceção é convertida em `CustomerAccountNotFoundException`.

Uma violação de unicidade causada por concorrência deveria ser traduzida para um erro coerente com conflito ou CPF duplicado, mantendo a conversão para HTTP no adaptador da API.

## Critérios para avaliar o resultado

- [ ] O domínio e os casos de uso não importam classes de `persistence`, Spring Data ou Spring Web.
- [ ] A aplicação não recebe nem retorna DTOs exclusivos da API.
- [ ] Entidades JPA não atravessam o adaptador de persistência.
- [ ] Os testes das regras de negócio podem ser executados sem inicializar o contexto Spring ou o banco de dados.
- [ ] O contrato HTTP permanece coberto por testes e compatível com o comportamento esperado.