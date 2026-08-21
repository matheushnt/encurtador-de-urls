# Encurtador de URLs
**API REST** para criação e gerenciamento de URLs encurtadas, desenvolvida com **Java** e **Spring Boot**.

A aplicação recebe uma URL longa, gera um código curto utilizando **contador incremental** e **Base62**. Além disso, permite redirecionar acessos para a URL original, Os links possuem expiração automática e podem ser removidos por usuários autenticados.

## Descrição
O Encurtador de URLs permite:
- Criar URLs encurtadas;
- Redirecionar uma URL curta para sua URL original;
- Consultar os metadados de uma URL encurtada;
- Remover URLs encurtadas;
- Expirar automaticamente os links após 7 dias;
- Validar as URLs recebidas, aceitando apenas os schemes `http` e `https`;
- Autenticar usuários utilizando **Spring Security**;
- Restringir operações de gerenciamento aos usuários autenticados e autorizados.

A geração do código curto utiliza o identificador incremental da entidade convertido para **Base62**, evitando colisões por construção.

O redirecionamento utiliza `HTTP 302 Found`, permitindo que o servidor continue sendo consultado mesmo após alterações no estado do link, como expiração ou remoção.

## Sumário
- [Pré-requisitos](#pré-requisitos)
- [Como gerar o Build](#como-gerar-o-build)
- [Como executar a aplicação](#como-executar-a-aplicação)
- [Documentação da API](#documentação-da-api)
- [Tecnologias utilizadas](#tecnologias-utilizadas)
- [Decisões tomadas](#decisões-tomadas)
- [Observações](#observações)

## Pré-requisitos
Antes de começar, certifique-se de ter instalado:
- Java 21 ou superior;
- Maven 3.8+ ou use o Maven Wrapper já incluso no projeto;
- Git (opcional, caso queira clonar o repositório).

## Como gerar o Build
### Usando o Maven Wrapper
Como o projeto inclui o Maven Wrapper, você não precisa instalar o Maven globalmente, a menos que você queira.
#### No Linux/MacOS
```bash
./mvnw clean package
```
#### No Windows
```bash
./mvnw.cmd clean package
```
### Usando o Maven instalado globalmente
Com o Maven instalado, basta executar:
```bash
mvn clean package
```
---
Estes comandos irão:
- Limpar a pasta `/target`;
- Compilar o código-fonte;
- Gerar o arquivo `.jar`.

## Como executar a aplicação
### Usando o Maven (recomendado em desenvolvimento)
#### No Linux/MacOS
```bash
./mvnw spring-boot:run
```
#### No Windows
```bash
./mvnw.cmd spring-boot:run
```
#### Usando o Maven instalado globalmente
```bash
mvn spring-boot:run
```
### Usando o arquivo `.jar` gerado
Após realizar o build da aplicação, você pode executar o arquivo `.jar` diretamente:
```bash
java -jar target/url_shortener-1.0.0.jar
```
---
A aplicação estará disponível em `http://localhost:8080`.

## Documentação da API
A API é documentada utilizando **OpenAPI/Swagger**. Após iniciar a aplicação, a documentação interativa pode ser acessada em: `http://localhost:8080/swagger-ui/index.html`. A especificação **OpenAPI** também pode ser consultada em: `http://localhost:8080/api-docs`.

O Swagger UI contém a documentação detalhada dos endpoints, incluindo:
- Parâmetros;
- Headers;
- Modelos de requisição;
- Modelos de resposta;
- Códigos HTTP;
- Requisitos de autenticação;
- Possibilidade de executar as requisições diretamente pela interface.

## Tecnologias utilizadas
- **Java 21** - Linguagem de programação;
- **Spring Boot 4.0.7** - Framework principal;
- **Spring Data JPA** - Persistência e acesso aos dados;
- **Spring Validation** - Validação das requisições;
- **Spring Security** - Autenticação e autorização
- **H2 Database** - Banco de dados em memória;
- **Flyway** - Gerenciamento de migrations e versionamento do banco de dados;
- **Maven** - Gerenciador de dependências;
- **Lombok** - Biblioteca para redução de código boilerplate;
- **OpenAPI/Swagger** - Documentação da API.

## Decisões tomadas
### Geração do código curto
A chave primária da entidade é gerada pelo banco e posteriormente convertido para Base62 para formar o `shortCode`. Dessa forma, não é necessário implementar uma estratégia de detecção e tratamento de colisões.

Porém, uma consequência dessa abordagem é que os códigos são previsíveis, já que seguem a sequência dos identificadores. Para este projeto, não vejo problema, no entanto, em produção, essa consequência estaria expondo a quantidade de links curtos na base de dados. Uma solução que pensei mas que deve ser analisada é iniciar a contagem dos IDs com um número alto por exemplo 50 mil. Outra solução poderia ser é utilizar `NanoID`, porém, introduziria o problema de possíveis colisões.

### HTTP 302 Redirecionamento
Optei por utilizar o status `302 Found` em vez de `301 Moved Permanently`. A escolha evita que o navegador mantenha o redirecionamento em cache e continue acessando a URL original mesmo depois de o link ter expirado ou sido removido

## Observações
- A aplicação armazena os registros em memória, ou seja, não persiste em disco;
- Ao reiniciar a aplicação, todos os registros adicionados serão perdidos;
- A documentação detalhada dos endpoints está disponível no **Swagger**;
- A expiração dos links ocorre após **7 dias**;
- O código curto é gerado automaticamente e não permite alias personalizado;
- A previsibilidade dos códigos **Base62** permanece como uma característica conhecida da implementação.
- Na raiz do projeto, há a pasta `postman` que inclui a coleção de requisições. Você pode importar no Postman para fazer as requisições de maneira mais fácil.
