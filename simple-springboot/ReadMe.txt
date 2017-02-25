How to run it:
mvn spring-boot:run

or

mvn package
java -jar <jar_name_in_target_folder>


curl -v http://localhost:8080/

DockerCommand:
docker build -t springbootExample .
docker images
docker run -p 8080:8080 -t springbootexample
docker ps
docker stop <container_id>

