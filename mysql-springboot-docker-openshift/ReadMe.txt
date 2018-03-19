minishift start
eval $(minishift oc-env) 
eval $(minishift docker-env) 
oc login
docker login -u developer -p $(oc whoami -t) $(minishift openshift registry)
docker build -t springboot_mysql -f ./dockerfile_springboot_mysql .
docker images
docker tag springboot_mysql $(minishift openshift registry)/myproject/springboot_mysql
docker push $(minishift openshift registry)/myproject/springboot_mysql
docker pull openshift/mysql-56-centos7
oc new-app -e MYSQL_USER=root -e MYSQL_PASSWORD=root -e MYSQL_DATABASE=test openshift/mysql-56-centos7
oc get pods
oc rsh mysql-56-centos7-1-nvth9
sh-4.2$ mysql -u root
CREATE USER 'root'@'%' IDENTIFIED BY 'root';
Query OK, 0 rows affected (0.00 sec)
 
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;
Query OK, 0 rows affected (0.00 sec)
 
FLUSH PRIVILEGES;
Query OK, 0 rows affected (0.00 sec)
 
exit
oc get svc
oc new-app -e spring_datasource_url=jdbc:mysql://172.30.145.88:3306/test springboot_mysql
oc get pods
oc logs -f springbootmysql-1-5ngv4
oc get svc
oc expose svc springbootmysql
oc get route

#To test applications:
curl -v http://springbootmysql-myproject.192.168.42.182.nip.io/demo/all
curl http://springbootmysql-myproject.192.168.42.182.nip.io/demo/add?name=SpringBootMysqlTest
Saved
curl http://springbootmysql-myproject.192.168.42.182.nip.io/demo/all
