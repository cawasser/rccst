FROM postgres
ENV POSTGRES_USER postgres
ENV POSTRGRES_PASSWORD Password
ENV POSTGRES_DB rccst

RUN chown -R postgres:postgres /var/lib/postgresql/data && \
    chown -R postgres:postgres /var/run/postgresql/ && \
    chmod -R 777 /var/run/postgresql/ && \
    chmod -R 777 /var/lib/postgresql/data
EXPOSE 5432

COPY --chown=postgres:postgres userTable.sql /docker-entrypoint-initdb.d/