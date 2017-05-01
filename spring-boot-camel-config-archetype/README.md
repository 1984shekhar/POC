# Spring Boot and Camel using ConfigMaps and Secrets 

This quickstart demonstrates how to configure a Spring-Boot application using Openshift/kubernetes ConfigMaps and Secrets.

A route generates sample messages that are delivered to a list of recipient endpoints configured through a property named `quickstart.recipients` in the `src/main/resources/application.properties` file.
The property can be overridden using a Openshift/kubernetes ConfigMap object.
As soon as a ConfigMap named `camel-config` (containing a property named `application.properties`) is created or changed in the project, an application-context refresh event will be triggered and the logs will reflect the new configuration. 
A sample `ConfigMap` (`sample-configmap.yml`) is contained in this repository (it changes the configuration to use all available endpoints in the `recipientList`). 

The quickstart will run on Openshift/kubernetes using a `ServiceAccount` named `qs-camel-config`, with the `view` role granted.
This way, the application is allowed to read the `ConfigMap` and to listen for changes in the current Openshift/kuberenetes project.

Secrets can also be used to configure the application (a sample username/password combination is configured using secrets in this quickstart).
Unlike the `ConfigMap` objects, secrets require higher permissions in order to be read using the Openshift/kubernetes APIs.
To overcome this security limitation, the approach used in this quickstart is to mount the secret as a volume in the Pod and 
configure its location in the spring-cloud config file (`src/main/resources/bootstrap.yml`).

A sample secret (`sample-secret.yml`) is contained in this repository (it just replaces the username `wrong-username` with `myuser`). 

**Note: a secret named `camel-config` must be present in the namespace before the application is deployed**
(otherwise the container remains in a pending status, waiting for it).

### Building

The example can be built with

    mvn clean install


### Running the example in Minikube
eval $(minikube docker-env)

The following command will create the (**required**) secret:

    kubectl create -f sample-secret.yml

The following command can be used to create the ConfigMap (the ConfigMap can be also created after the application has been deployed, to see the live-reload feature in action):

    kubectl create -f sample-configmap.yml

Then the following command will package your app and run it on Openshift/kubernetes:

    mvn fabric8:deploy

To list all the running pods:

    kubectl get pods

Then find the name of the pod that runs this quickstart, and output the logs from the running pods with:

    kubectl logs <name of pod>

