package br.edu.ufersa.pd.servermonitoring.client;

import com.rabbitmq.client.ConnectionFactory;

import br.edu.ufersa.pd.servermonitoring.utils.GUI;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.*;

public class User {

    private final String EXCHANGE;
    private final String ROUTINGKEY;
    private final ConnectionFactory factory;

    public User() {
        this.factory = new ConnectionFactory();
        this.ROUTINGKEY = "service_orders";
        this.EXCHANGE = "work_order_queue";
        this.init();
    }

    private void init() {

        factory.setHost("localhost");
        try {
            
            Connection conn = factory.newConnection();
            Channel channel = conn.createChannel();

            channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.DIRECT);
            String queueName = channel.queueDeclare().getQueue();

            channel.queueBind(queueName, EXCHANGE, ROUTINGKEY);

            GUI.userScreen();

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println(message);
            };

            channel.basicConsume(queueName, true, deliverCallback, (consumerTag) -> {});

        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }

    }

}
