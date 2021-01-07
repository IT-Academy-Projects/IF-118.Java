# IF-118.Java
SoftClass Project

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
