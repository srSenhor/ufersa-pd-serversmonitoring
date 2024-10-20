package br.edu.ufersa.pd.servermonitoring.utils;

import br.edu.ufersa.pd.servermonitoring.entities.ServerInfo;

public class GUI {

    private static final String RESET = "\u001B[0m";
    private static final String BLACK = "\u001B[30m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String WHITE = "\u001B[37m";

    private static final String BG_BLACK = "\u001B[40m";
    private static final String BG_RED = "\u001B[41m";
    private static final String BG_YELLOW = "\u001B[43m";
    private static final String BG_BLUE = "\u001B[44m";
    private static final String BG_WHITE = "\u001B[47m";

    private static final String BOLD = "\u001B[1m"; 

    public static void dummyTest() {
        System.out.println(BG_RED + WHITE + "[=================== Server 1 ===================]" + RESET);
        System.out.println(BG_WHITE + BLACK + "texto" + RESET);
        System.out.println(BG_WHITE + BLACK + "texto" + RESET);
        System.out.println(BG_WHITE + BOLD + RED + "CRITICAL" + RESET);
        System.out.println(BG_RED + WHITE + "[================================================]" + RESET);
    }

    public static void customServerStatus(ServerInfo info) {

        Status status = info.getStatus();
        switch (status) {
            case OK:
                System.out.println(BG_BLUE + WHITE + "[=================== " + info.getServerName() + " ===================]" + RESET);
                printInfo(info);
                System.out.println(printStatus(status));
                System.out.println(BG_BLUE + WHITE + "[================================================]" + RESET);
                break;
            case WARNING:
                System.out.println(BG_YELLOW + WHITE + "[=================== " + info.getServerName() + " ===================]" + RESET);
                printInfo(info);
                System.out.println(printStatus(status));
                System.out.println(BG_YELLOW + WHITE + "[==============================================]" + RESET);
                break;
            case CRITICAL:
                System.out.println(BG_RED    + WHITE + "[=================== " + info.getServerName() + " ===================]" + RESET);
                printInfo(info);
                System.out.println(printStatus(status));
                System.out.println(BG_RED + WHITE + "[==================================================]" + RESET);
                break;
        
            default:
                break;
        }

        System.out.println();
    }

    public static String customServiceOrder(ServerInfo info, String problem, String actionRequired) {
        StringBuilder response = new StringBuilder();
        
        response.append(BG_BLACK + WHITE + "*->             Service order             <-*" + RESET + "\n");
        response.append(printPipe() + "Timestamp: " + info.getTimestamp() + RESET + "\n");
        response.append(printPipe() + "Server: " + info.getServerName() + RESET + "\n");
        response.append(printPipe() + "Service: " + info.getServiceType() + RESET + "\n");
        response.append(printPipe() + printStatus(info.getStatus()) + "\n"); 
        response.append(printPipe() + "Problem: " + problem + RESET + "\n");
        response.append(printPipe() + "Action Required: " + actionRequired + RESET + "\n");
        response.append(BG_BLACK + WHITE + "*->                                       <-*" + RESET + "\n");        

        return response.toString();
    }

    private static String printStatus(Status status) {
        return switch (status) {
            case OK         -> ("Status: " + BOLD + BLUE + status + RESET);
            case WARNING    -> ("Status: " + BOLD + YELLOW + status + RESET);
            case CRITICAL   -> ("Status: " + BOLD + RED +  status + RESET);
            default         -> ("Status: " + BOLD + PURPLE + "undefined" + RESET);
        };
    }

    private static String printPipe() {
        return BG_WHITE + BLACK + "|" + RESET;
    }

    private static void printInfo(ServerInfo info) {
        System.out.println(WHITE + "Timestamp: " + info.getTimestamp() + RESET);
        System.out.println(WHITE + "Service: " + info.getServiceType() + RESET);
        System.out.println(WHITE + "Metrics:" + RESET);
        System.out.println(WHITE + "|> CPU Usage: " + String.format("%.2f", info.getCpuUsage()) + "%" + RESET);
        System.out.println(WHITE + "|> Memory Usage: " + String.format("%.2f", info.getMemoryUsage()) + "%" + RESET);
        System.out.println(WHITE + "|> Active Connections (capacity): " + String.format("%.2f", info.getActiveConnections()) + "%" + RESET);
        System.out.println(WHITE + "|> Response Time: " + info.getResponseTime() + "ms" + RESET);
    }

    public static void userScreen() {
        System.out.println("""
                =========--- Service Orders ---=========
                """);
    }

    public static void clearScreen() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                // Se estiver no Windows, usa o comando "cls" para limpar o console.
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Se estiver em outro sistema operacional (como Linux ou macOS), usa o comando "clear".
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
