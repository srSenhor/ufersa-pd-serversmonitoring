package br.edu.ufersa.pd.servermonitoring.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClusterExample {

    public ClusterExample() { this.init(); }

    private void init() {
        ExecutorService executor = Executors.newCachedThreadPool();

        executor.submit(() -> {
            new Server("Server 1");
        });
        executor.submit(() -> {
            new Server("Server 2");
        });
        executor.submit(() -> {
            new Server("Server 3");
        });
    }
}
