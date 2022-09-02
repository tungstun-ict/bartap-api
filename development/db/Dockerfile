FROM postgres:13-alpine

COPY v2/schema-v2.sql /data/schema-v2.sql

COPY create_users.sh /docker-entrypoint-initdb.d/01-create_users.sh
COPY create_dbs.sh /docker-entrypoint-initdb.d/02-create_dbs.sh
COPY import_schema.sh /docker-entrypoint-initdb.d/03-create_schema.sh

RUN sed -i -e 's/\r$//' /docker-entrypoint-initdb.d/01-create_users.sh && \
    sed -i -e 's/\r$//' /docker-entrypoint-initdb.d/02-create_dbs.sh && \
    sed -i -e 's/\r$//' /docker-entrypoint-initdb.d/03-create_schema.sh

