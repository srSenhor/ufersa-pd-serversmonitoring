package br.edu.ufersa.pd.servermonitoring.server;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import br.edu.ufersa.pd.servermonitoring.entities.ServerInfo;
import br.edu.ufersa.pd.servermonitoring.utils.ServerStatusWrapper;
import br.edu.ufersa.pd.servermonitoring.utils.ServiceType;

public class SubCentralServerThread implements Runnable {

    private final String EXCHANGE;
    private final List<String> ROUTINGKEYS;
    private final ConnectionFactory factory;
    private Connection conn;
    private Channel channel;
    private String queueName;
    private ServerStatusWrapper serverStatus;
    private ConcurrentMap<String, ServerInfo> map;

    public SubCentralServerThread(ConcurrentMap<String, ServerInfo> map) {
        this.EXCHANGE = "monitoring_agency";
        this.ROUTINGKEYS = Arrays.asList(
            "*.service1",
            "*.service2"
            // "#"
        );
        this.factory = new ConnectionFactory();
        this.serverStatus = ServerStatusWrapper.getInstance();
        this.map = map;
        this.initialize();
    }

    private void initialize() {
        factory.setHost("localhost");

        try {

            this.conn = factory.newConnection();
            this.channel = conn.createChannel();
            channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.TOPIC);
            queueName = channel.queueDeclare().getQueue();

            this.channel.queueBind(queueName, EXCHANGE, "#");

        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String info = new String(delivery.getBody(), "UTF-8");
            String [] fields = info.split("#");
            
            ServerInfo updatedInfo = ServerInfo.fromString(fields[0]);
            serverStatus.update(updatedInfo.getServiceType().name(), updatedInfo);

        };

        try {
            channel.basicConsume(queueName, true, deliverCallback, (consumerTag) -> {});
        } catch (IOException e) {
            e.printStackTrace();
        }

        switch (serverStatus.getServerName()) {
            case "Server 1":
                map.put("Server 1 (WEBSERVICE)", serverStatus.get(ServiceType.WEBSERVICE.name()));
                map.put("Server 1 (DATABASESERVICE)", serverStatus.get(ServiceType.DATABASESERVICE.name()));
                break;
            case "Server 2":
                map.put("Server 2 (WEBSERVICE)", serverStatus.get(ServiceType.WEBSERVICE.name()));
                map.put("Server 2 (DATABASESERVICE)", serverStatus.get(ServiceType.DATABASESERVICE.name()));
                break;
            case "Server 3":
                map.put("Server 3 (WEBSERVICE)", serverStatus.get(ServiceType.WEBSERVICE.name()));
                map.put("Server 3 (DATABASESERVICE)", serverStatus.get(ServiceType.DATABASESERVICE.name()));
                break;
        
            default:
                break;
        }

    }
}
