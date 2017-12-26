

The example can be built and run on OpenShift using a single goal:

    mvn fabric8:deploy

To list all the running pods:

    oc get pods

Then find the name of the pod that runs this quickstart, and output the logs from the running pods with:

    oc logs <name of pod>

This example just connects the cxf rest service using url:


<to id="_to1" uri="http4://{{spring-boot-cxf-jaxrs.service.host}}:{{spring-boot-cxf-jaxrs.service.port}}/services/helloservice/sayHello/FIS"/>

Where following resolves to:
{{spring-boot-cxf-jaxrs.service.host}}:172.30.99.182
{{spring-boot-cxf-jaxrs.service.port}}:80

We can also just place Cluster-IP:PORT to access service from client.
[cpandey@cpandey spring-boot-cxf-jaxrs]$ oc get service
NAME                    CLUSTER-IP      EXTERNAL-IP                     PORT(S)    AGE
spring-boot-cxf-jaxrs   172.30.99.182   <none>                          80/TCP     5h



