# Instruções de Instalação e Execução do Sistema

Este guia explica como executar o sistema utilizando Docker e Docker Compose.

## Pré-requisitos

Antes de começar, certifique-se de que você tem o **Docker** e o **Docker Compose** instalados na sua máquina. Você pode encontrar as instruções de instalação nos links abaixo:

* **Docker:** [Instruções de Instalação do Docker](https://docs.docker.com/get-docker/)
* **Docker Compose:** [Instruções de Instalação do Docker Compose](https://docs.docker.com/compose/install/)

## Passo 1: Clone o Repositório do Projeto

Primeiro, clone o repositório do seu projeto para a sua máquina local utilizando o Git:

```bash
git clone git@github.com:ricajust/monolito.git
cd edugo_tcc
```


# Passo 2: Crie o Arquivo docker-compose.yml

Na raiz do diretório do seu projeto, crie um arquivo chamado docker-compose.yml com o seguinte conteúdo:
```YAML

version: '3.8'

services:
  db:
    image: ricartoons/edugo-db-with-data:latest
    container_name: edugo_db
    environment:
      POSTGRES_USER: ricajust
      POSTGRES_PASSWORD: '123'
      POSTGRES_DB: edugo
    ports:
      - "5433:5432" # Mapeia a porta 5432 do container para a porta 5433 da sua máquina local
    volumes:
      - edugo_db_data:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U ricajust -d edugo"]
      interval: 5s
      timeout: 5s
      retries: 5

  app:
    image: ricartoons/edugo-app:latest
    container_name: edugo_app
    ports:
      - "8080:8080" # Mapeia a porta 8080 do container para a porta 8080 da sua máquina local
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/edugo
      SPRING_DATASOURCE_USERNAME: ricajust
      SPRING_DATASOURCE_PASSWORD: 123
    depends_on:
      db:
        condition: service_healthy

volumes:
  edugo_db_data:
```
### Observações importantes:

* Certifique-se de que as portas mapeadas (na seção ports de cada serviço) atendem às suas necessidades. A porta 5433:5432 significa que a porta 5432 dentro do container do banco de dados estará acessível na porta 5433 da sua máquina local. A porta da aplicação segue uma lógica similar.
* As variáveis de ambiente para o banco de dados (POSTGRES_USER, POSTGRES_PASSWORD, POSTGRES_DB) e para a aplicação (SPRING_DATASOURCE_URL, SPRING_DATASOURCE_USERNAME, SPRING_DATASOURCE_PASSWORD) devem corresponder às configurações que você utilizou no seu sistema.


# Passo 3: Inicie o Sistema com Docker Compose

Com o arquivo **docker-compose.yml** criado na raiz do seu projeto, abra o terminal e execute o seguinte comando nesta pasta:

```Bash
docker-compose up -d
```

Este comando fará com que o Docker Compose baixe as imagens do Docker Hub (caso ainda não estejam presentes localmente), crie os containers e os inicie em segundo plano.

Observação: Ao executar **docker-compose up -d**, o Docker automaticamente baixará as imagens **ricartoons/edugo-app:latest** e **ricartoons/edugo-db-with-data:latest** do Docker Hub, caso elas não estejam encontradas localmente na sua máquina. Certifique-se de que você tenha uma conexão com a internet durante este processo.

# Passo 4: Acesse a Aplicação

Depois que os containers estiverem rodando, você poderá acessar a sua aplicação através do seu navegador ou de uma ferramenta como o Insomnia, utilizando o endereço e a porta que você configurou no arquivo **docker-compose.yml**. No exemplo acima, a aplicação estará disponível em:

```
http://localhost:8080
```

O banco de dados PostgreSQL estará rodando no container **edugo_db**. Se você precisar acessar o banco de dados diretamente (por exemplo, com um cliente de banco de dados), você pode usar a porta que você mapeou na sua máquina local (no exemplo, **localhost:5433**) com as credenciais definidas nas variáveis de ambiente.

# Próximos Passos

* Para parar o sistema, execute o comando **docker-compose down** no mesmo diretório.
* Você pode verificar os logs dos containers com os comandos **docker-compose logs app** (para a aplicação) e **docker-compose logs db** (para o banco de dados).