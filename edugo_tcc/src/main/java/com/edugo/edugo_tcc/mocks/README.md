# DOCKER COMPOSE
Para importar a aplicação e o banco de dados, baixe os dois arquivos *edugo-app.tar* e *edugo-db.tar* e na sequência execute:
```
docker load -i edugo_app.tar
docker load -i edugo_db.tar
```

# IMPORTAR O DUMP
Para importar o dump.sql, utilize o comando abaixo, mas certifique-se que você esteja no mesmo diretório do dump.sql.
```
docker exec -it edugo_db psql -U ricajust -d edugo -f /dump.sql
```

### PROBLEMAS PARA FAZER A IMPORTAÇÃO DO DUMP
Se ao tentar fazer a importação do arquivo dump.sql você tiver um erro semelhante a este:

```
psql:/dump.sql:43: ERROR:  relation "aluno" already exists
ALTER TABLE
psql:/dump.sql:57: ERROR:  relation "cobranca" already exists
ALTER TABLE
psql:/dump.sql:74: ERROR:  relation "desempenho" already exists
ALTER TABLE
psql:/dump.sql:90: ERROR:  relation "disciplina" already exists
ALTER TABLE
psql:/dump.sql:104: ERROR:  relation "disciplina_seq" already exists
ALTER SEQUENCE
psql:/dump.sql:117: ERROR:  relation "funcionario" already exists
ALTER TABLE
psql:/dump.sql:132: ERROR:  relation "matricula" already exists
ALTER TABLE
psql:/dump.sql:146: ERROR:  relation "matricula_seq" already exists
ALTER SEQUENCE
psql:/dump.sql:161: ERROR:  relation "pagamento" already exists
ALTER TABLE
psql:/dump.sql:174: ERROR:  relation "professor" already exists
ALTER TABLE
psql:/dump.sql:196: ERROR:  relation "usuario" already exists
ALTER TABLE
psql:/dump.sql:238: ERROR:  insert or update on table "aluno" violates foreign key constraint "fkc8wsngo14dwn23nvgsty37bfx"
DETAIL:  Key (id)=(19094345-a2da-465b-aa42-246be6199151) is not present in table "usuario".
psql:/dump.sql:630: ERROR:  duplicate key value violates unique constraint "ukqo6uriwb3habyl42n424f9uep"
DETAIL:  Key (pagamento_id)=(2ac5dbc8-617a-4c75-9a01-919d95492067) already exists.
CONTEXT:  COPY cobranca, line 2
```


Você precisará limpar a base antes da importação. Para isso use o comando dentro do psql:

```
# Entrar no banco de dados postgres pelo psql
docker exec -it edugo_db psql -U ricajust -d edugo

# Muda para o banco de dados postgres
\c postgres;

# remove o banco de dados edugo se existir
DROP DATABASE edugo;
```

#### Caso não consiga 'Dropar' a base, por conta deste erro:
```
postgres=# DROP DATABASE edugo;
ERROR:  database "edugo" is being accessed by other users
DETAIL:  There are 10 other sessions using the database.
```

Tente esses comandos. Isso ocorre, pois há mais usuários (você) conectado ao banco, o que impede a importação.

#### 1 - Impeça novas conexões ao banco de dados edugo
```
ALTER DATABASE edugo WITH ALLOW_CONNECTIONS = false;
```

#### 2 - Force a terminação de todas as sessões existentes conectadas ao banco de dados edugo
```
SELECT pg_terminate_backend(pid)
FROM pg_stat_activity
WHERE datname = 'edugo';
```

#### 3 - Tente excluir o banco de dados novamente
```
DROP DATABASE edugo;
```

#### 4 - Permita novas conexões ao banco de dados postgres novamente (para futuras operações)
```
ALTER DATABASE postgres WITH ALLOW_CONNECTIONS = true;
```

#### 5 - Crie o banco de dados novamente
```
CREATE DATABASE edugo;
```

#### 6 - Importe os dados do arquivo dump.sql
```
# Execute esse comando fora do psql (\q) para sair do psql
docker exec -it edugo_db psql -U ricajust -d edugo -f /dump.sql
```