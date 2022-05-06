FROM postgres
ENV POSTGRES_USER postgres
ENV POSTRGRES_PASSWORD Password
ENV POSTGRES_DB rccst

RUN chown -R 1001:1001 /var/lib/postgresql/data && \
    chown -R 1001:1001 /var/run/postgresql/ && \
    chmod -R g=u /var/run/postgresql/ && \
    chmod -R g=u /var/lib/postgresql/data
EXPOSE 5432

COPY --chown=1001:1001 userTable.sql /docker-entrypoint-initdb.d/