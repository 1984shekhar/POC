/**
 *  Copyright 2005-2016 Red Hat, Inc.
 *
 *  Red Hat licenses this file to you under the Apache License, version
 *  2.0 (the "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.  See the License for the specific language governing
 *  permissions and limitations under the License.
 */
package com.iot.kafka.poc;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.apache.camel.component.amqp.AMQPComponent;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({"classpath:spring/camel-context.xml"})
public class PocApplication {


    public static void main(String[] args) {
        SpringApplication.run(PocApplication.class, args);
    }
    
    @Bean(name = "amqp-component")
    AMQPComponent amqpComponent(AMQPConfiguration config) {
        String remoteURI = String.format("amqp://%s:%s?%s", config.getServiceName(), config.getServicePort(), config.getParameters());

        JmsConnectionFactory qpid = new JmsConnectionFactory(config.getUsername(), config.getPassword(), remoteURI);

        PooledConnectionFactory factory = new PooledConnectionFactory();
        factory.setConnectionFactory(qpid);

        return new AMQPComponent(factory);
    }
 
}
