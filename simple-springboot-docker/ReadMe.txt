
For minikube setup follow below:
################################
minikube start --vm-driver kvm --alsologtostderr
eval $(minikube docker-env)
docker images
# rename Dockerfile_java to Dockerfile
docker build -t cpandey/minimal-java .
# rename Dockerfile to Dockerfile_java.
mvn clean package
#copy the jar file from target folder to the existing parent folder location and rename it to simple-springboot-1.0.jar app.jar.
# rename Dockerfile_springBoot to Dockerfile
docker build -t springbootexample .
docker images
# rename Dockerfile to Dockerfile_springBoot
kubectl run springbootsimple --image=springbootexample --port 8080 --image-pull-policy=IfNotPresent
kubectl expose deployment springbootsimple --type=NodePort
kubectl get services
minikiube ip
echo $(minikube service springbootsimple --url)
curl -v http://192.168.42.216:30199


DockerCommand(not for kubernetes i.e. plain docker):
docker build -t springbootExample .
docker images
docker run -p 8080:8080 -t springbootexample
docker ps
docker stop <container_id>

How to run it:
This example is having docker plugin which creates image in docker registry.
1)mvn package docker:build
2)kubectl run springbootsimple --image=springbootexample --port 8080 --image-pull-policy=IfNotPresent
3)kubectl expose deployment springbootsimple --type=NodePort
4)kubectl get services
5)minikiube ip
6)echo $(minikube service springbootsimple --url)
7)curl -v http://<IP_from_output_5>:<PORT_from_output_4>





