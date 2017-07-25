# Karaf 2 and CXF REST QuickStart

This quickstart demonstrates how to create a RESTful (JAX-RS) web service using CXF and expose it through the OSGi HTTP Service.

The REST service provides a customer service that supports the following operations

- PUT /customerservice/customers/ - to create or update a customer
- GET /customerservice/customers/{id} - to view a customer with the given id
- DELETE /customerservice/customers/{id} - to delete a customer with the given id
- GET /customerservice/orders/{orderId} - to view an order with the given id
- GET /customerservice/orders/{orderId}/products/{productId} - to view a specific product on an order with the given id

When the application is deployed, you can access the REST service using a web browser.

### Building

The example can be built with

    mvn clean install

### Running the example in OpenShift

It is assumed that:
- OpenShift platform is already running, if not you can find details how to [Install OpenShift at your site](https://docs.openshift.com/container-platform/3.3/install_config/index.html).
- Your system is configured for Fabric8 Maven Workflow, if not you can find a [Get Started Guide](https://access.redhat.com/documentation/en/red-hat-jboss-middleware-for-openshift/3/single/red-hat-jboss-fuse-integration-services-20-for-openshift/)

The example can be built and deployed using a single goal:

    mvn fabric8:deploy

When the example runs in OpenShift, you can use the OpenShift client tool to inspect the status

To list all the running pods:

    oc get pods

Then find the name of the pod that runs this quickstart, and output the logs from the running pods with:

    oc logs <name of pod>

You can also use the openshift [web console](https://docs.openshift.com/container-platform/3.3/getting_started/developers_console.html#developers-console-video) to manage the
running pods, and view logs and much more.

To list service

	oc get service
	
Now we have to expose service as route. Here service_name can be taken from output above.

	oc expose service/<service_name>
	
	At my local system:
	
[cpandey@cpandey fis-karaf-cxf]$ oc get service
NAME            CLUSTER-IP      EXTERNAL-IP   PORT(S)    AGE
fis-karaf-cxf   172.30.66.134   <none>        8181/TCP   16m

[cpandey@cpandey fis-karaf-cxf]$ oc get route
NAME            HOST/PORT                                      PATH      SERVICES        PORT      TERMINATION
fis-karaf-cxf   fis-karaf-cxf-myproject.192.168.42.31.nip.io             fis-karaf-cxf   default   
[cpandey@cpandey fis-karaf-cxf]$ 


### Running via an S2I Application Template

Applicaiton templates allow you deploy applications to OpenShift by filling out a form in the OpenShift console that allows you to adjust deployment parameters.  This template uses an S2I source build so that it handle building and deploying the application for you.

First, import the Fuse image streams:

    oc create -f https://raw.githubusercontent.com/jboss-fuse/application-templates/GA/fis-image-streams.json

Then create the quickstart template:

    oc create -f https://raw.githubusercontent.com/jboss-fuse/application-templates/GA/quickstarts/karaf2-cxf-rest-template.json

Now when you use "Add to Project" button in the OpenShift console, you should see a template for this quickstart. 

### Access services using a web browser

You can use any browser to perform a HTTP GET.  This allows you to very easily test a few of the RESTful services we defined:

Notice: As it depends on your OpenShift setup, the hostname (route) might vary. Verify with `oc get routes` which hostname is valid for you.  You can create route(or expose service) as above or add the '-Dfabric8.deploy.createExternalUrls=true' option to your maven commands if you want it to deploy a Route configuration for the service.

Use this URL to display the root of the REST service, which also allows to access the WADL of the service:

   http://fis-karaf-cxf-myproject.192.168.42.31.nip.io/cxf/crm?_wadl

Use this URL to display the XML representation for customer 123:

    http://fis-karaf-cxf-myproject.192.168.42.31.nip.io/cxf/crm/customerservice/customers/123

You can also access the XML representation for order 123 ...

    http://fis-karaf-cxf-myproject.192.168.42.31.nip.io/cxf/crm/customerservice/customers/123

**Note:** if you use Safari, you will only see the text elements but not the XML tags - you can view the entire document with 'View Source'


### To run a command-line utility:

You can use a command-line utility, such as cURL or wget, to perform the HTTP requests.  We have provided a few files with sample XML representations in `src/test/resources`, so we will use those for testing our services.

1. Open a command prompt and change directory to `karaf2-cxf-rest`.
2. Run the following curl commands (curl commands may not be available on all platforms):

    * Create a customer

            curl -X POST -T src/test/resources/add_customer.xml -H "Content-Type: text/xml" http://fis-karaf-cxf-myproject.192.168.42.31.nip.io/cxf/crm/customerservice/customers

    * Retrieve the customer instance with id 123

            curl http://quickstart-cxf-rest.example.com/cxf/crm/customerservice/customers/123

    * Update the customer instance with id 123

            curl -X PUT -T src/test/resources/update_customer.xml -H "Content-Type: text/xml" http://fis-karaf-cxf-myproject.192.168.42.31.nip.io/cxf/crm/customerservice/customers

    * Delete the customer instance with id 123

             curl -X DELETE http://fis-karaf-cxf-myproject.192.168.42.31.nip.io/cxf/crm/customerservice/customers/123


### Integration Testing

The example includes a [fabric8 arquillian](https://github.com/fabric8io/fabric8/tree/v2.2.170.redhat/components/fabric8-arquillian) OpenShift Integration Test. 
Once the container image has been built and deployed in OpenShift, the integration test can be run with:

    mvn test -Dtest=*KT

The test is disabled by default and has to be enabled using `-Dtest`. Open Source Community documentation at [Integration Testing](https://fabric8.io/guide/testing.html) and [Fabric8 Arquillian Extension](https://fabric8.io/guide/arquillian.html) provide more information on writing full fledged black box integration tests for OpenShift. 
