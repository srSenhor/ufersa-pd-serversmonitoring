package br.edu.ufersa.pd.servermonitoring.server;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import br.edu.ufersa.pd.servermonitoring.entities.ServerInfo;

public class ServerStatusWrapper {

    private static String serverName;
    private static ServerStatusWrapper instance;
    private static ConcurrentMap<String, ServerInfo> services;

    private ServerStatusWrapper() {
        services = new ConcurrentHashMap<>();
    }

    public static ServerStatusWrapper getInstance() {
        if (instance == null) {
            instance = new ServerStatusWrapper();
        }

        return instance;
    }

    public void update(String key, ServerInfo value) {
        services.put(key, value);
    }

    public ConcurrentMap<String, ServerInfo> get() {
        return services;
    }

    public ServerInfo get(String key) {
        return services.get(key);
    }

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		ServerStatusWrapper.serverName = serverName;
	}

}
