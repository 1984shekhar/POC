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

minikube setup:
minikube start --vm-driver kvm --alsologtostderr
eval $(minikube docker-env)
docker images
docker build -t cpandey/minimal-java .
docker build -t springbootexample .
docker images
kubectl run springbootsimple --image=springbootexample --port 8080 --image-pull-policy=IfNotPresent
kubectl expose deployment springbootsimple --type=NodePort
kubectl get services
echo $(minikube service springbootsimple --url)
curl -v http://192.168.42.216:30199


