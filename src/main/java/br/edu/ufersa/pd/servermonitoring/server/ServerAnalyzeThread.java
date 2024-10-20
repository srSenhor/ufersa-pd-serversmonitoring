package br.edu.ufersa.pd.servermonitoring.server;

import java.time.LocalDateTime;
import java.util.Random;

import br.edu.ufersa.pd.servermonitoring.entities.ServerInfo;
import br.edu.ufersa.pd.servermonitoring.utils.ServerStatusWrapper;
import br.edu.ufersa.pd.servermonitoring.utils.ServiceType;
import br.edu.ufersa.pd.servermonitoring.utils.Status;

public class ServerAnalyzeThread implements Runnable {

    private ServerStatusWrapper serverStatus;
    private final String SERVER_NAME;

	public ServerAnalyzeThread(String serverName, ServerStatusWrapper serverStatus) {
		this.SERVER_NAME = serverName;
        this.serverStatus = ServerStatusWrapper.getInstance();
	}

    private ServerInfo generateInfo(ServerInfo info, ServiceType type) {
        Random r = new Random();
        
        float cpuUsage = r.nextFloat(3.0f, 10.0f);
        float memoryUsage = r.nextFloat(3.0f, 10.0f);
        int responseTime = r.nextInt(20, 50);
        float activeConnections = r.nextFloat(3.0f, 10.0f);
        
        cpuUsage = (r.nextBoolean() == true) ? cpuUsage : - cpuUsage;
        memoryUsage = (r.nextBoolean() == true) ? memoryUsage : - memoryUsage;
        responseTime = (r.nextBoolean() == true) ? responseTime : - responseTime;
        activeConnections = (r.nextBoolean() == true) ? activeConnections : - activeConnections;
        
        
        info.setServiceType(type);
        info.incrementCpuUsage(cpuUsage);
        info.incrementMemoryUsage(memoryUsage);
        info.incrementResponseTime(responseTime);
        info.incrementActiveConnections(activeConnections);            

        return info;
    }
    
    @Override
    public void run() {

        ServerInfo info = new ServerInfo(LocalDateTime.now(), SERVER_NAME);
        info = generateInfo(info, ServiceType.WEBSERVICE);
        
        if (info.getCpuUsage() < 60.0f 
        && info.getMemoryUsage() < 70.0f 
        && info.getResponseTime() < 200 
        && info.getActiveConnections() < 70.0f) {

            info.setStatus(Status.OK);

        } else if (info.getCpuUsage() < 85.0f 
        && info.getMemoryUsage() < 90.0f 
        && info.getResponseTime() < 500 
        && info.getActiveConnections() < 90.0f) {

            info.setStatus(Status.WARNING);

        } else {
            info.setStatus(Status.CRITICAL);
        }

        serverStatus.update(ServiceType.WEBSERVICE.name(), info);

        // ------------------------------------------------------

        info = new ServerInfo(LocalDateTime.now(), SERVER_NAME);
        info = generateInfo(info, ServiceType.DATABASESERVICE);

        if (info.getCpuUsage() < 50.0f 
        && info.getMemoryUsage() < 60.0f 
        && info.getResponseTime() < 100 
        && info.getActiveConnections() < 60.0f) {

            info.setStatus(Status.OK);

        } else if (info.getCpuUsage() < 75.0f 
        && info.getMemoryUsage() < 80.0f 
        && info.getResponseTime() < 300 
        && info.getActiveConnections() < 80.0f) {

            info.setStatus(Status.WARNING);

        } else {
            info.setStatus(Status.CRITICAL);
        }

        serverStatus.update(ServiceType.DATABASESERVICE.name(), info);

    }
}
