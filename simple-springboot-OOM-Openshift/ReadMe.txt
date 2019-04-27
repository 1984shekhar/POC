How to run it:
mvn fabric8:deploy

chandrashekhar@chandrashekhar:~/Development/GIT_REPO/POC/simple-springboot-OOM-Openshift$ oc get route|grep oom
simple-springboot-oom      simple-springboot-oom-amq-demo.192.168.42.221.nip.io                simple-springboot-oom      8080                    None
chandrashekhar@chandrashekhar:~/Development/GIT_REPO/POC/simple-springboot-OOM-Openshift$ 


chandrashekhar@chandrashekhar:~/Development/GIT_REPO/POC/simple-springboot$ curl http://simple-springboot-oom-amq-demo.192.168.42.221.nip.io  
{"timestamp":1556397021117,"status":500,"error":"Internal Server Error","exception":"java.lang.OutOfMemoryError","message":"Java heap space","path":"/"} 

chandrashekhar@chandrashekhar:~/Development/GIT_REPO/POC/simple-springboot$ oc rsh simple-springboot-oom-1-8hj57

sh-4.2$ ps -ef|grep java
1000050+     1     0  0 19:47 ?        00:00:11 java -javaagent:/opt/jolokia/jolokia.jar=config=/opt/jolokia/etc/jolokia.properties -Xmx192m -XX:ParallelGCThreads=1 -XX:ConcGCThreads=1 -Djava.util.concurrent.ForkJoinPool.common.parallelism=1 -cp . -jar /deployments/simple-springboot-1.0.jar




