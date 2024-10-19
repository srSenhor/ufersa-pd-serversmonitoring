package br.edu.ufersa.pd.servermonitoring.server;

import br.edu.ufersa.pd.servermonitoring.utils.ServiceType;

public class ServerApp {
    public static void main(String[] args) {
        // new Server("Server 1", ServiceType.WEBSERVICE);
        new Server("Server 1", ServiceType.DATABASESERVICE);
    }
}
