package br.edu.ufersa.pd.servermonitoring.server;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.*;

import br.edu.ufersa.pd.servermonitoring.utils.GUI;
import br.edu.ufersa.pd.servermonitoring.utils.ServerStatusWrapper;
import br.edu.ufersa.pd.servermonitoring.utils.ServiceType;

public class MonitoringAgentThread implements Runnable {

    private final String EXCHANGE;
    private final List<String> ROUTINGKEYS;
    private final ConnectionFactory factory;
    private ServerStatusWrapper serverStatus;

    public MonitoringAgentThread() {
        this.EXCHANGE = "monitoring_agency";
        this.ROUTINGKEYS = Arrays.asList(
            "server1.service1",
            "server1.service2",
            "server2.service1",
            "server2.service2",
            "server3.service1",
            "server3.service2"
        );
        this.factory = new ConnectionFactory();
        this.serverStatus = ServerStatusWrapper.getInstance();
    }

    @Override
    public void run() {
        
        factory.setHost("localhost");

        GUI.clearScreen();

        System.out.println("DEBUG: " + serverStatus.get(ServiceType.WEBSERVICE.name()));
        System.out.println("DEBUG: " + serverStatus.get(ServiceType.DATABASESERVICE.name()));

        try (   Connection conn = factory.newConnection();
                Channel channel = conn.createChannel()) {

            channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.TOPIC);

            switch (serverStatus.getServerName()) {
                case "Server 1":
                    channel.basicPublish(
                        EXCHANGE, 
                        ROUTINGKEYS.get(0), 
                        null, 
                        serverStatus.get(ServiceType.WEBSERVICE.name()).toSendFormat().getBytes(StandardCharsets.UTF_8)
                    );
                    channel.basicPublish(
                        EXCHANGE, 
                        ROUTINGKEYS.get(1), 
                        null, 
                        serverStatus.get(ServiceType.DATABASESERVICE.name()).toSendFormat().getBytes(StandardCharsets.UTF_8)
                    );
                    break;
                case "Server 2":
                    channel.basicPublish(
                        EXCHANGE, 
                        ROUTINGKEYS.get(2), 
                        null, 
                        serverStatus.get(ServiceType.WEBSERVICE.name()).toSendFormat().getBytes(StandardCharsets.UTF_8)
                    );
                    channel.basicPublish(
                        EXCHANGE, 
                        ROUTINGKEYS.get(3), 
                        null, 
                        serverStatus.get(ServiceType.DATABASESERVICE.name()).toSendFormat().getBytes(StandardCharsets.UTF_8)
                    );
                    break;
                case "Server 3":
                    channel.basicPublish(
                        EXCHANGE, 
                        ROUTINGKEYS.get(4), 
                        null, 
                        serverStatus.get(ServiceType.WEBSERVICE.name()).toSendFormat().getBytes(StandardCharsets.UTF_8)
                    );
                    channel.basicPublish(
                        EXCHANGE, 
                        ROUTINGKEYS.get(5), 
                        null, 
                        serverStatus.get(ServiceType.DATABASESERVICE.name()).toSendFormat().getBytes(StandardCharsets.UTF_8)
                    );
                    break;
                default:
                    break;
            }
            
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }

    }

}
