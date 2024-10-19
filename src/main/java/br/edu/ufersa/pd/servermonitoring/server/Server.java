package br.edu.ufersa.pd.servermonitoring.server;

import java.time.LocalDateTime;
import java.util.Random;

import br.edu.ufersa.pd.servermonitoring.entities.ServiceData;
import br.edu.ufersa.pd.servermonitoring.utils.ServiceType;
import br.edu.ufersa.pd.servermonitoring.utils.Status;

public class Server {

    private String name;
    private ServiceType serviceType;

    public Server(String name, ServiceType serviceType) {
        this.name = name;
        this.serviceType = serviceType;
        this.init();
    }


    private void init() {
        
        Random r = new Random();
        ServiceData data = new ServiceData(LocalDateTime.now(), serviceType, name);

        for (int i = 0; i < 100; i++) {
            float cpuUsage = r.nextFloat(5.0f);
            float memoryUsage = r.nextFloat(5.0f);
            int responseTime = r.nextInt(50);
            float activeConnections = r.nextFloat(5.0f);
    
    
            cpuUsage = (r.nextBoolean() == true) ? cpuUsage : - cpuUsage;
            memoryUsage = (r.nextBoolean() == true) ? memoryUsage : - memoryUsage;
            responseTime = (r.nextBoolean() == true) ? responseTime : - responseTime;
            activeConnections = (r.nextBoolean() == true) ? activeConnections : - activeConnections;
    
            data.incrementCpuUsage(cpuUsage);
            data.incrementMemoryUsage(memoryUsage);
            data.incrementResponseTime(responseTime);
            data.incrementActiveConnections(activeConnections);            
        }



        switch (serviceType) {
            case WEBSERVICE:

                if (data.getCpuUsage() < 60.0f 
                && data.getMemoryUsage() < 70.0f 
                && data.getResponseTime() < 200 
                && data.getActiveConnections() < 70.0f) {

                    data.setStatus(Status.OK);

                } else if (data.getCpuUsage() < 85.0f 
                && data.getMemoryUsage() < 90.0f 
                && data.getResponseTime() < 500 
                && data.getActiveConnections() < 90.0f) {

                    data.setStatus(Status.WARNING);

                } else {
                    data.setStatus(Status.CRITICAL);
                }
                
                break;
            case DATABASESERVICE:

                if (data.getCpuUsage() < 50.0f 
                && data.getMemoryUsage() < 60.0f 
                && data.getResponseTime() < 100 
                && data.getActiveConnections() < 60.0f) {

                    data.setStatus(Status.OK);

                } else if (data.getCpuUsage() < 75.0f 
                && data.getMemoryUsage() < 80.0f 
                && data.getResponseTime() < 300 
                && data.getActiveConnections() < 80.0f) {

                    data.setStatus(Status.WARNING);

                } else {
                    data.setStatus(Status.CRITICAL);
                }
                
                break;
            default:
                break;
        }

        System.out.println(data.toString());

    }
    
    

}
