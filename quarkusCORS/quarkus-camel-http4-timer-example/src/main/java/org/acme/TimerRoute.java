package org.acme;

import org.apache.camel.builder.RouteBuilder;

public class TimerRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("timer:foo?period=10000")
        .log("Hello World")
        .to("http://localhost:8080/hello-resteasy")
        .log("response body: ${body}");
    }
}