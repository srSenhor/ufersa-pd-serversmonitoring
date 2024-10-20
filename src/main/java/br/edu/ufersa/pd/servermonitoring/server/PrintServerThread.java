package br.edu.ufersa.pd.servermonitoring.server;

import br.edu.ufersa.pd.servermonitoring.utils.GUI;
import br.edu.ufersa.pd.servermonitoring.utils.ServerStatusWrapper;
import br.edu.ufersa.pd.servermonitoring.utils.ServiceType;

public class PrintServerThread implements Runnable {

    private ServerStatusWrapper serverStatus;

    public PrintServerThread() {
        this.serverStatus = ServerStatusWrapper.getInstance();
    }

    @Override
    public void run() {
        GUI.clearScreen();
        GUI.customServerStatus(serverStatus.get(ServiceType.WEBSERVICE.name()));
        GUI.customServerStatus(serverStatus.get(ServiceType.DATABASESERVICE.name()));
    }
}
