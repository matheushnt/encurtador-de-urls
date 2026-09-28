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
- [Como executar a aplicação](#como-executar-a-aplicação)
- [Documentação da API](#documentação-da-api)
- [Tecnologias utilizadas](#tecnologias-utilizadas)
- [Decisões tomadas](#decisões-tomadas)
- [Observações](#observações)

## Pré-requisitos
- **Linux:** Docker Engine e Docker Compose v2;
- **macOS:** Docker Desktop instalado e em execução;
- **Windows:** Docker Desktop instalado e em execução, WSL 2 instalado e integração do Docker Desktop habilitada para a distribuição Linux;
- Git (opcional, caso queira clonar o repositório).

## Como executar a aplicação
No Linux, com Docker Engine e Docker Compose instalados, execute os comandos abaixo no terminal, na raiz do projeto.

No macOS, inicie o Docker Desktop e execute os mesmos comandos no Terminal, na raiz do projeto. No Windows, abra o terminal da distribuição WSL integrada ao Docker Desktop e execute os comandos nela, não no PowerShell.

Crie o arquivo `.env`:
```bash
cp .env.example .env
```

Abra `.env` e defina `POSTGRES_PASSWORD` e `JWT_SECRET`. O Docker Compose carrega esse arquivo automaticamente.

Na raiz do projeto, construa a imagem e inicie a aplicação e o banco de dados:
```bash
docker compose up --build -d
```

Acompanhe os logs da aplicação:
```bash
docker compose logs -f application
```

Quando os logs indicarem que a aplicação iniciou, pressione `Ctrl+C` para encerrar o acompanhamento. Os containers continuarão em execução.

Acesse o Swagger UI em `http://localhost:8080/swagger-ui/index.html` para consultar e testar os endpoints da API. Para parar os serviços sem remover os dados do banco:
```bash
docker compose down
```

Os dados do PostgreSQL são mantidos no volume Docker `postgres-data`.

## Documentação da API
Com a aplicação em execução, acesse o Swagger UI em `http://localhost:8080/swagger-ui/index.html`. Ele lista as rotas, parâmetros, dados esperados, respostas e requisitos de autenticação. Também permite enviar requisições pela própria interface para testar a API.

A especificação OpenAPI em JSON está disponível em `http://localhost:8080/api-docs`.

## Tecnologias utilizadas
- **Java 21** - Linguagem de programação;
- **Spring Boot 4.0.7** - Framework principal;
- **Spring Data JPA** - Persistência e acesso aos dados;
- **Spring Validation** - Validação das requisições;
- **Spring Security** - Autenticação e autorização;
- **PostgreSQL 17** - Banco de dados relacional;
- **Flyway** - Gerenciamento de migrations e versionamento do banco de dados;
- **Maven** - Gerenciador de dependências;
- **Docker e Docker Compose** - Build e execução da aplicação e do banco;
- **Lombok** - Biblioteca para redução de código boilerplate;
- **OpenAPI/Swagger** - Documentação da API.

## Decisões tomadas
### Geração do código curto
A chave primária da entidade é gerada pelo banco e posteriormente convertido para Base62 para formar o `shortCode`. Dessa forma, não é necessário implementar uma estratégia de detecção e tratamento de colisões.

Porém, uma consequência dessa abordagem é que os códigos são previsíveis, já que seguem a sequência dos identificadores. Para este projeto, não vejo problema, no entanto, em produção, essa consequência estaria expondo a quantidade de links curtos na base de dados. Uma solução que pensei mas que deve ser analisada é iniciar a contagem dos IDs com um número alto por exemplo 50 mil. Outra solução poderia ser é utilizar `NanoID`, porém, introduziria o problema de possíveis colisões.

### HTTP 302 Redirecionamento
Optei por utilizar o status `302 Found` em vez de `301 Moved Permanently`. A escolha evita que o navegador mantenha o redirecionamento em cache e continue acessando a URL original mesmo depois de o link ter expirado ou sido removido

## Observações
- Os dados do PostgreSQL são persistidos no volume Docker `postgres-data`;
- A documentação detalhada dos endpoints está disponível no **Swagger**;
- A expiração dos links ocorre após **7 dias**;
- O código curto é gerado automaticamente e não permite alias personalizado;
- A previsibilidade dos códigos **Base62** permanece como uma característica conhecida da implementação.
- Na raiz do projeto, há a pasta `postman` que inclui a coleção de requisições. Você pode importar no Postman para fazer as requisições de maneira mais fácil.
