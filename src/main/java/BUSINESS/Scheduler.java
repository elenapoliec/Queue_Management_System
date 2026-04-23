package BUSINESS;

import MODEL.Server;
import MODEL.Task;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Server> servers;
    private int maxNoServers;
    private int maxTasksPerServer;
    private Strategy strategy;

    public Scheduler(int maxNoServers, int maxTasksPerServer) {
        this.maxNoServers = maxNoServers;
        this.maxTasksPerServer = maxTasksPerServer;
        this.servers = new ArrayList<>();

        // inițializez cele Q servere (cozi)
        for (int i = 0; i < maxNoServers; i++) {
            // creez un server nou cu capacitatea maximă de task-uri
            Server server = new Server(maxTasksPerServer);
            servers.add(server);

            // fiecare server(client) are propriul sau fir de execuție(thread)
            Thread t = new Thread(server);
            t.start();
        }
    }

    public void changeStrategy(SelectionPolicy policy) {
        if (policy == SelectionPolicy.SHORTEST_QUEUE) {
            strategy = new ConcreteStrategyQueue();
        }
        if (policy == SelectionPolicy.SHORTEST_TIME) {
            strategy = new ConcreteStrategyTime();
        }
    }

    //trimit clientul catre o coada
    public Server dispatchTask(Task t) {
        return strategy.addTask(servers, t);
    }


    public List<Server> getServers() {
        return servers;
    }



}
