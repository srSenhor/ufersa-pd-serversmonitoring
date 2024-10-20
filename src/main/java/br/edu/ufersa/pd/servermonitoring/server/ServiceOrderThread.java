package br.edu.ufersa.pd.servermonitoring.server;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import br.edu.ufersa.pd.servermonitoring.utils.ServerStatusWrapper;
import br.edu.ufersa.pd.servermonitoring.utils.ServiceType;
import br.edu.ufersa.pd.servermonitoring.utils.Status;

public class ServiceOrderThread implements Runnable {

    private final String EXCHANGE;
    private final String ROUTINGKEY;
    private final ConnectionFactory factory;
    private ServerStatusWrapper serverStatus;

    public ServiceOrderThread() {
        this.EXCHANGE = "work_order_queue";
        this.ROUTINGKEY = "service_orders";
        this.factory = new ConnectionFactory();
        this.serverStatus = ServerStatusWrapper.getInstance();
    }

    @Override
    public void run() {
        factory.setHost("localhost");

        try (   Connection conn = factory.newConnection();
                Channel channel = conn.createChannel()) {

            channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.DIRECT);

            serverStatus.get().values().stream().forEach(sample -> {

                String message = sample.toString();

                switch (sample.getStatus()) {
                    case WARNING:
                    case CRITICAL:
                        try {
                            channel.basicPublish(EXCHANGE, ROUTINGKEY, null, message.getBytes(StandardCharsets.UTF_8));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }            
                        break;
                    default:
                        break;
                }
            });

        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

}
