- Start minikube
```
[chandrashekhar@localhost ~]$ minikube version
minikube version: v1.9.2
commit: 93af9c1e43cab9618e301bc9fa720c63d5efa393
[chandrashekhar@localhost ~]$ minikube start -p iotpoc
[chandrashekhar@localhost ~]$ kubectl create ns kafka
[chandrashekhar@localhost ~]$ kubectl config set-context $(kubectl config current-context) --namespace=testfuse

```
- Deploy
```
eval  $(minikube -p iotpoc docker-env)
mvn k8s:build
mvn k8s:resource
mvn k8s:deploy
```
- Check Pod is created in namespace kafka.

```
$ kubectl get pods
NAME                                          READY   STATUS    RESTARTS   AGE
my-cluster-entity-operator-574bcbc568-rjmfj   3/3     Running   11         6d9h
my-cluster-kafka-0                            1/1     Running   3          6d9h
my-cluster-zookeeper-0                        1/1     Running   4          6d10h
my-connect-connect-6bb65d9bd5-78l88           1/1     Running   4          5d20h
strimzi-cluster-operator-54ff55979f-bmnqt     1/1     Running   2          6d10h
[chandrashekhar@localhost spring-boot-cxf-jaxrs-minikube]$ 

$ kubectl get svc
[chandrashekhar@localhost POC]$ kubectl get svc
NAME                                  TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)                      AGE
my-cluster-kafka-0                    NodePort    10.96.91.67      <none>        9094:30652/TCP               6d9h
my-cluster-kafka-bootstrap            ClusterIP   10.107.97.187    <none>        9091/TCP,9092/TCP,9093/TCP   6d9h
my-cluster-kafka-brokers              ClusterIP   None             <none>        9091/TCP,9092/TCP,9093/TCP   6d9h
my-cluster-kafka-external-bootstrap   NodePort    10.104.31.245    <none>        9094:31458/TCP               6d9h
my-cluster-zookeeper-client           ClusterIP   10.105.235.149   <none>        2181/TCP                     6d10h
my-cluster-zookeeper-nodes            ClusterIP   None             <none>        2181/TCP,2888/TCP,3888/TCP   6d10h
my-connect-connect-api                ClusterIP   10.108.167.252   <none>        8083/TCP                     6d5h
[chandrashekhar@localhost POC]$ 

```
- Note in application.properties we have set kafka-bootstrap for camel-kafka component as "camel.component.kafka.configuration.brokers=my-cluster-kafka-bootstrap:9092"
- Access kafka pod and run consumer

```
$ kubectl exec --stdin --tty my-cluster-kafka-0 -- /bin/bash
[kafka@my-cluster-kafka-0 bin]$ pwd 
/opt/kafka/bin
[kafka@my-cluster-kafka-0 bin]$ ./kafka-topics.sh --list --bootstrap-server 0.0.0.0:9092
__consumer_offsets
connect-test-topic
csp
my-connect-configs
my-connect-offsets
my-connect-status
[kafka@my-cluster-kafka-0 bin]$ ./kafka-console-consumer.sh --bootstrap-server 0.0.0.0:9092 --group test-group --topic csp --from-beginning
Message creation: 2020-12-12 11:08:20
Message creation: 2020-12-12 11:08:23
Message creation: 2020-12-12 11:08:24
Message creation: 2020-12-12 11:08:25
^CProcessed a total of 106 messages
[kafka@my-cluster-kafka-0 bin]$ ./kafka-consumer-groups.sh --bootstrap-server 0.0.0.0:9092 --group test-group --describe

Consumer group 'test-group' has no active members.

GROUP           TOPIC           PARTITION  CURRENT-OFFSET  LOG-END-OFFSET  LAG             CONSUMER-ID     HOST            CLIENT-ID
test-group      csp             0          106             106             0               -               -               -
[kafka@my-cluster-kafka-0 bin]$ 

```
- Undeploy
```
mvn k8s:undeploy
[chandrashekhar@localhost ~]$ kubectl get deployment -n testfuse
No resources found in testfuse namespace.
[chandrashekhar@localhost ~]$ kubectl get pods -n testfuse
No resources found in testfuse namespace.
```
