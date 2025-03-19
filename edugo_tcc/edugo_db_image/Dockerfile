FROM postgres:16-alpine

COPY dump_with_data.sql /docker-entrypoint-initdb.d/

# Opcional: Defina variáveis de ambiente padrão se precisar de um usuário específico
ENV POSTGRES_USER ricajust
ENV POSTGRES_PASSWORD '123'
ENV POSTGRES_DB edugo