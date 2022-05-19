FROM postgres
ENV POSTGRES_USER 1001710000
ENV POSTRGRES_PASSWORD Password
ENV POSTGRES_DB rccst

RUN chown -R 1001710000:1001710000 /var/lib/postgresql/data && \
    chown -R 1001710000:1001710000 /var/run/postgresql/ && \
    chmod -R 777 /var/run/postgresql/ && \
    chmod -R 777 /var/lib/postgresql/data
EXPOSE 5432

COPY --chown=1001710000:1001710000 userTable.sql /docker-entrypoint-initdb.d/