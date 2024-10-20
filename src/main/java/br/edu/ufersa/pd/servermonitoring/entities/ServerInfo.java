package br.edu.ufersa.pd.servermonitoring.entities;

import br.edu.ufersa.pd.servermonitoring.utils.ServiceType;
import br.edu.ufersa.pd.servermonitoring.utils.Status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerInfo {

    private LocalDateTime timestamp;
    private static DateTimeFormatter fmt;
    private ServiceType serviceType;
    private Status status;
    private String serverName;
    private float cpuUsage;
    private float memoryUsage;
    private int responseTime;
    private float activeConnections;

    
    public ServerInfo() {
        setStatus(Status.OK);
        this.cpuUsage = 0.0f;
        this.memoryUsage = 0.0f;
        this.responseTime = 1;
        this.activeConnections = 0.0f;
        fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    }
    
    private ServerInfo(LocalDateTime timestamp, ServiceType serviceType, Status status, String serverName,
            float cpuUsage, float memoryUsage, int responseTime, float activeConnections) {
        this.timestamp = timestamp;
        this.serviceType = serviceType;
        this.status = status;
        this.serverName = serverName;
        this.cpuUsage = cpuUsage;
        this.memoryUsage = memoryUsage;
        this.responseTime = responseTime;
        this.activeConnections = activeConnections;
        fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    public ServerInfo(LocalDateTime timestamp, String serverName) {
        setTimestamp(timestamp);
        setServerName(serverName);
        setStatus(Status.OK);
        this.cpuUsage = 0.0f;
        this.memoryUsage = 0.0f;
        this.responseTime = 1;
        this.activeConnections = 0.0f;
        fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }
    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

    public String getServerName() {
        return serverName;
    }
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public float getCpuUsage() {
        return cpuUsage;
    }
    public void incrementCpuUsage(float cpuUsage) {
        if (this.cpuUsage + cpuUsage < 0.0f) {
            this.cpuUsage = 0.0f;
            return;
        } 
        if (this.cpuUsage + cpuUsage > 100.0f) {
            this.cpuUsage = 100.0f;
            return;
        } 
        this.cpuUsage += cpuUsage;
    }

    public float getMemoryUsage() {
        return memoryUsage;
    }
    public void incrementMemoryUsage(float memoryUsage) {
        if (this.memoryUsage + memoryUsage < 0.0f) {
            this.memoryUsage = 0.0f;
            return;
        } 
        if (this.memoryUsage + memoryUsage > 100.0f) {
            this.memoryUsage = 100.0f;
            return;
        } 
        this.memoryUsage += memoryUsage;
    }

    public int getResponseTime() {
        return responseTime;
    }
    public void incrementResponseTime(int responseTime) {
        if (this.responseTime + responseTime < 0) {
            this.responseTime = 0;
            return;
        } 
        this.responseTime += responseTime;
    }

    public float getActiveConnections() {
        return activeConnections;
    }
    public void incrementActiveConnections(float activeConnections) {
        if (this.activeConnections + activeConnections < 0.0f) {
            this.activeConnections = 0.0f;
            return;
        } 
        if (this.activeConnections + activeConnections > 100.0f) {
            this.activeConnections = 100.0f;
            return;
        } 
        this.activeConnections += activeConnections;
    }

    @Override
    public String toString() {
        return      "{timestamp:" + fmt.format(timestamp) + "," 
                +   "service:" + serviceType + ","
                +   "status:" + status + ","
                +   "server:" + serverName + ","
                +   "metrics: {" 
                +   "cpu_usage:" + cpuUsage + ","
                +   "memory_usage:" + memoryUsage + ","
                +   "response_time:" + responseTime + ","
                +   "active_connections:" + activeConnections 
                +   "}}";
    }

    public String toSendFormat() {
        return  timestamp + "/" 
                + serviceType + "/" 
                + status + "/" 
                + serverName + "/" 
                + cpuUsage + "/" 
                + memoryUsage + "/" 
                + responseTime + "/" 
                + activeConnections + "/" 
                + "#";
    }

    public static ServerInfo fromString(String input) {

        String [] fields = input.split("/");

        LocalDateTime timestamp = LocalDateTime.parse(fields[0]);
        ServiceType serviceType = ServiceType.valueOf(fields[1]);
        Status status = Status.valueOf(fields[2]);
        String serverName = fields[3];
        float cpuUsage = Float.parseFloat(fields[4]);
        float memoryUsage = Float.parseFloat(fields[5]);
        int responseTime = Integer.parseInt(fields[6]);
        float activeConnections = Float.parseFloat(fields[7]);

        return new ServerInfo(timestamp, serviceType, status, serverName, cpuUsage, memoryUsage, responseTime, activeConnections);

    }

    @Override
    public ServerInfo clone() {
        return new ServerInfo(timestamp, serviceType, status, serverName, cpuUsage, memoryUsage, responseTime, activeConnections);
    }

    
    

}