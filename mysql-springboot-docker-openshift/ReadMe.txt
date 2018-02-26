
How to deploy a plain spring-boot project which interacts with mysql in OpenShift Environment ?

We might have a requirement where we have to migrate our spring-boot standalone project to OpenShift environment. 
We will discuss here one of the way where we can create dockerfile and thus push the docker image to OpenShift registry and finally create containers with spring-boot app deployed.
To create spring-boot app I took help from doc[1]. Also I pulled mysql centos based docker image in OpenShift environment.
One can find the actual code at github location[2]. One should follow below steps to deploy spring-boot app to OpenShift environment. I used minishift for local setup and testing.

One can build this project using 'mvn clean install'. I would suggest to copy the generated jar within target folder to location src/main/docker-files. So that while creating docker image, application jar can be found at same location.

1) One can change directory location to src/main/docker-files within the project as per link[2].
minishift start.
eval $(minishift docker-env)
# login using developer account
oc login
docker login -u developer -p $(oc whoami -t) $(minishift openshift registry)
docker build -t minimaljava -f ./dockerfile_java .
docker build -t springboot_mysql -f ./dockerfile_springboot_mysql .
#using below command, one can check docker image created.
docker images
#tag springboot_mysql image in openshift registry and finally push it to registry.
docker tag springboot_mysql $(minishift openshift registry)/myproject/springboot_mysql
docker push $(minishift openshift registry)/myproject/springboot_mysql
#pull centos mysql image-stream
docker pull openshift/mysql-56-centos7
#create deployment-config using image-stream
oc get is
NAME               DOCKER REPO                                  TAGS      UPDATED
mysql-56-centos7   172.30.1.1:5000/myproject/mysql-56-centos7   latest    About an hour ago
springboot_mysql   172.30.1.1:5000/myproject/springboot_mysql   latest    23 minutes ago

oc new-app -e MYSQL_USER=root -e MYSQL_PASSWORD=root -e MYSQL_DATABASE=test openshift/mysql-56-centos7
oc get pods
NAME                       READY     STATUS    RESTARTS   AGE
mysql-56-centos7-1-nvth9   1/1       Running   0          3m

#ssh into mysql and create root user with full privilege
oc rsh mysql-56-centos7-1-nvth9 
sh-4.2$ mysql -u root
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 3
Server version: 5.6.24 MySQL Community Server (GPL)

Copyright (c) 2000, 2015, Oracle and/or its affiliates. All rights reserved.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql> CREATE USER 'root'@'%' IDENTIFIED BY 'root';
Query OK, 0 rows affected (0.00 sec)

mysql> GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;
Query OK, 0 rows affected (0.00 sec)

mysql> FLUSH PRIVILEGES;
Query OK, 0 rows affected (0.00 sec)

#Expose route so that we can access spring-boot externally
oc expose service springbootmysql
route "springbootmysql" exposed
[cpandey@cpandey docker-files]$ oc get svc
NAME               CLUSTER-IP      EXTERNAL-IP   PORT(S)    AGE
mysql-56-centos7   172.30.145.88   <none>        3306/TCP   8h
springbootmysql    172.30.33.222   <none>        8080/TCP   5m
[cpandey@cpandey docker-files]$ oc get route
NAME              HOST/PORT                                         PATH      SERVICES          PORT       TERMINATION   WILDCARD
springbootmysql   springbootmysql-myproject.192.168.42.182.nip.io             springbootmysql   8080-tcp                 None



#Finally 
oc new-app -e  spring_datasource_url=jdbc:mysql://172.30.145.88:3306/test springboot_mysql

oc get pods
NAME                       READY     STATUS    RESTARTS   AGE
mysql-56-centos7-1-nvth9   1/1       Running   0          12m
springbootmysql-1-5ngv4    1/1       Running   0          9m
[cpandey@cpandey docker-files]$ 

oc logs -f springbootmysql-1-5ngv4
Here is a system: 1 | UBUNTU 17.10 LTS | 2017-08-11 00:00:00.0
Here is a system: 2 | RHEL 7 | 2017-07-21 00:00:00.0
Here is a system: 3 | Solaris 11 | 2017-08-13 00:00:00.0

One can test this application using below curl command:
#It should get all entries in database table.
curl -v http://springbootmysql-myproject.192.168.42.182.nip.io/demo/all
#It should set an entry in db.
[cpandey@cpandey docker-files]$ curl http://springbootmysql-myproject.192.168.42.182.nip.io/demo/add?name=SpringBootMysqlTest
Saved
#Now again check, db entries.
[cpandey@cpandey docker-files]$ curl  http://springbootmysql-myproject.192.168.42.182.nip.io/demo/all
[{"name":"UBUNTU 17.10 LTS","lastaudit":1502409600000,"id":1},{"name":"RHEL 7","lastaudit":1500595200000,"id":2},{"name":"Solaris 11","lastaudit":1502582400000,"id":3},{"name":"SpringBootTest","lastaudit":1519603200000,"id":4},{"name":"SpringBootMysqlTest","lastaudit":1519603200000,"id":5}












# For testing one can also directly connect openshift/mysql-56-centos7 with springboot_mysql jar created after 'mvn clean install' in target folder of project using below commands
# Note: Check docker0 interface in ifconfig output. Set that as IP while connecting to mysql openshift image. 
[cpandey@cpandey target]$ ifconfig|grep -A 4 docker
docker0: flags=4099<UP,BROADCAST,MULTICAST>  mtu 1500
        inet 172.17.0.1  netmask 255.255.0.0  broadcast 0.0.0.0
        ether 02:42:56:49:b2:5f  txqueuelen 0  (Ethernet)
        RX packets 0  bytes 0 (0.0 B)
        RX errors 0  dropped 0  overruns 0  frame 0
java -Dspring.datasource.url=jdbc:mysql://172.17.0.1:3306/test?useSSL=false -jar mysql-jdbc-0.0.1-SNAPSHOT.jar












[1]






