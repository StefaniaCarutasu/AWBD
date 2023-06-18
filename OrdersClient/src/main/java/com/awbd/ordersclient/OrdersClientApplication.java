package com.awbd.ordersclient;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class OrdersClientApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(OrdersClientApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);

        WebClient loadBalancedClient = ctx.getBean(WebClient.Builder.class).build();

        for (int i = 1; i <= 10; i++) {
            String response =
                    loadBalancedClient.get().uri("http://orders-service/orders/webflux/list/stefi")
                            .retrieve().toEntity(String.class)
                            .block().getBody();
            System.out.println(response);
        }
    }

}
