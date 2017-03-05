mvn clean package -DskipTests

Run the jar in target folder as:

java -jar camel-springboot-1.0.jar


#Docker Commands(from location of pom.xml):
mvn package docker:build
docker images
docker run --name camel-springboot-docker springboot-docker-example/camel-springboot
#below command not required.
docker exec camel-springboot-docker ps -aef
#Stop running docker container
docker ps
docker stop camel-springboot-docker
#below It will delete image
docker image rm --force springboot-docker-example/camel-springboot