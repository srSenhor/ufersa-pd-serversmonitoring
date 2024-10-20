package br.edu.ufersa.pd.servermonitoring.server;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import br.edu.ufersa.pd.servermonitoring.entities.ServerInfo;
import br.edu.ufersa.pd.servermonitoring.utils.GUI;
import br.edu.ufersa.pd.servermonitoring.utils.ServerStatusWrapper;
import br.edu.ufersa.pd.servermonitoring.utils.ServiceType;

public class Server {

    private String name;
    private ScheduledExecutorService executor;
    private ServerStatusWrapper serverStatus;
    private ConcurrentMap<String, ServerInfo> map;
    
    private final boolean IS_CENTRAL_SERVER;

    public Server(String name, boolean isCentralServer) {
        this.IS_CENTRAL_SERVER = isCentralServer;
        this.name = name;
        this.serverStatus = ServerStatusWrapper.getInstance();
        this.serverStatus.setServerName(name);
        this.map = new ConcurrentHashMap<>();
        this.init();
    }

    public Server(String name) {
        this.IS_CENTRAL_SERVER = false;
        this.name = name;
        this.serverStatus = ServerStatusWrapper.getInstance();
        this.serverStatus.setServerName(name);
        this.map = new ConcurrentHashMap<>();
        this.init();
    }

    private void init() {

        serverStatus.update(ServiceType.WEBSERVICE.name(), new ServerInfo());
        serverStatus.update(ServiceType.DATABASESERVICE.name(), new ServerInfo());

        if (!IS_CENTRAL_SERVER) {
            executor = Executors.newScheduledThreadPool(2);

            executor.scheduleWithFixedDelay(new ServerAnalyzeThread(name, serverStatus), 0, 1, TimeUnit.SECONDS);
            executor.scheduleWithFixedDelay(new MonitoringAgentThread(), 3, 6, TimeUnit.SECONDS);
            executor.schedule(() -> {
    
                executor.shutdownNow();
    
            }, 1, TimeUnit.DAYS);
            
        } else {
            executor = Executors.newScheduledThreadPool(3);
            
            executor.scheduleWithFixedDelay(new SubCentralServerThread(map), 0, 1, TimeUnit.SECONDS);
            executor.scheduleWithFixedDelay(new ServiceOrderThread(), 10, 2, TimeUnit.SECONDS);
            executor.scheduleWithFixedDelay(new PrintServerThread(), 0, 2, TimeUnit.SECONDS);
            executor.schedule(() -> {
                
                executor.shutdownNow();
                
            }, 1, TimeUnit.DAYS);

            System.out.println("=========== Central Server ===========");
        }
        
    }

}
