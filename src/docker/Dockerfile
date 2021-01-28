FROM openjdk:11-jre-slim
WORKDIR /opt/app
COPY target/*.jar app.jar
COPY src/docker/wait-for-it.sh wait-for-it.sh
RUN chmod +x wait-for-it.sh
ENTRYPOINT ["java","-jar","app.jar"]