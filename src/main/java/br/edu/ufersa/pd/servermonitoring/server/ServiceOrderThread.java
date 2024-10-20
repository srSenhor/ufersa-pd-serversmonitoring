package br.edu.ufersa.pd.servermonitoring.server;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import br.edu.ufersa.pd.servermonitoring.entities.ServerInfo;
import br.edu.ufersa.pd.servermonitoring.utils.*;

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

    private String selectAction(Status status) {
        return switch(status) {
            case WARNING -> "redirect users to another server";
            case CRITICAL -> "check and restart server";
            default -> "undefined";
        };
    }

    private String buildProblem(ServerInfo info) {
        return getProblem(info) + detailProblem(info);
    }

    private String detailProblem(ServerInfo info) {

        Status status = info.getStatus();

        return switch(status) {
            case WARNING -> "server is congested";
            case CRITICAL -> "server is not responding";
            default -> "undefined";
        };
    }

    private String getProblem(ServerInfo info) {

        StringBuilder problem = new StringBuilder();
        String temp = "";
        float value = 0.0f;

        switch (info.getServiceType()) {
            case WEBSERVICE:
                temp = ((value = info.getCpuUsage()) < 85.0f) ? "CPU Usage reach " + value + "%, " : "";
                problem.append(temp);

                temp = ((value = info.getMemoryUsage()) < 90.0f) ? "Memory Usage reach " + value + "%, " : "";
                problem.append(temp);
                
                temp = ((value = info.getResponseTime()) < 500) ? "Response Time " + value + "ms, " : "";
                problem.append(temp);
                
                temp = ((value = info.getActiveConnections()) < 90.0f) ? "Active Connections (capacity) reach" + value + "%, " : "";
                problem.append(temp);
                break;
            case DATABASESERVICE:
                temp = ((value = info.getCpuUsage()) < 75.0f) ? "CPU Usage reach" + value + "%, " : "";
                problem.append(temp);

                temp = ((value = info.getMemoryUsage()) < 80.0f) ? "Memory Usage reach " + value + "%, " : "";
                problem.append(temp);
                
                temp = ((value = info.getResponseTime()) < 300) ? "Response Time " + value + "ms, " : "";
                problem.append(temp);
                
                temp = ((value = info.getActiveConnections()) < 80.0f) ? "Active Connections (capacity) reach" + value + "%, " : "";
                problem.append(temp);
                break;
        
            default:
                break;
        }
        
        return problem.toString();

    }

    @Override
    public void run() {
        factory.setHost("localhost");

        try (   Connection conn = factory.newConnection();
                Channel channel = conn.createChannel()) {

            channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.DIRECT);

            serverStatus.get().values().stream().forEach(sample -> {

                String message = GUI.customServiceOrder(sample, buildProblem(sample), selectAction(sample.getStatus()));

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
