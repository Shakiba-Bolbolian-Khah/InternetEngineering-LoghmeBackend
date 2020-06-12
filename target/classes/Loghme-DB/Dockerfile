FROM mysql:latest
ENV MYSQL_ROOT_PASSWORD=MirHamed2495
ENV MYSQL_DATABASE=Loghme
ADD Loghme.sql /docker-entrypoint-initdb.d
ENTRYPOINT ["docker-entrypoint.sh"]
EXPOSE 3306
CMD ["mysqld"]