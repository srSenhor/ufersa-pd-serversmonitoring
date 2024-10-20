package br.edu.ufersa.pd.servermonitoring.server;

import java.time.LocalDateTime;
import java.util.Random;

import br.edu.ufersa.pd.servermonitoring.entities.ServerInfo;
import br.edu.ufersa.pd.servermonitoring.utils.ServiceType;
import br.edu.ufersa.pd.servermonitoring.utils.Status;

public class ServerAnalyzeThread implements Runnable {

    private ServerStatusWrapper serverStatus;
    private final String SERVER_NAME;

	public ServerAnalyzeThread(String serverName, ServerStatusWrapper serverStatus) {
		this.SERVER_NAME = serverName;
        this.serverStatus = ServerStatusWrapper.getInstance();
	}
    
    @Override
    public void run() {
        Random r = new Random();
        
        float cpuUsage = r.nextFloat(5.0f);
        float memoryUsage = r.nextFloat(5.0f);
        int responseTime = r.nextInt(50);
        float activeConnections = r.nextFloat(5.0f);
        
        cpuUsage = (r.nextBoolean() == true) ? cpuUsage : - cpuUsage;
        memoryUsage = (r.nextBoolean() == true) ? memoryUsage : - memoryUsage;
        responseTime = (r.nextBoolean() == true) ? responseTime : - responseTime;
        activeConnections = (r.nextBoolean() == true) ? activeConnections : - activeConnections;
        
        ServerInfo dataService1 = new ServerInfo(LocalDateTime.now(), SERVER_NAME);

        dataService1.incrementCpuUsage(cpuUsage);
        dataService1.incrementMemoryUsage(memoryUsage);
        dataService1.incrementResponseTime(responseTime);
        dataService1.incrementActiveConnections(activeConnections);            

        if (dataService1.getCpuUsage() < 60.0f 
        && dataService1.getMemoryUsage() < 70.0f 
        && dataService1.getResponseTime() < 200 
        && dataService1.getActiveConnections() < 70.0f) {

            dataService1.setStatus(Status.OK);

        } else if (dataService1.getCpuUsage() < 85.0f 
        && dataService1.getMemoryUsage() < 90.0f 
        && dataService1.getResponseTime() < 500 
        && dataService1.getActiveConnections() < 90.0f) {

            dataService1.setStatus(Status.WARNING);

        } else {
            dataService1.setStatus(Status.CRITICAL);
        }

        // ------------------------------------------------------

        if (dataService1.getCpuUsage() < 50.0f 
        && dataService1.getMemoryUsage() < 60.0f 
        && dataService1.getResponseTime() < 100 
        && dataService1.getActiveConnections() < 60.0f) {

            dataService1.setStatus(Status.OK);

        } else if (dataService1.getCpuUsage() < 75.0f 
        && dataService1.getMemoryUsage() < 80.0f 
        && dataService1.getResponseTime() < 300 
        && dataService1.getActiveConnections() < 80.0f) {

            dataService1.setStatus(Status.WARNING);

        } else {
            dataService1.setStatus(Status.CRITICAL);
        }

        // ------------------------------------------------------

        dataService1.setServiceType(ServiceType.WEBSERVICE);
        serverStatus.update(ServiceType.WEBSERVICE.name(), dataService1);

        ServerInfo dataService2 = dataService1.clone();

        dataService2.setServiceType(ServiceType.DATABASESERVICE);
        serverStatus.update(ServiceType.DATABASESERVICE.name(), dataService2);

    }
}
