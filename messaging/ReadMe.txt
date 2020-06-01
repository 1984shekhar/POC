1. Generate Camel spring-boot archetype.

```
mvn archetype:generate   -DarchetypeGroupId=org.apache.camel.archetypes   -DarchetypeArtifactId=camel-archetype-spring-boot   -DarchetypeVersion=3.3.0
```

2. RUN IBM MQ docker image.

```
podman run   --env LICENSE=accept   --env MQ_QMGR_NAME=QM1   --publish 1414:1414   --publish 9443:9443   --detach   ibmcom/mq
```
3. Run spring-boot application

```
[chandrashekhar@localhost messaging]$ mvn spring-boot:run
```

4. Browse Queue

```
[chandrashekhar@localhost messaging]$ podman ps
CONTAINER ID  IMAGE                       COMMAND  CREATED      STATUS          PORTS                   NAMES
52f0f72d56ed  docker.io/ibmcom/mq:latest           5 hours ago  Up 5 hours ago  0.0.0.0:1414->1414/tcp  laughing_almeida
[chandrashekhar@localhost messaging]$ 

[chandrashekhar@localhost messaging]$  podman exec --tty --interactive 52f0f72d56ed  bash

bash-4.4$ /opt/mqm/samp/bin/amqsbcg DEV.QUEUE.1 QM1
```
