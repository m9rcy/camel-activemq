package com.example.demo;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class CamelRouteBuilder extends RouteBuilder {

    public void configure() throws Exception {

        // Send a message to a queue every 5 seconds
        from("timer:mytimer?period=5000").routeId("generate-route")
                .transform(constant("HELLO from Camel!"))
                .to("jmsComponent:queue:INCOMING");

        // Receive the message from the queue and send it to another queue
        from("jmsComponent:queue:INCOMING").routeId("receive-route")
                .log("Received a message - ${body} - sending to outbound queue")
                .to("jmsComponent:queue:PROCESSED?exchangePattern=InOnly");

    }
}