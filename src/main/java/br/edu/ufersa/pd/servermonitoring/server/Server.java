package br.edu.ufersa.pd.servermonitoring.server;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import br.edu.ufersa.pd.servermonitoring.entities.ServerInfo;
import br.edu.ufersa.pd.servermonitoring.utils.ServiceType;

public class Server {

    private String name;
    private ScheduledExecutorService executor;
    private ServerStatusWrapper serverStatus;
    
    private final boolean IS_CENTRAL_SERVER;
    private final int N_THREADS = 3;

    public Server(String name, boolean isCentralServer) {
        this.IS_CENTRAL_SERVER = isCentralServer;
        this.name = name;
        this.executor = Executors.newScheduledThreadPool(N_THREADS);
        this.serverStatus = ServerStatusWrapper.getInstance();
        this.serverStatus.setServerName(name);
        this.init();
    }

    public Server(String name) {
        this.IS_CENTRAL_SERVER = false;
        this.name = name;
        this.executor = Executors.newScheduledThreadPool(N_THREADS);
        this.serverStatus = ServerStatusWrapper.getInstance();
        this.serverStatus.setServerName(name);
        this.init();
    }

    private void init() {

        serverStatus.update(ServiceType.WEBSERVICE.name(), new ServerInfo());
        serverStatus.update(ServiceType.DATABASESERVICE.name(), new ServerInfo());

        if (!IS_CENTRAL_SERVER) {
            executor.scheduleWithFixedDelay(new ServerAnalyzeThread(name, serverStatus), 0, 5, TimeUnit.SECONDS);
            executor.scheduleWithFixedDelay(new MonitoringAgentThread(), 10, 1, TimeUnit.SECONDS);
            executor.schedule(() -> {
    
                executor.shutdownNow();
    
            }, 1, TimeUnit.DAYS);
            
        } else {
            
            executor.scheduleWithFixedDelay(new SubCentralServerThread(), 0, 2, TimeUnit.SECONDS);
            executor.schedule(() -> {
                
                executor.shutdownNow();
                
            }, 1, TimeUnit.DAYS);

            System.out.println("=========== Central Server ===========");
        }



        ConcurrentMap<String, ServerInfo> map = null;

        for (int i = 0; i < 100; i++) {

            try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

            map = serverStatus.get();

            System.out.println(map.get(ServiceType.WEBSERVICE.name()));
            System.out.println(map.get(ServiceType.DATABASESERVICE.name()));
        }
        
    }

}
