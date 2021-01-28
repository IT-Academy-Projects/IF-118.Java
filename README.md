# SoftClass  
[![GitHub license](https://img.shields.io/badge/license-MIT-yellowgreen.svg)](https://github.com/IT-Academy-Projects/IF-118.Java/blob/master/LICENSE)
[![GitHub issues](https://img.shields.io/github/issues/IT-Academy-Projects/IF-118.Java)](https://github.com/IT-Academy-Projects/IF-118.Java/issues)
[![Pending Pull-Requests](https://img.shields.io/github/issues-pr/IT-Academy-Projects/IF-118.Java?style=flat-square)](https://github.com/IT-Academy-Projects/IF-118.Java/pulls)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=IT-Academy-Projects_IF-118.Java&metric=alert_status)](https://sonarcloud.io/dashboard?id=IT-Academy-Projects_IF-118.Java)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=IT-Academy-Projects_IF-118.Java&metric=coverage)](https://sonarcloud.io/dashboard?id=IT-Academy-Projects_IF-118.Java)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=IT-Academy-Projects_IF-118.Java&metric=bugs)](https://sonarcloud.io/dashboard?id=IT-Academy-Projects_IF-118.Java)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=IT-Academy-Projects_IF-118.Java&metric=code_smells)](https://sonarcloud.io/dashboard?id=IT-Academy-Projects_IF-118.Java)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=IT-Academy-Projects_IF-118.Java&metric=security_rating)](https://sonarcloud.io/dashboard?id=IT-Academy-Projects_IF-118.Java)

## Run the application from Docker:
1. Head to the project directory and build the project:
```
./mvnw clean package
```
2. Build docker image:
```
docker build -t project -f src/docker/Dockerfile .
```
3. Start the application:
```
docker-compose -f src/docker/docker-compose.yml up
```
4. Access site at: 
```
localhost:8080
```
5. To stop the application:
```
Ctrl+C
docker-compose -f src/docker/docker-compose.yml down
```
6. Delete unused image:
```
docker images
docker image rm <repo:tag>
```