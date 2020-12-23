FROM openjdk:11
WORKDIR /opt/app
COPY target/*.jar app.jar
COPY src/docker/wait-for-it.sh wait-for-it.sh
RUN chmod +x wait-for-it.sh
ENV SPRING_DATASOURCE_USERNAME: root
ENV SPRING_DATASOURCE_PASSWORD: postgres
ENV SPRING_DATASOURCE_URL: jdbc:mysql://mysqldb:3306/db
ENTRYPOINT ["java","-jar","app.jar"]