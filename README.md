# IF-118.Java
SoftClass Project

## Run the application from Docker:
1. Install Docker (if not installed already):
```
sudo apt install docker.io
sudo systemctl enable --now docker
```
2. Install docker-compose (if not installed already):
```
sudo curl -L "https://github.com/docker/compose/releases/download/1.27.4/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
```
3. Head to the project directory and build the project:
```
./mvnw clean package
```
4. Build docker image:
```
docker build -t project -f src/docker/Dockerfile .
```
5. Start the application:
```
docker-compose -f src/docker/docker-compose.yml up
```
6. To stop the application:
```
Ctrl+C
docker-compose -f src/docker/docker-compose.yml down
```
7. Delete unused image:
```
docker images
docker image rm <repo:tag>
```
