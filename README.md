# CantinaJá — Backend

Sistema de pedidos para cantina com carteira pré-paga. Este repositório é o **backend**: um projeto Maven multi-module onde cada épico é uma aplicação Spring Boot independente.

> Stack: **Java 25 · Spring Boot 4 · Spring Framework 7 · Maven · H2 (em memória) · Swagger/OpenAPI**

---

## Sumário

- [Módulos e portas](#módulos-e-portas)
- [Estrutura de pastas](#estrutura-de-pastas)
- [Pré-requisitos](#pré-requisitos)
- [Como rodar](#como-rodar)
- [O módulo `common` (tratamento de erros)](#o-módulo-common-tratamento-de-erros)
  - [O que é](#o-que-é)
  - [Como usar no seu código (passo a passo)](#como-usar-no-seu-código-passo-a-passo)
  - [O que o cliente recebe](#o-que-o-cliente-recebe)
  - [Tabela de status HTTP](#tabela-de-status-http-mais-usados)
- [Convenções do time](#convenções-do-time)

---

## Módulos e portas

Cada épico roda como uma aplicação separada, na sua própria porta.

| Módulo | Porta | Swagger | Console H2 |
|---|---|---|---|
| `cardapio` | 8081 | http://localhost:8081/swagger-ui.html | http://localhost:8081/h2-console |
| `carteira` | 8082 | http://localhost:8082/swagger-ui.html | http://localhost:8082/h2-console |
| `pedidos` | 8083 | http://localhost:8083/swagger-ui.html | http://localhost:8083/h2-console |
| `alunos` | 8084 | http://localhost:8084/swagger-ui.html | http://localhost:8084/h2-console |

Além dos quatro épicos, existe o módulo **`common`**: uma biblioteca compartilhada (não é uma aplicação) — veja a seção dedicada abaixo.

---

## Estrutura de pastas

```
cantinaja-backend/
├─ pom.xml                → POM raiz (parent + lista de módulos)
├─ common/                → biblioteca compartilhada (exceptions, handler)
├─ cardapio/              → épico A · porta 8081
├─ carteira/              → épico B · porta 8082
├─ pedidos/               → épico C · porta 8083
└─ alunos/                → épico D · porta 8084
```

Dentro de cada épico, o código segue a arquitetura em camadas (MVC).

```
<módulo>/src/main/java/br/com/cantinaja/<módulo>/
├─ <Módulo>Application.java   → o "botão de ligar" (main)
├─ config/                    → configurações (ex.: CORS)
├─ controller/                → recebe as requisições HTTP (a "porta de entrada")
├─ service/                   → onde moram as REGRAS de negócio
├─ repository/                → acesso ao banco de dados
├─ model/                     → entidades (as tabelas)
└─ dto/                       → objetos de entrada/saída da API
```

Regra de ouro: **cada squad só mexe na pasta do seu módulo.**

---

## Pré-requisitos

- **Java 25** (JDK) — confira com `java -version`
- **Maven** — confira com `mvn -version`

> O passo a passo completo de instalação do ambiente está no GitBook, em "Preparando o ambiente do backend".

---

## Como rodar

Na raiz do projeto, para compilar tudo de uma vez (inclui o `common`):

```bash
mvn clean install
```

Para rodar **um** módulo (cada um sobe na sua porta):

```bash
mvn spring-boot:run -pl cardapio
```

Para o fluxo completo funcionar, os módulos que conversam entre si precisam estar rodando ao mesmo tempo (cada um em um terminal).

---

## O módulo `common` (tratamento de erros)

### O que é

O `common` é uma **biblioteca compartilhada** por todos os módulos. Hoje ele cuida do **tratamento de erros** de forma padronizada: quando uma regra de negócio é quebrada, a API responde sempre no mesmo formato, o **ProblemDetail** (um padrão da internet, a RFC 9457).

A grande vantagem: **você escreve o tratamento de erro uma vez, e todos os módulos ganham automaticamente.** Não precisa configurar nada no seu módulo além de já ter o `common` como dependência (o que já está pronto).

Ele tem duas peças que interessam pra você no dia a dia:

**1. `BusinessException`** — a exception que você lança quando uma regra é violada. Ela carrega o status HTTP e a mensagem.

```java
public class BusinessException extends RuntimeException {

    private final HttpStatus status;

    public BusinessException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
```

**2. `GlobalExceptionHandler`** — o "porteiro" que captura essas exceptions e transforma na resposta ProblemDetail. **Você não precisa mexer nele** — ele já funciona sozinho em todos os módulos.

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ProblemDetail handleBusiness(BusinessException ex) {
        return ProblemDetail.forStatusAndDetail(ex.getStatus(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex) {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno inesperado");
    }
}
```

### Como usar no seu código (passo a passo)

A ideia é simples: **no `Service`, quando a regra de negócio for violada, você lança uma `BusinessException`.** O resto acontece automaticamente.

**Passo 1 — importe a exception** no topo do seu service:

```java
import br.com.cantinaja.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
```

**Passo 2 — lance a exception quando a regra for quebrada**, dentro do seu `Service`:

```java
@Service
public class MeuService {

    // dependências (ex.: o repository) injetadas pelo construtor

    public void minhaOperacao(/* dados de entrada */) {

        // Quando a SUA regra de negócio for violada, lance a exception:
        if (/* condição que viola a regra */) {
            throw new BusinessException(HttpStatus.CONFLICT, "Mensagem explicando o problema");
        }

        // ... caso contrário, segue o fluxo normal ...
    }
}
```

**Pronto.** Você **não** precisa escrever `try/catch` no controller, nem montar a resposta de erro na mão. Quando o `throw` acontece, o `GlobalExceptionHandler` (lá no `common`) captura e devolve a resposta certa, com o status certo.

**Qual status usar?** Escolha o que combina com a regra (veja a [tabela abaixo](#tabela-de-status-http-mais-usados)). Alguns exemplos do nosso domínio:

```java
// Conflito de estado ou duplicidade → CONFLICT (409)
throw new BusinessException(HttpStatus.CONFLICT, "Descrição do conflito");

// Dado inválido ou regra de valor → BAD_REQUEST (400)
throw new BusinessException(HttpStatus.BAD_REQUEST, "Descrição do dado inválido");

// Recurso não encontrado → NOT_FOUND (404)
throw new BusinessException(HttpStatus.NOT_FOUND, "Recurso não encontrado");
```

### O que o cliente recebe

Quando a regra é violada, a API responde com o status HTTP correto e um corpo JSON padronizado. Por exemplo, para uma operação que resultou em conflito (`409`):

**Resposta:** `409 Conflict`

```json
{
  "type": "about:blank",
  "title": "Conflict",
  "status": 409,
  "detail": "Descrição do problema que aconteceu"
}
```

O frontend consegue ler o `status` para saber o que aconteceu e o `detail` para mostrar a mensagem ao usuário. Como todas as APIs do projeto usam o mesmo formato, o frontend trata os erros de todos os módulos da mesma maneira.

### Tabela de status HTTP mais usados

| Status | Constante Java | Quando usar |
|---|---|---|
| 400 | `HttpStatus.BAD_REQUEST` | Dado inválido ou valor fora do permitido |
| 404 | `HttpStatus.NOT_FOUND` | O recurso pedido não existe |
| 409 | `HttpStatus.CONFLICT` | Conflito de estado ou duplicidade |
| 422 | `HttpStatus.UNPROCESSABLE_ENTITY` | Requisição entendida, mas viola uma regra de negócio |

> Dica: para o caminho de sucesso, quem define o status é o **controller** (`201 Created` ao criar, `200 OK` ao consultar/atualizar). A `BusinessException` é só para o caminho de erro.

---

## Convenções do time

- **Cada squad só mexe na sua pasta de módulo.**
- **Regra de negócio mora no `Service`** — o controller só cuida do HTTP.
- **Título de Pull Request prefixado com o módulo**, ex.: `[Cardápio] A1 - Cadastrar item`.
- **O POM raiz e o módulo `common` só mudam com revisão de um instrutor.**

Documentação completa do projeto (histórias, arquitetura, git flow, etc.) no GitBook do time.
